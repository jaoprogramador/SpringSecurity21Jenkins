package com.jao.login_auth_ms.infrastructure.adapter.out.email;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.jao.login_auth_ms.domain.port.out.EmailNotificationPort;

@Component
@RequiredArgsConstructor
public class SmtpEmailNotificationAdapter implements EmailNotificationPort {

    // SLF4J Logger para registrar la actividad de envío
    private static final Logger log = LoggerFactory.getLogger(SmtpEmailNotificationAdapter.class);
    
    private final JavaMailSender mailSender;
    
    // Aquí puedes inyectar el email del remitente desde application.properties
    private final String senderEmail = "no-reply@login-auth-ms.com"; 

    @Override
    public void notifyAccountLocked(String recipientEmail, String username) {
        
        log.warn("Intentando enviar notificación de bloqueo a: {}", recipientEmail);
        
        try {
            // Uso de lambda: Runnable para ejecutar el envío en un hilo separado (no bloqueante)
            Runnable sendEmailTask = () -> {
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
                    
                    helper.setFrom(senderEmail);
                    helper.setTo(recipientEmail);
                    helper.setSubject("🚨 Aviso de Seguridad: Cuenta Bloqueada");
                    
                    String content = createEmailContent(username);
                    helper.setText(content, true); // true para HTML
                    
                    mailSender.send(helper.getMimeMessage());
                    log.info("Notificación de bloqueo enviada con éxito a {}", recipientEmail);
                    
                } catch (jakarta.mail.MessagingException | MailException e) {
                    // Control de excepciones específico de la infraestructura
                    log.error("Fallo al enviar el email de bloqueo a {}: {}", recipientEmail, e.getMessage());
                }
            };
            
            // Ejecutar el envío (típicamente se usaría un ExecutorService para gestión de hilos, 
            // pero para una implementación sencilla, Spring maneja bien los hilos en mailSender.send())
            new Thread(sendEmailTask).start();

        } catch (Exception e) {
            log.error("Error al preparar el hilo de envío de correo: {}", e.getMessage());
        }
    }
    
    /**
     * Genera el contenido HTML del correo.
     * @param username Nombre del usuario.
     * @return Contenido HTML del correo.
     */
    private String createEmailContent(String username) {
        return "<html><body>"
             + "<h2>Estimado/a " + username + ",</h2>"
             + "<p>Le informamos que su cuenta en nuestro sistema ha sido **bloqueada** temporalmente por exceder el número máximo de intentos de inicio de sesión fallidos (3).</p>"
             + "<p>Esto es una medida de seguridad (**Security by Default**) para proteger su cuenta de ataques de fuerza bruta.</p>"
             + "<p>Por favor, contacte a soporte para solicitar el desbloqueo. Proporcione su email para acelerar el proceso.</p>"
             + "<p>Gracias por su comprensión.</p>"
             + "</body></html>";
    }
}

