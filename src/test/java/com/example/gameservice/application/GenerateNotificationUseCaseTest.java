package com.example.gameservice.application;

import com.example.authservice.domain.User;
import com.example.gameservice.domain.Game;
import com.example.gameservice.domain.GameSession;
import com.example.gameservice.domain.Notification;
import com.example.gameservice.port.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class GenerateNotificationUseCaseTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private GenerateNotificationUseCase generateNotificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateSessionCompletionNotification() {
        // Crear un usuario de prueba y asignarle un ID
        User user = new User();
        user.setId(1L); // Asegúrate de asignar un ID u otros atributos necesarios
        user.setUsername("testuser");

        // Crear un juego de prueba y asignarle el usuario
        Game game = new Game();
        game.setTitle("Juego de Prueba");
        game.setUser(user); // Asigna el usuario al juego

        // Crear una sesión de juego de prueba y asignarle el juego y la duración
        GameSession gameSession = new GameSession();
        gameSession.setGame(game);
        gameSession.setDurationInMinutes(60);

        // Crear un mock explícito para la notificación
        Notification mockNotification = mock(Notification.class);
        
        // Simular el guardado de la notificación en el repositorio
        when(notificationRepository.save(any(Notification.class))).thenReturn(mockNotification);

        // Llamar al método que se va a probar
        generateNotificationUseCase.generateSessionCompletionNotification(gameSession);

        // Verificar que el repositorio de notificaciones fue llamado con cualquier instancia de Notification
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

}
