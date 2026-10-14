package com.svi.sms.services.impl;

import com.svi.sms.dto.request.SmsRequestDTO;
import com.svi.sms.dto.response.SmsResponseDTO;
import com.svi.sms.exceptions.SmsSendingException;
import com.svi.sms.services.GoIpService;
import com.svi.sms.services.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private final GoIpService goIpService;

    public SmsServiceImpl(GoIpService goIpService) {
        this.goIpService = goIpService;
    }

    @Override
    public SmsResponseDTO sendSms(SmsRequestDTO request) {
        boolean accepted = goIpService.sendMessage(
                request.telnum(),
                request.smscontent()
        );

        if (!accepted) {
            throw new SmsSendingException("SMS request was not accepted by GOIP8");
        }

        return new SmsResponseDTO(true, "SMS request accepted by GOIP8");
    }
}
