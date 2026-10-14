package com.svi.sms.services;

public interface GoIpService {

    /**
     * Sends using the gateway's SMS web form with Basic Auth. A 200 response
     * indicates HTTP success; it does not confirm delivery to the recipient.
     */
    boolean sendMessage(String telnum, String smscontent);
}
