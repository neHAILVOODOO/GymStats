package com.voodoo.gymstats.dto;

import com.voodoo.gymstats.model.Details;
import com.voodoo.gymstats.model.Training;
import com.voodoo.gymstats.model.TrainingProgram;
import com.voodoo.gymstats.model.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class UserDto {

    private Long id;
    @NotEmpty(message = "Это поле должно быть заполнено")
    private String login;
    @NotEmpty(message = "Это поле должно быть заполнено")
    private String password;
    @NotEmpty(message = "Это поле должно быть заполнено")
    @Length(max = 20,message = "Введите менее 20 символов")
    private String name;
    @NotEmpty(message = "Это поле должно быть заполнено")
    @Length(max = 20,message = "Введите менее 20 символов")
    private String surname;

    private LocalDate lastVisitDate;
    private LocalDate createdOnDate;

    private String filename;
    private Long trainerId;
    @Valid
    private Details userDetails;
    private String active;


    private List<Training> trainings;

    private List<TrainingProgram> trainingPrograms;



    private UserRole role;


}
