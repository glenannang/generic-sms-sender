package com.svi.sms.services;

import com.svi.sms.dto.request.SmsRequestDTO;
import com.svi.sms.dto.response.SmsResponseDTO;

public interface SmsService {

    SmsResponseDTO sendSms(SmsRequestDTO request);
}
