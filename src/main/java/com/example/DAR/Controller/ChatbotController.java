package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.DTO.In.ChatbotDTOIn;
import com.example.DAR.Service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @GetMapping("/questions")
    public ResponseEntity<?> getSuggestedQuestions() {
        return ResponseEntity.status(200).body(chatbotService.getSuggestedQuestions());
    }

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody @Valid ChatbotDTOIn dto) {
        return ResponseEntity.status(200).body(new ApiResponse(chatbotService.ask(dto)));
    }
}
