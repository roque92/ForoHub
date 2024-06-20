CREATE TABLE topics(
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    creationDate DATE NOT NULL,
    status BOOLEAN NOT NULL,
    author VARCHAR(100) NOT NULL,
    curso_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    PRIMARY KEY(id),
    FOREIGN KEY(curso_id) REFERENCES cursos(id)
    FOREIGN KEY(user_id) REFERENCES users(id)
);
    );