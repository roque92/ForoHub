package org.example.forohub.dtos.cursoDTO;

import jakarta.validation.constraints.NotNull;

@NotNull(message = "Categoria del curso es requerida")
public enum ExistingCourses {
    JAVA("Java", "java"),
    JAVASCRIPT("JavaScript", "javascript", "js"),
    REACT("React", "react"),
    ANGULAR("Angular", "angular"),
    VUE("Vue", "vue"),
    HTML("HTML", "html"),
    CSS("CSS", "css"),
    NODEJS("Node.js", "nodejs"),
    EXPRESS("Express", "express"),
    MONGODB("MongoDB", "mongodb"),
    PYTHON("Python", "python", "py"),
    KOTLIN("Kotlin", "kotlin"),
    GO("Go", "go"),
    SWIFT("Swift", "swift"),
    CSHARP("C#", "csharp"),
    CPLUSPLUS("C++", "c++"),
    C("c"),
    TYPESCRIPT("TypeScript", "typescript"),
    PHP("php"),
    RUBY("Ruby", "ruby"),
    RUST("Rust", "rust"),
    OTHER;

    private final String[] names;

    ExistingCourses(String... names) {
        this.names = names;
    }

    public boolean matches(String name) {
        for (String variant : names) {
            if (variant.equalsIgnoreCase(name)) {
                return true; // Return true if a match is found
            }
        }
        return false;
    }
}