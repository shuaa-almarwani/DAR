package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.PaymentDtoIn;
import com.example.DAR.DTO.Out.PaymentDtoOut;
import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Payment;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.PaymentRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    public List<PaymentDtoOut> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDtoOut> dtoOuts = new ArrayList<>();

        for (Payment payment : payments) {
            PaymentDtoOut dto = modelMapper.map(payment, PaymentDtoOut.class);
            dto.setUserSubscriptionId(payment.getUserSubscription().getId());
            dtoOuts.add(dto);
        }

        return dtoOuts;
    }

public void addPayment(Integer userSubscriptionId, PaymentDtoIn dto) {

    UserSubscription userSubscription =
            userSubscriptionRepository.findUserSubscriptionById(userSubscriptionId);

    if (userSubscription == null) {
        throw new ApiException("User subscription not found");
    }

    if (userSubscription.getPaymentStatus() != PaymentStatus.UNPAID) {
        throw new ApiException("Subscription payment is not unpaid");
    }

    Payment payment = new Payment();

    payment.setUserSubscription(userSubscription);
    payment.setAmount(userSubscription.getSubscriptionPlan().getPrice());
    payment.setPaymentMethod(dto.getPaymentMethod());
    payment.setTransactionReference(dto.getTransactionReference());
    payment.setPaymentDate(LocalDate.now());
    payment.setStatus(PaymentStatus.PAID);

    paymentRepository.save(payment);

    userSubscription.setStatus(UserSubscriptionStatus.ACTIVE);
    userSubscription.setPaymentStatus(PaymentStatus.PAID);
    userSubscriptionRepository.save(userSubscription);
    notificationService.sendSubscriptionActivatedNotification(
            userSubscription.getUser(),
            userSubscription.getSubscriptionPlan().getName()
    );
}

    public void deletePayment(Integer id) {
        Payment payment = paymentRepository.findPaymentById(id);

        if (payment == null) {
            throw new ApiException("Payment not found");
        }

        paymentRepository.delete(payment);
    }

}
