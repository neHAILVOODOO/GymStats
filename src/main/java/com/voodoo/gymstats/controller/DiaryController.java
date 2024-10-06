package com.voodoo.gymstats.controller;

import com.voodoo.gymstats.dto.UserDto;
import com.voodoo.gymstats.model.*;
import com.voodoo.gymstats.service.*;
import com.voodoo.gymstats.service.wrapper.ExercisesWrapper;
import com.voodoo.gymstats.service.wrapper.TrainingProgramWrapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class DiaryController {

    private UserService userService;
    private DetailsService detailsService;
    private ExerciseService exerciseService;
    private CardioService cardioService;
    private TrainingService trainingService;
    private ExerciseProgramService exerciseProgramService;
    private CardioProgramService cardioProgramService;
    private TrainingProgramService trainingProgramService;

    @Autowired
    public DiaryController(UserService userService, DetailsService detailsService,
                           ExerciseService exerciseService, TrainingService trainingService, CardioService cardioService,
                           ExerciseProgramService exerciseProgramService,CardioProgramService cardioProgramService, TrainingProgramService trainingProgramService
    ) {
        this.userService = userService;
        this.detailsService = detailsService;
        this.exerciseService = exerciseService;
        this.cardioService = cardioService;
        this.trainingService = trainingService;
        this.exerciseProgramService = exerciseProgramService;
        this.cardioProgramService = cardioProgramService;
        this.trainingProgramService = trainingProgramService;

    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/users/profile/info")
    public String loadProfile(Model model, @AuthenticationPrincipal User user) {

        LocalDate localDate = LocalDate.now();

        model.addAttribute("user", user);
        model.addAttribute("localDate", localDate);

        return "users-profile-info";
    }

    @GetMapping("/users/profile/diary/{date}")
    public String loadDiary(Model model, @AuthenticationPrincipal User user,  @PathVariable("date") String dateString) {

        LocalDate currentDate;
        TrainingProgram trainingProgram;

        try {
            currentDate = dateString.isEmpty() ? LocalDate.now() : LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            currentDate = LocalDate.now();
        }

        trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(currentDate.getDayOfWeek(), user);
        LocalDate nextDate = currentDate.plusDays(1);
        LocalDate prevDate = currentDate.minusDays(1);



        ExercisesWrapper wrapper = new ExercisesWrapper();
        Training training = trainingService.findTrainingByTrainingDateAndUser(currentDate,user);


        wrapper.setExercises(new ArrayList<>());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());
        wrapper.getExercises().add(new Exercise());

        wrapper.setCardios(new ArrayList<>());
        wrapper.getCardios().add(new Cardio());
        wrapper.getCardios().add(new Cardio());

        if (training != null) {

            wrapper.setTodayWeight(training.getTodayWeight());

            for (int i = 0; i < wrapper.getExercises().size(); i++) {

                Exercise existingExercise = exerciseService.findByTrainingAndPosition(training, i);
                if (existingExercise != null) {
                    wrapper.getExercises().get(i).changeExercise(existingExercise);
                }
            }

            for (int i = 0; i < wrapper.getExercises().size(); i++) {

                Cardio existingCardio = cardioService.findByTrainingAndPosition(training, i);
                if (existingCardio != null) {
                    wrapper.getCardios().get(i).changeCardio(existingCardio);
                }
            }


        } else {

            if (trainingProgram != null) {
                if (trainingProgram.getExercisePrograms() != null) {

                    for (int i = 0; i < trainingProgram.getExercisePrograms().size(); i++) {

                        wrapper.getExercises().get(i).setExerciseFromProgram(trainingProgram.getExercisePrograms().get(i));
                    }
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("nextDate", nextDate);
        model.addAttribute("prevDate", prevDate);

        return "users-profile-diary";
    }

    @PostMapping("/users/profile/diary/{date}")
    public String saveDiary(@ModelAttribute ExercisesWrapper wrapper,
                            @AuthenticationPrincipal User authUser,
                            @PathVariable("date") String dateString
    ) {


        User user = userService.findById(authUser.getId());
        Training training = new Training();
        LocalDate currentDate = LocalDate.now();

        if (!dateString.isEmpty()) {
            currentDate = LocalDate.parse(dateString);
        }

        if (trainingService.findTrainingByTrainingDateAndUser(currentDate, user) != null) {
            training = trainingService.findTrainingByTrainingDateAndUser(currentDate, user);
            training.setTodayWeight(wrapper.getTodayWeight());
            training.getExercises().clear();
            training.getCardios().clear();
        } else {
            training.setUser(user);
            training.setTrainingDate(currentDate);
            training.setTodayWeight(wrapper.getTodayWeight());
            trainingService.saveTraining(training);

        }


        for (int i = 0; i < wrapper.getExercises().size(); i++) {

            Exercise exercise = wrapper.getExercises().get(i);

            if (!exercise.getExerciseName().isEmpty()) {

                if (exerciseService.findByTrainingAndPosition(training, i) != null) {
                    Exercise existingExercise = exerciseService.findByTrainingAndPosition(training, i);
                    existingExercise.changeExercise(exercise);
                    training.addExerciseToTraining(existingExercise);
                } else {
                    exercise.setPosition(i);
                    training.addExerciseToTraining(exercise);
                }
            } else {
                exerciseService.deleteByTrainingAndPosition(training, i);
            }
        }

        for (int i = 0; i < wrapper.getCardios().size(); i++) {

            Cardio cardio = wrapper.getCardios().get(i);

            if (!cardio.getCardioName().isEmpty()) {

                if (cardioService.findByTrainingAndPosition(training, i) != null) {
                    Cardio existingCardio = cardioService.findByTrainingAndPosition(training, i);
                    existingCardio.changeCardio(cardio);
                    training.addCardioToTraining(existingCardio);
                } else {
                    cardio.setPosition(i);
                    training.addCardioToTraining(cardio);
                }
            } else {
                cardioService.deleteByTrainingAndPosition(training, i);
            }
        }

        trainingService.saveTraining(training);

        User updatedUser = userService.findById(authUser.getId());
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);


        return "redirect:/users/profile/diary/" + currentDate;
    }

    @GetMapping("/users/profile/training-program")
    public String loadTrainingProgram(Model model,
                                      @AuthenticationPrincipal User user,
                                      @RequestParam(name = "dayOfWeek", required = false) DayOfWeek dayOfWeek)  {

        TrainingProgram trainingProgram;

        if (dayOfWeek != null) {
             trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(dayOfWeek,user);
        } else {
             trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(LocalDate.now().getDayOfWeek(), user);
        }


        TrainingProgramWrapper wrapper = new TrainingProgramWrapper();

        wrapper.setExercisePrograms(new ArrayList<>());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());
        wrapper.getExercisePrograms().add(new ExerciseProgram());

        wrapper.setCardioPrograms(new ArrayList<>());
        wrapper.getCardioPrograms().add(new CardioProgram());
        wrapper.getCardioPrograms().add(new CardioProgram());


        if (trainingProgram != null) {

            for (int i = 0; i < wrapper.getExercisePrograms().size(); i++) {

                ExerciseProgram existingExerciseProgram = exerciseProgramService.findByTrainingProgramAndPosition(trainingProgram, i);
                if (existingExerciseProgram != null) {
                    wrapper.getExercisePrograms().get(i).changeExerciseProgram(existingExerciseProgram);
                }
            }

            for (int i = 0; i < wrapper.getCardioPrograms().size(); i++) {

                CardioProgram existingCardioProgram = cardioProgramService.findByTrainingProgramAndPosition(trainingProgram, i);
                if (existingCardioProgram != null) {
                    wrapper.getCardioPrograms().get(i).changeCardioProgram(existingCardioProgram);
                }
            }
        }

        LocalDate localDate = LocalDate.now();

        model.addAttribute("user", user);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("localDate", localDate);


        return "users-profile-training-program";
    }

    @PostMapping("/users/profile/training-program")
    public String saveTrainingProgram(@ModelAttribute TrainingProgramWrapper wrapper,
                               @AuthenticationPrincipal User authUser

    ) {

        User user = userService.findById(authUser.getId());

        TrainingProgram trainingProgram;


        if (trainingProgramService.findTrainingProgramByDayOfWeekAndUser(wrapper.getDayOfWeek(), user) != null) {
            trainingProgram = trainingProgramService.findTrainingProgramByDayOfWeekAndUser(wrapper.getDayOfWeek(), user);
            trainingProgram.getExercisePrograms().clear();
            trainingProgram.getCardioPrograms().clear();
        } else {
            trainingProgram = new TrainingProgram();
            trainingProgram.setUser(user);
            trainingProgram.setDayOfWeek(wrapper.getDayOfWeek());
            trainingProgramService.saveTrainingProgram(trainingProgram);
        }


        for (int i = 0; i < wrapper.getExercisePrograms().size(); i++) {

            ExerciseProgram exerciseProgram = wrapper.getExercisePrograms().get(i);

            if (!exerciseProgram.getExerciseName().isEmpty()) {

                if (exerciseProgramService.findByTrainingProgramAndPosition(trainingProgram, i) != null) {
                    ExerciseProgram existingExerciseProgram = exerciseProgramService.findByTrainingProgramAndPosition(trainingProgram, i);
                    existingExerciseProgram.changeExerciseProgram(exerciseProgram);
                    trainingProgram.addExerciseProgramToTrainingProgram(existingExerciseProgram);
                } else {
                    exerciseProgram.setPosition(i);
                    trainingProgram.addExerciseProgramToTrainingProgram(exerciseProgram);
                }
            } else {
                exerciseProgramService.deleteByTrainingProgramAndPosition(trainingProgram, i);
            }
        }


        for (int i = 0; i < wrapper.getCardioPrograms().size(); i++) {

            CardioProgram cardioProgram = wrapper.getCardioPrograms().get(i);

            if (!cardioProgram.getCardioName().isEmpty()) {

                if (cardioProgramService.findByTrainingProgramAndPosition(trainingProgram, i) != null) {
                    CardioProgram existingCardioProgram = cardioProgramService.findByTrainingProgramAndPosition(trainingProgram, i);
                    existingCardioProgram.changeCardioProgram(cardioProgram);
                    trainingProgram.addCardioProgramToTrainingProgram(existingCardioProgram);
                }
                else {
                    cardioProgram.setPosition(i);
                    trainingProgram.addCardioProgramToTrainingProgram(cardioProgram);
                }
            } else {
                cardioProgramService.deleteByTrainingProgramAndPosition(trainingProgram, i);
            }
        }

            trainingProgram.refreshExercisePositions();
            trainingProgram.refreshCardioPositions();

           trainingProgramService.saveTrainingProgram(trainingProgram);


        return "redirect:/users/profile/training-program";



    }

    @GetMapping("/users/profile/training-stats")

    public String getTrainingStats(@AuthenticationPrincipal User user,

                                   @RequestParam(name = "inputTrainingType", required = false) String inputTrainingType,

                                   @RequestParam(name = "inputExerciseName", required = false) String inputExerciseName,
                                   @RequestParam(name = "inputExerciseParameter", required = false) String inputExerciseParameter,

                                   @RequestParam(name = "inputCardioName", required = false) String inputCardioName,
                                   @RequestParam(name = "inputCardioParameter", required = false) String inputCardioParameter,

                                   Model model) {

        LocalDate localDate = LocalDate.now();
        List<Exercise> exercises = new ArrayList<>();
        List<Cardio> cardios = new ArrayList<>();
        String parameter = null;

            for (int i = 0; i < user.getTrainings().size(); i++) {

                for (int j = 0; j < user.getTrainings().get(i).getExercises().size(); j++) {

                    boolean isEquals = false;
                    if (!exercises.isEmpty()) {
                        for (int z = 0; z < exercises.size(); z++) {
                            if (exercises.get(z).getExerciseName().equals(user.getTrainings().get(i).getExercises().get(j).getExerciseName())) {
                                isEquals = true;
                                break;
                            }
                        }
                        if (!isEquals) {
                            exercises.add(user.getTrainings().get(i).getExercises().get(j));
                        }
                    } else {
                        exercises.add(user.getTrainings().get(i).getExercises().get(j));
                    }

                }

                for (int j = 0; j < user.getTrainings().get(i).getCardios().size(); j++) {

                    boolean isEquals = false;
                    if (!cardios.isEmpty()) {
                        for (int z = 0; z < cardios.size(); z++) {
                            if (cardios.get(z).getCardioName().equals(user.getTrainings().get(i).getCardios().get(j).getCardioName())) {
                                isEquals = true;
                                break;
                            }
                        }
                        if (!isEquals) {
                            cardios.add(user.getTrainings().get(i).getCardios().get(j));
                        }
                    } else {
                        cardios.add(user.getTrainings().get(i).getCardios().get(j));
                    }
                }
            }


        List<Cardio> cardiosByName;
        List<Exercise> exercisesByName;

        if ((inputExerciseName != null && inputExerciseParameter != null) || (inputCardioName != null && inputCardioParameter != null)) {

            if (inputTrainingType.equals("EXERCISE")) {

                exercisesByName = exerciseService.findByTraining_UserAndExerciseName(user, inputExerciseName);
                parameter = inputExerciseParameter.toLowerCase();
                model.addAttribute("exercisesByName", exercisesByName);

            }
            else if (inputTrainingType.equals("CARDIO")) {

                cardiosByName = cardioService.findByTraining_UserAndCardioName(user, inputCardioName);
                parameter = inputCardioParameter.toLowerCase();
                model.addAttribute("cardiosByName", cardiosByName);

            }

            model.addAttribute("parameter", parameter);

        }

        model.addAttribute("exercises", exercises);
        model.addAttribute("cardios", cardios);
        model.addAttribute("user", user);
        model.addAttribute("localDate", localDate);


        return "users-profile-training-stats";
    }



    @GetMapping("/users/profile/edit")
    public String editUserForm(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "users-profile-edit";
    }

    @PostMapping("/users/profile/edit")
    public String updateUser(@AuthenticationPrincipal User authUser,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult result,
                             @RequestParam("editFile") MultipartFile editFile) throws IOException {

        if (result.hasErrors()) {
            return "users-profile-edit";
        }

        Details details = detailsService.findByUser(authUser);


        if (editFile != null && !editFile.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + editFile.getOriginalFilename();

            editFile.transferTo(new File(uploadPath + "/" + resultFilename));

            userDto.setFilename(resultFilename);

        } else {
            userDto.setFilename("default.jpg");
        }

        details.changeDetails(userDto.getUserDetails());

        userDto.setUserDetails(details);
        userDto.setRole(authUser.getRole());
        userDto.setTrainerId(authUser.getTrainerId());
        userDto.setCreatedOnDate(authUser.getCreatedOnDate());
        userDto.setLastVisitDate(authUser.getLastVisitDate());
        userDto.setTrainings(authUser.getTrainings());
        userDto.setTrainingPrograms(authUser.getTrainingPrograms());


        userService.updateUser(userDto);

        User updatedUser = userService.findById(authUser.getId());
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/users/profile/info";

    }

}
