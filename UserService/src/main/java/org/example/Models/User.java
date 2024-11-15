package org.example.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {
    @Id
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String profilePhoto;
    private String role;
}
