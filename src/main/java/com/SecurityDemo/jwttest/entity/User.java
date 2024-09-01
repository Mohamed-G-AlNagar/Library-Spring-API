package com.SecurityDemo.jwttest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/*Note2: UserDetails is Class from Security which have some methods for Auth to be used*/
// Note2: @Data , @Builder, @AllArgsConstructor, @NoArgsConstructor  comes from Lombok depency
//which used to reduce the code written
@Data  // instead of getters and Setters
@Builder  // to be able to use builder design pattern
@AllArgsConstructor  // instead of no args constructor
@NoArgsConstructor  // instead of  args constructor
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class) //enable automatic auditing of entity changes (createdDate, lastModifiedDate, CreatedBy , ...)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

//    @Column(name = "email")
    @Column(unique = true,name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean accountLocked;
    private boolean enabled;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false) // to not initialize it
    private LocalDateTime lastModifiedDate;

    @Override
//    return list of users roles
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

//    return the email which unique and equal to useName
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


    public String getFullName(){
        return firstName + " " + lastName;
    }


}
