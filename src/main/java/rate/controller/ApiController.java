//package rate.controller;
//
//import org.springframework.web.server.ResponseStatusException;
//import rate.service.RateLimitService;
//import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class ApiController {
//
//    private final RateLimitService userRateLimiterService;
//
//    public ApiController(RateLimitService userRateLimiterService) {
//        this.userRateLimiterService = userRateLimiterService;
//    }
//
////    @GetMapping("/data")
////    @RateLimiter(name = "apiServiceLimiter", fallbackMethod = "apiRateLimitFallback")
////    public ResponseEntity<String> getData(@RequestHeader("X-User-Id") String userId) {
////        if (!userRateLimiterService.isAllowed(userId)) {
////            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
////                    .body("User-level rate limit exceeded for user: " + userId);
////        }
////        return ResponseEntity.ok("Success! Data for user " + userId);
////    }
//
//    @GetMapping("/data")
//    public String getData(@RequestHeader("X-User-Id") String userId) {
//        String key = "ratelimit:" + userId + ":/api/data";
//
//        boolean allowed = userRateLimiterService.isAllowed(key);
//        if (!allowed) {
//            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded for user");
//        }
//
//        return "Data for user " + userId;
//    }
//
//
//    public ResponseEntity<String> apiRateLimitFallback(String userId, Throwable t) {
//        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
//                .body("API-level rate limit exceeded");
//    }
//}