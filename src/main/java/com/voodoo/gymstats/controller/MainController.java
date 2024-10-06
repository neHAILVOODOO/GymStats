package com.voodoo.gymstats.controller;

import com.voodoo.gymstats.dto.UserDto;
import com.voodoo.gymstats.model.Details;
import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.model.UserRole;
import com.voodoo.gymstats.service.DetailsService;
import com.voodoo.gymstats.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private UserService userService;
    private DetailsService detailsService;


    @Autowired
    public MainController (UserService userService, DetailsService detailsService) {
        this.userService = userService; this.detailsService = detailsService;
    }

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping("/users")
    public String listUsers(Model model) {

        List<UserDto> users = userService.findAllUsers();

        model.addAttribute("users", users);


        return "main-users-list";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_FOUNDER')")
    @GetMapping ("/users/new")
    public String createUserForm(Model model, HttpSession session) {
        User user = new User();

        List<User> trainers = userService.findUsersByRole(UserRole.TRAINER);

        session.setAttribute("trainers", trainers);
        model.addAttribute("user", user);
        model.addAttribute("trainers", trainers);

        return "admin-create-users";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_FOUNDER')")
    @PostMapping("/users/new")
    public String saveUser(@Valid @ModelAttribute ("user") UserDto userDto,
                           BindingResult bindingResult,
                           Model model,
                           HttpSession session,
                           @RequestParam("file") MultipartFile file) throws IOException

    {

        List<User> trainers = (List<User>) session.getAttribute("trainers");

        if (userService.existsUserByLogin(userDto.getLogin())) {
            bindingResult.rejectValue("login", "error.user.exists", "Пользователь с таким логином уже существует");
            model.addAttribute("trainers", trainers);
            return "admin-create-users";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            model.addAttribute("trainers", trainers);
            return "admin-create-users";
        }


        userDto.setActive(String.valueOf(true));
        userDto.setCreatedOnDate(LocalDate.now());
        userDto.setFilename(file.getOriginalFilename());


        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            userDto.setFilename(resultFilename);
        } else {
            userDto.setFilename("default.jpg");
        }


        userService.saveUser(userDto);
        session.removeAttribute("trainers");
        return "redirect:/users";
    }

    @GetMapping("/users/{userId}")
    public String userDetail(@PathVariable("userId") long userId, @AuthenticationPrincipal User authUser, Model model) {

        User user = userService.findById(userId);
        List<User> trainerStudents = userService.findStudentsByTrainerId(userId);

        if (authUser.getLogin().equals(user.getLogin())) {
            return "redirect:/users/profile/info";
        }

        if (!trainerStudents.isEmpty()) {
            model.addAttribute("trainerStudents", trainerStudents);
        }
        model.addAttribute("user", user);


        return "main-users-detail";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_FOUNDER')")
    @GetMapping("/admin/users")
    public String adminListUsers(Model model) {

        List<UserDto> users = userService.findAllUsers();

        model.addAttribute("users", users);


        return "admin-users-list";
    }

    @GetMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable("userId") Long userId) {

        userService.delete(userId);

        return "redirect:/users";
    }

    @GetMapping("/users/search")
    public String searchUser(@RequestParam(value = "query") String query, Model model) {

        List<UserDto> users = userService.searchUsers(query);

        model.addAttribute("users", users);

        return "main-users-list";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_FOUNDER')")
    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable("userId") Long userId, Model model) {

        UserDto user = userService.findUserById(userId);
        List<User> trainers = userService.findUsersByRole(UserRole.TRAINER);

        model.addAttribute("user", user);
        model.addAttribute("trainers", trainers);

        return "admin-users-edit";


    }



    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_FOUNDER')")
    @PostMapping("/users/{userId}/edit")
    public String updateUser(@PathVariable("userId") Long userId,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult result,
                             @RequestParam("editFile") MultipartFile editFile) throws IOException {

        if (result.hasErrors()) {
            return "admin-users-edit";
        }


        Details details = detailsService.findByUser(userService.findById(userId));

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

        userDto.setUserDetails(details);
        userDto.setId(userId);
        userService.updateUser(userDto);

        return "redirect:/users";
    }


    @GetMapping("/")
    public String mainPage() {

        return "main-site";

    }

    @GetMapping("/tickets")
    public String ticketsPage() {

        return "main-tickets";

    }

    @GetMapping("/contacts")
    public String contactsPage() {

        return "main-contacts";

    }

}
