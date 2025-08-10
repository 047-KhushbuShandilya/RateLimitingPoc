package rate.config;


import rate.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;

    public RateLimitInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String apiPath = request.getRequestURI();
        String userId = request.getHeader("X-User-Id");

        // API-level check
        if (!rateLimitService.isAllowedForApi(apiPath)) {
            response.setStatus(429);
            response.getWriter().write("API rate limit exceeded. Try again later.");
            return false;
        }

        // User-level check (if userId present)
        if (userId != null && !userId.isBlank()) {
            if (!rateLimitService.isAllowedForUser(userId, apiPath)) {
                response.setStatus(429);
                response.getWriter().write("User-specific rate limit exceeded. Try again later.");
                return false;
            }
        }

        return true;
    }
}

