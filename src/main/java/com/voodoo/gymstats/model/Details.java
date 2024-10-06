package com.voodoo.gymstats.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "details")
public class Details {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^8\\d{10}$|^$", message = "Введите номер телефона в соответствии с примером: 81234567890")
    private String phone;

    @Length(max = 40, message = "Пожалуйста, введите до 40 символов")
    @Pattern(regexp = "^.+@.+\\..+$|^$", message = "Введите электронную почту в соответствии с примером: jimmy@gmail.com")
    private String email;

    @Max(value = 70, message = "Пожалуйста, введите значение до 70")
    private int experience;

    @Length(max = 20,message = "Введите менее 20 символов")
    @Pattern(regexp = "^@.+|^$", message = "Введите имя, начиная с \"@\"")
    private String tgLink;

    @Length(max = 20,message = "Введите менее 20 символов")
    @Pattern(regexp = "[^/]+|^$", message = "Введите имя пользователя, а не ссылку")
    private String instaLink;

    @Length(max = 20,message = "Введите менее 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$|^$", message = "Введите ID вашей страницы, состоящий из букв или цифр, например 'id5436234' ")
    private String vkLink;

    @Length(max = 20,message = "Введите менее 20 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$|^$", message = "Введите ID канала, состоящий из букв или цифр, например 'myid123' ")
    private String youtubeLink;

    @Length(max = 100,message = "Введите менее 100 символов")
    private String description;


    @OneToOne()
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    public void changeDetails(Details change) {
        this.setPhone(change.getPhone());
        this.setEmail(change.getEmail());
        this.setExperience(change.getExperience());

        this.setTgLink(change.getTgLink());
        this.setInstaLink(change.getInstaLink());
        this.setVkLink(change.getVkLink());
        this.setYoutubeLink(change.getYoutubeLink());

        this.setDescription(change.getDescription());
    }

    public static String getYearEnding(int years) {
        if (years % 10 == 1 && years % 100 != 11) {
            return "год";
        } else if (years % 10 >= 2 && years % 10 <= 4 && (years % 100 < 12 || years % 100 > 14)) {
            return "года";
        } else {
            return "лет";
        }
    }


}
