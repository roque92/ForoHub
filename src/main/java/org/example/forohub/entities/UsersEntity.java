package org.example.forohub.entities;

import java.util.List;

import org.example.forohub.dtos.userDTO.UserRegistration;

import jakarta.persistence.Column;
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
public class UsersEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombre")
    private String name;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "author_id")
    private List<TopicEntity> usersTopic;

    public UsersEntity(UserRegistration registration) {
        this.name = registration.name();
        this.email = registration.email();
        this.password = registration.password();
    }

    
    
}
