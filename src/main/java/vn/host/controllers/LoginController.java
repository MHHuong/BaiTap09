package vn.host.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.host.entity.User;
import vn.host.repository.UserRepo;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserRepo userRepo;

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpServletRequest req,
                          Model model) {
        User u = userRepo.findByEmail(email).orElse(null);
        if (u == null || !u.getPassword().equals(password)) {
            return "redirect:/login?error";
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("LOGIN_USER_ID", u.getId());
        session.setAttribute("LOGIN_ROLE", u.getRole().name()); // "USER" | "ADMIN"

        // Redirect theo role
        if (u.getRole() == User.Role.ADMIN) {
            return "redirect:/admin/home";
        } else {
            return "redirect:/user/home";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/user/home")
    public String userHome() { return "user-home"; }

    @GetMapping("/admin/home")
    public String adminHome() { return "admin"; } // tận dụng admin.html có sẵn
}