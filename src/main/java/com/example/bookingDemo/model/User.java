package com.example.bookingDemo.model;

import com.example.bookingDemo.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * Element Collection is used to define one to many relationship to an embedable object or a baic value (such as collection of string)
     * The embedded object is not stored
     * **/
    @ElementCollection(fetch = FetchType.EAGER)
    /**
     * Name parameter refers to the name of table where the collection of roles will be stored (not necessarily an entity)
     * Join column parameters will be the column connecting the users table with users_roles table
     * Collection table is normally used for collections of embeddable items rather than entities (contrast to @OneToOneMapping etc)
     **/
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    /**
     * Dictates how the enum values are stored in the database
     **/
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<RoleEnum> role = new HashSet<>();

}
