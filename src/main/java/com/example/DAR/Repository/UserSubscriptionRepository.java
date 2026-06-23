package com.example.DAR.Repository;

import com.example.DAR.Enums.PaymentStatus;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    UserSubscription findUserSubscriptionById(Integer id);
    List<UserSubscription> findUserSubscriptionsByUserId(Integer userId);
    List<UserSubscription> findUserSubscriptionsByStatus(UserSubscriptionStatus status);
    UserSubscription findUserSubscriptionByUserIdAndStatus(Integer userId, UserSubscriptionStatus status);

    @Query("select s from UserSubscription s where s.user.id = ?1 and s.status in ?2")
    List<UserSubscription> findByUserAndStatuses(Integer userId, List<UserSubscriptionStatus> statuses);

    @Query("select s from UserSubscription s where s.user.id = ?1 and s.status = ?2 and s.id <> ?3")
    List<UserSubscription> findOtherByUserAndStatus(Integer userId, UserSubscriptionStatus status, Integer subscriptionId);

    @Query("select s from UserSubscription s where s.status = ?1 and s.endDate < ?2")
    List<UserSubscription> findExpired(UserSubscriptionStatus status, LocalDate today);

    @Query("select s from UserSubscription s where s.user.id = ?1 and s.subscriptionPlan.lemonSqueezyVariantId = ?2 and s.status = 'PENDING'")
    UserSubscription findPendingByUserAndVariant(Integer userId, String variantId);

    //  to verify daily AI reminder access from the database direct
   // thiss avoids "Lazy Loading errors" from user.getUserSubscriptions()
    boolean existsUserSubscriptionByUserIdAndStatusAndPaymentStatusAndSubscriptionPlan_DailyAIReminder(
            Integer userId,
            UserSubscriptionStatus status,
            PaymentStatus paymentStatus,
            Boolean dailyAIReminder
    );
}
