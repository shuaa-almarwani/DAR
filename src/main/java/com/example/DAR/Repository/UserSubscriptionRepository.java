package com.example.DAR.Repository;

import com.example.DAR.Model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    UserSubscription findUserSubscriptionById(Integer id);
    List<UserSubscription> findUserSubscriptionsByUserId(Integer userId);
    List<UserSubscription> findUserSubscriptionsByStatus(String status);

}
