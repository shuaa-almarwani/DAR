package com.example.DAR.Repository;

import com.example.DAR.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findPaymentById(Integer id);

    @Query("select p from Payment p where p.userSubscription.user.id = ?1")
    List<Payment> findByUser(Integer userId);
}
