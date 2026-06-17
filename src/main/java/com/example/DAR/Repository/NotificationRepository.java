package com.example.DAR.Repository;

import com.example.DAR.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Notification findNotificationById(Integer id);

    List<Notification> findNotificationsByUserId(Integer userId);

    List<Notification> findNotificationsByUserIdAndIsRead(Integer userId, Boolean isRead);
}
