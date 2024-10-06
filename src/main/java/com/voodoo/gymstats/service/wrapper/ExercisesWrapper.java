package com.voodoo.gymstats.service.wrapper;

import com.voodoo.gymstats.model.Cardio;
import com.voodoo.gymstats.model.Exercise;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ExercisesWrapper {

    private int todayWeight;
    private List<Exercise> exercises;
    private List<Cardio> cardios;


}
