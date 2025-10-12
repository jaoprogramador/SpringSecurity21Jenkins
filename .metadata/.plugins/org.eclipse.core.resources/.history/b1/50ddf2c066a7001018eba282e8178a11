package com.jao.login_auth_ms.infrastructure.adapter.in;
import com.jao.login_auth_ms.application.dto.UserResponse;
import com.jao.login_auth_ms.domain.model.User;
import com.jao.login_auth_ms.domain.port.in.UserManagementPort;
import com.jao.login_auth_ms.infrastructure.adapter.in.UserController;
//import com.jao.login_auth_ms.infrastructure.adapter.in.dto.UserResponse; 

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Habilita el soporte de Mockito para JUnit 5
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    // Mockea la dependencia de lógica de negocio
    @Mock
    private UserManagementPort userManagementPort;

    // Mockea la dependencia de mapeo
    @Mock
    private ModelMapper modelMapper;

    // Inyecta los mocks en el controlador, la clase bajo prueba
    @InjectMocks
    private UserController userController;

    @Test
    void getAll_shouldReturnListOfUsersWithSecurityDetailsAndHttpStatus200() {
        // --- ARRANGE (Configuración) ---

        LocalDateTime lockTime1 = LocalDateTime.now().minusHours(1);

        // 1. Crea dos objetos User (Domain Model) con datos completos, incluyendo seguridad
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("juan.perez");
        user1.setEmail("juan.perez@test.com");
        user1.setFailedLoginAttempts(3);
        user1.setAccountNonLocked(true);
        user1.setRole("ADMIN");
        user1.setLockTime(null); // No está bloqueado

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("ana.gomez");
        user2.setEmail("ana.gomez@test.com");
        user2.setFailedLoginAttempts(5);
        user2.setAccountNonLocked(false);
        user2.setRole("USER");
        user2.setLockTime(lockTime1); // Bloqueado

        List<User> mockUsers = Arrays.asList(user1, user2);

        // 2. Crea los DTOs de Respuesta esperados, replicando los datos de User
        UserResponse response1 = new UserResponse(1L, "juan.perez", "juan.perez@test.com", 3, true, null, "ADMIN");
        UserResponse response2 = new UserResponse(2L, "ana.gomez", "ana.gomez@test.com", 5, false, lockTime1, "USER");

        // 3. Define el comportamiento del puerto (UserManagementPort)
        when(userManagementPort.findAll()).thenReturn(mockUsers);

        // 4. Define el comportamiento del ModelMapper: simula la conversión
        when(modelMapper.map(user1, UserResponse.class)).thenReturn(response1);
        when(modelMapper.map(user2, UserResponse.class)).thenReturn(response2);


        // --- ACT (Acción) ---
        ResponseEntity<List<UserResponse>> responseEntity = userController.getAll();

        // --- ASSERT (Verificación) ---

        // 1. Verifica el Código de Estado HTTP
        assertEquals(200, responseEntity.getStatusCodeValue(), 
                     "Debería devolver un código de estado 200 OK.");

        // 2. Verifica el Contenido y la Integridad de la Respuesta
        List<UserResponse> actualResponseList = responseEntity.getBody();
        
        // Comprueba tamaño
        assertEquals(2, actualResponseList.size(), 
                     "La lista de respuesta debe contener 2 elementos.");
        
        // Comprueba los campos de seguridad del primer usuario (no bloqueado)
        UserResponse actualResponse1 = actualResponseList.get(0);
        assertEquals(1L, actualResponse1.getId());
        assertEquals("juan.perez@test.com", actualResponse1.getEmail());
        assertEquals(3, actualResponse1.getFailedLoginAttempts());
        assertEquals(true, actualResponse1.isAccountNonLocked());
        assertEquals("ADMIN", actualResponse1.getRole());
        
        // Comprueba los campos de seguridad del segundo usuario (bloqueado)
        UserResponse actualResponse2 = actualResponseList.get(1);
        assertEquals(5, actualResponse2.getFailedLoginAttempts());
        assertEquals(false, actualResponse2.isAccountNonLocked());
        assertEquals(lockTime1, actualResponse2.getLockTime());
        assertEquals("USER", actualResponse2.getRole());

        // 3. Verifica las interacciones con los Mocks
        
        // El puerto de gestión fue llamado una vez
        verify(userManagementPort, times(1)).findAll();
        
        // El ModelMapper fue llamado dos veces (una por cada mapeo)
        verify(modelMapper, times(mockUsers.size())).map(any(User.class), eq(UserResponse.class));
    }
}








