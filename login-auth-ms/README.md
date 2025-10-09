com.jao.loginAuth
├── domain/                  (El Núcleo - Lógica Pura de Negocio)
│   ├── model/               (Entidades de Dominio, P. ej., Usuario, LogginAttempt)
│   ├── port/                (Interfaces/Puertos)
│   │   ├── in/              (Puertos de Entrada/Driven - Casos de Uso, P. ej., LoginServicePort)
│   │   └── out/             (Puertos de Salida/Driving - DB, Cache, Email, P. ej., UserRepositoryPort)
│   └── service/             (Implementaciones de los Puertos de Entrada - Use Cases)
│
├── application/             (Capa de Aplicación - DTOs y Mappers)
│   ├── dto/                 (Objetos de Transferencia de Datos, P. ej., LoginRequest, UserDTO)
│   └── mapper/              (ModelMapper Config y Clases de Mapeo)
│
└── infrastructure/          (Adaptadores - Conexión al Mundo Exterior)
    ├── adapter/
    │   ├── in/              (Adaptadores de Entrada - Llama a los Puertos de Entrada, P. ej., LoginController)
    │   └── out/             (Adaptadores de Salida - Implementa Puertos de Salida)
    │       ├── persistence/ (Adaptador DB, P. ej., UserRepositoryAdapterImpl)
    │       ├── cache/       (Adaptador Redis para Contadores de Login)
    │       └── email/       (Adaptador Email para Notificación de Bloqueo)
    ├── config/              (Configuraciones de Spring: Security, ModelMapper, Resilience4j, Redis)
    └── exception/           (Manejo centralizado de Excepciones - @ControllerAdvice)

    
    ✅ Resumen del Proyecto Terminado
Hemos completado todos los requisitos bajo la Arquitectura Hexagonal:

Proyecto: Spring Boot 3.2.x, Java 21, Maven, JAR.

Arquitectura: Estructura de paquetes Hexagonal (domain, application, infrastructure).

Dependencias: Web, Data JPA, Security, Validation, Lombok, Resilience4j, SLF4J, Redis, PostgreSQL, ModelMapper.

Seguridad: Security by Design/Default con encriptado de claves (Hash y Salt vía BCrypt).

Lógica Clave: Control de 3 intentos de login, bloqueo de usuario, y notificación por email.

API: CRUD (GET, POST, PUT, DELETE, PATCH) expuesto por controladores REST.

Manejo de Errores: Validaciones de entrada (@Valid) y control de excepciones global.