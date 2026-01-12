package com.example.authapi.Controller;


import com.example.authapi.DTO.ProcessRequest;
import com.example.authapi.DTO.ProcessResponse;
import com.example.authapi.Entity.ProcessingLog;
import com.example.authapi.Entity.UserEntity;
import com.example.authapi.Repository.LogRepository;
import com.example.authapi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class ProcessController {
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${internal.token}")

    private String internalToken;

    @Value("${service.b.url}")

    private String serviceBUrl;

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestBody ProcessRequest req, @AuthenticationPrincipal String email) {

        UserEntity user = userRepository.findByEmail(email);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalToken);
        HttpEntity<ProcessRequest> entity = new HttpEntity<>(req, headers);

        try {
            ResponseEntity<ProcessResponse> response = restTemplate.exchange(
                    serviceBUrl, HttpMethod.POST, entity, ProcessResponse.class);

            String result = response.getBody().result();

            logRepository.save(ProcessingLog.builder()
                    .userId(user.getId())
                    .inputText(req.text())
                    .outputText(result)
                    .createdAt(LocalDateTime.now())
                    .build());

            return ResponseEntity.ok(new ProcessResponse(result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling Service B: " + e.getMessage());
        }
    }
}