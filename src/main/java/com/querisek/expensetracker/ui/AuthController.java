package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.infrastructure.auth.User;
import com.querisek.expensetracker.infrastructure.auth.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String userRegistration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if(userService.userExistsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.email", "Konto z tym adresem email juz istnieje.");
        }
        if(result.hasErrors()) {
            return "registration";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String userLogin() {
        return "login";
    }
}
