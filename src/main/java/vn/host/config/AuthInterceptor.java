package vn.host.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    private final String requiredRole; // "USER" hoặc "ADMIN" hoặc null (chỉ cần đăng nhập)

    public AuthInterceptor(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler)
            throws Exception {
        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("LOGIN_ROLE") : null;
        // chưa đăng nhập
        if (role == null) {
            resp.sendRedirect("/login?error");
            return false;
        }
        // có yêu cầu role cụ thể => kiểm tra
        if (requiredRole != null && !requiredRole.equals(role)) {
            // sai quyền
            resp.sendRedirect("/login?error");
            return false;
        }
        return true; // hợp lệ
    }
}