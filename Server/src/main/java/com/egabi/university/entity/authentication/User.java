package com.egabi.university.entity.authentication;

import com.egabi.university.entity.Instructor;
import com.egabi.university.entity.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @OneToOne(mappedBy = "user")
    private Student student;
    
    @OneToOne(mappedBy = "user")
    private Instructor instructor;
    
    @Column(name = "locked", nullable = false)
    private boolean locked = false;
    
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    
    @Override
    public String getUsername() {
        return this.email;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
