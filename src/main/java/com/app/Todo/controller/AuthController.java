package com.app.Todo.controller;

import com.app.Todo.models.User;
import com.app.Todo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Handle the Home Page URL (/)
    @GetMapping("/")
    public String homePage() {
        // If user visits localhost:8080, send them to /tasks.
        // Security will check if they are logged in. If not, it redirects to /login automatically.
        return "redirect:/tasks";
    }

    // 2. Handle the Login Page URL (/login)
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // Simply returns the login.html view
        return "login";
    }

    // 3. Handle Registration (/register)
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        if (userRepo.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal User user, Model model){
        if(user == null){
            model.addAttribute("user",new User());
        }else{
            model.addAttribute("user",user);
        }
        return "profile";
    }
}