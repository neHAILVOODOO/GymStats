package com.voodoo.gymstats.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;
    private String name;
    private String surname;
    private String filename;
    private LocalDate lastVisitDate;
    private LocalDate createdOnDate;

    private String active;


    private Long trainerId;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Details userDetails;

    @OneToMany(mappedBy = "user", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Training> trainings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<TrainingProgram> trainingPrograms;

    @Enumerated(EnumType.STRING)
    private UserRole role;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override

    public String getUsername() {
        return getName() + getSurname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public static String getUserRole(String role) {

        if (role.equals(UserRole.STUDENT.name())) {
            return "Занимающийся";
        }
        else if (role.equals(UserRole.TRAINER.name())) {
            return "Тренер";
        }
        else if (role.equals(UserRole.MANAGER.name())) {
            return "Менеджер";
        }
        else if (role.equals(UserRole.FOUNDER.name())) {
            return "Глава компании";
        } else {
            return "пвывпвпывапывпваыпыв";
        }

    }


}
