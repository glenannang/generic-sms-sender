package com.svi.sms.controllers;

import com.svi.sms.dto.request.SmsRequestDTO;
import com.svi.sms.dto.response.SmsResponseDTO;
import com.svi.sms.services.SmsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<SmsResponseDTO> sendSms(
            @Valid @RequestBody SmsRequestDTO request
    ) {
        return ResponseEntity.ok(smsService.sendSms(request));
    }
}
