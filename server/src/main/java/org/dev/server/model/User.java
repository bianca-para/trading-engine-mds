package org.dev.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dev.server.model.enums.KYCStatus;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

//import org.dev.server.model.enums.KycStatus;
//import org.dev.server.model.enums.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Username should not be blank.")
    @Size(min = 1, max = 32, message = "Username length should be between 1 and 32 characters long.")
    private String username;

    @NotNull(message = "User email should not be null.")
    @Email(message = "Invalid format for email.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password should not be empty.")
    private String password;

    @Enumerated(EnumType.STRING)
    private KYCStatus kycStatus;

//    @Enumerated(EnumType.STRING)
//    private Role role;


    @Column(name = "registered_date", columnDefinition = "DATE")
    private LocalDate registeredDate;

//
    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(
            name="roles",
            joinColumns = @JoinColumn(name="user_id")
    )
    @Column(name="user_role")
    private Set<String> roles;

    //constructor fara id, pt ca e generat automat in bd
    public User(String username, String email, String password, String kycStatus, LocalDate registeredDate, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.kycStatus = KYCStatus.valueOf(kycStatus);
        this.registeredDate = registeredDate;
        this.roles = roles;
    }
}