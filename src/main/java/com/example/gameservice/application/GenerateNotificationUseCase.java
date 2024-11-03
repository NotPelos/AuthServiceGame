package com.example.gameservice.application;

import com.example.gameservice.domain.GameSession;
import com.example.gameservice.domain.Notification;
import com.example.gameservice.port.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenerateNotificationUseCase {

    private final NotificationRepository notificationRepository;

    public void generateSessionCompletionNotification(GameSession gameSession) {
        String message = "Has completado una sesión de " + gameSession.getDurationInMinutes() + " minutos en el juego " + gameSession.getGame().getTitle();
        Notification notification = new Notification();
        notification.setUserId(gameSession.getGame().getUser().getId());
        notification.setMessage(message);
        
        notificationRepository.save(notification);
    }
}
