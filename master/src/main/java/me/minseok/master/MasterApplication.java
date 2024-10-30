package me.minseok.master;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@SpringBootApplication
public class MasterApplication {

    RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "queue", defaultValue = "default") String queue,
            @RequestParam(name = "member_id") Long memberId,
            HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieName = "member-queue-%s-token".formatted(queue);

        String token = "";
        if (cookies != null) {
            Optional<Cookie> cookie = Arrays.stream(cookies).filter(i -> i.getName().equalsIgnoreCase(cookieName)).findFirst();
            token = cookie.orElse(new Cookie(cookieName, "")).getValue();
        }

        URI uri = UriComponentsBuilder
                .fromUriString("http://127.0.0.1:9010")
                .path("/api/v1/queue/allowed")
                .queryParam("queue", queue)
                .queryParam("member_id", memberId)
                .queryParam("token", token)
                .encode()
                .build()
                .toUri();

        ResponseEntity<AllowedMemberResponse> response = restTemplate.getForEntity(uri, AllowedMemberResponse.class);
        if (response.getBody() == null || !response.getBody().allowed()) {
            return "redirect:http://127.0.0.1:9010/waiting-room?member_id=%d&redirect_url=%s".formatted(
                    memberId, "http://127.0.0.1:9000?member_id=%d".formatted(memberId)
            );
        }

        return "index";
    }

    public record AllowedMemberResponse(Boolean allowed) {
    }

}
