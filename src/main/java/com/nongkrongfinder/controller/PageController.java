package com.nongkrongfinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "forward:/dashboard.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/register.html";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/dashboard.html";
    }

    @GetMapping("/tempat")
    public String tempat() {
        return "forward:/tempat.html";
    }

    @GetMapping("/tempat-form")
    public String tempatForm() {
        return "forward:/tempat-form.html";
    }

    @GetMapping("/tempat-detail")
    public String tempatDetail() {
        return "forward:/tempat-detail.html";
    }

    @GetMapping("/review")
    public String review() {
        return "forward:/review.html";
    }

    @GetMapping("/favorit")
    public String favorit() {
        return "forward:/favorit.html";
    }

    @GetMapping("/event")
    public String event() {
        return "forward:/event.html";
    }

    @GetMapping("/event-form")
    public String eventForm() {
        return "forward:/event-form.html";
    }

    @GetMapping("/event-peserta")
    public String eventPeserta() {
        return "forward:/event-peserta.html";
    }

    @GetMapping("/profil")
    public String profil() {
        return "forward:/profil.html";
    }

    @GetMapping("/admin")
    public String adminHome() {
        return "forward:/admin/dashboard.html";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "forward:/admin/dashboard.html";
    }

    @GetMapping("/admin/users")
    public String adminUsers() {
        return "forward:/admin/users.html";
    }

    @GetMapping("/admin/tempat")
    public String adminTempat() {
        return "forward:/admin/tempat.html";
    }

    @GetMapping("/admin/review")
    public String adminReview() {
        return "forward:/admin/review.html";
    }

    @GetMapping("/admin/event")
    public String adminEvent() {
        return "forward:/admin/event.html";
    }
}
