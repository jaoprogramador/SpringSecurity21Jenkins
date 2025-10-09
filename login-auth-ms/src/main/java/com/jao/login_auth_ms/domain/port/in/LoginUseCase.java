package com.jao.login_auth_ms.domain.port.in;

//Interface para el caso de uso de inicio de sesión
public interface LoginUseCase {
 
 // Retorna el token JWT o una respuesta de éxito si el login es válido
 String authenticate(String email, String password);
 
 // Lógica para manejar el CRUD (como crear un usuario)
 // Usaremos un puerto separado para mantener la cohesión
}
