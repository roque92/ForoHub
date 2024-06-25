package org.example.forohub.dtos.cursoDTO;

import jakarta.validation.constraints.NotNull;

@NotNull(message = "Categoria del curso es requerida")
public enum ExistingCourses {
    JAVA,
    JAVASCRIPT,
    REACT,
    NODEJS,
    PYTHON,
    GO,
    KOTLIN,
    SWIFT,
    CSHARP,
    CPLUSPLUS,
    C,
    TYPESCRIPT,
    PHP,
    RUBY,
    RUST,
    OTHER;

    public static ExistingCourses fromStringIgnoreCase(String courseName) {
        for (ExistingCourses course : ExistingCourses.values()) {
            if (course.name().equalsIgnoreCase(courseName)) {
                return course;
            }
        }
        return OTHER;
    }
}