package com.jao.login_auth_ms.domain.port.out;

//Contrato para la notificación por correo (Implementado por Spring Mail Adapter)
public interface EmailNotificationPort {
 
 void notifyAccountLocked(String recipientEmail, String username);
}
