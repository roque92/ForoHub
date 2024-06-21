package org.example.forohub.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class usersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonAlias("id")
    private Long id;
    @JsonAlias("nombre")
    private String name;
    @JsonAlias("email")
    private String email;
    @JsonAlias("password")
    private String password;

    @OneToMany(mappedBy = "author_id")
    private List<topicEntity> usersTopic;
    
}
