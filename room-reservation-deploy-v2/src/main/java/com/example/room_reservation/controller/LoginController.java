package com.example.room_reservation.controller;

import com.example.room_reservation.model.Role;
import com.example.room_reservation.model.UserBase;
import com.example.room_reservation.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // Menampilkan halaman login
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {

        UserBase user = authService.login(email, password);

        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());  // Simpan role di session

            // Bandingkan role dengan enum, bukan string
            if (Role.ADMIN.equals(user.getRole())) {
                return "redirect:/admin/dashboard";  // Jika admin
            } else if (Role.CLIENT.equals(user.getRole())) {
                return "redirect:/client/dashboard";  // Jika client
            } else {
                model.addAttribute("error", "Role tidak dikenali.");
                return "login";  // Jika role tidak sesuai
            }
        } else {
            model.addAttribute("error", "Email atau password salah.");
            return "login";  // Jika login gagal
        }
    }
}
