# ForoHub

## ¿Qué es ForoHub?

ForoHub es una plataforma de discusión y ayuda para entusiastas de la programación, tanto principiantes como expertos. Aquí podrás compartir tus conocimientos, resolver dudas y aprender de otros miembros de la comunidad.

## Funcionalidades:

Creación de temas: Inicia nuevas discusiones sobre cualquier tema relacionado con la programación.
Participación en debates: Responde a temas existentes, comparte tus ideas y debate con otros usuarios.
Interacción con la comunidad: Conéctate con otros programadores, aprende de sus experiencias y colabora en la resolución de problemas.
Instalación:

ForoHub es una API REST que te permite integrar fácilmente las funcionalidades de un foro en tu aplicación. Para utilizarla, sigue estos pasos:

## Uso:
- Regístrate: Crea una cuenta gratuita para acceder a todas las funcionalidades de ForoHub
- Explora los temas: Utiliza los endpoints de la API para obtener la lista de temas disponibles.
- Crea un nuevo tema: Envía una petición al endpoint correspondiente con la información del nuevo tema.
- Participa en los debates: Utiliza los endpoints para obtener las respuestas de un tema y enviar nuevas respuestas.

## Tecnologías utilizadas:

Java (versión 21): Lenguaje de programación principal utilizado para desarrollar la API.
Maven (versión 8.3): Herramienta de gestión de dependencias y construcción del proyecto.
Spring Boot (versión 3.3.1): Framework para la creación de aplicaciones web Java, que facilita la configuración y el despliegue.
RESTful API: Arquitectura para la comunicación entre aplicaciones a través de HTTP.
JSON: Formato de de intercambio de datos utilizado para las respuestas de la API.

MySQL (versión 5.5.0): Sistema de gestión de bases de datos relacional utilizado para almacenar la información del foro.

## Ejemplos de código (Java):

    @RequestMapping("/login")

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserRegistration registration) {
        // ... (código para registrar un nuevo usuario)
    }

    @PostMapping()
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLogin login) {
        // ... (código para iniciar sesión)
    }

La API devuelve respuestas en formato JSON.
