package com.example.backend.repositories;

import com.example.backend.models.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    //todo might need to change to native query
    List<Notification> findByUserId(long userId);
}
