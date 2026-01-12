package com.example.dataapi.Controller;


import com.example.dataapi.Dto.TransformRequest;
import com.example.dataapi.Dto.TransformResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransformController {

    @Value("${internal.token}")
    private String internalToken;

    @PostMapping("/transform")
    public ResponseEntity<?> transform(
            @RequestHeader(value = "X-Internal-Token", required = false) String token,
            @RequestBody TransformRequest request) {

        // 1. ПЕРЕВІРКА БЕЗПЕКИ
        // Якщо токена немає або він не співпадає з нашим секретом -> 403 Forbidden
        if (token == null || !token.equals(internalToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Invalid or missing internal token");
        }

        // 2. БІЗНЕС-ЛОГІКА (Трансформація)
        if (request.text() == null) {
            return ResponseEntity.badRequest().body("Text is required");
        }

        // Приклад: Робимо реверс тексту і переводимо у верхній регістр
        // "hello" -> "OLLEH"
        String transformed = new StringBuilder(request.text())
                .reverse()
                .toString()
                .toUpperCase();

        return ResponseEntity.ok(new TransformResponse(transformed));
    }
}