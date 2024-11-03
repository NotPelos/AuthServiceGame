package com.example.gameservice.web;

import com.example.gameservice.domain.Notification;
import com.example.gameservice.port.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(@RequestHeader("User-Id") Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }
}
