package com.svi.sms.services.impl;

import com.svi.sms.configurations.GoIpConfig;
import com.svi.sms.services.GoIpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Service
public class GoIpServiceImpl implements GoIpService {

    private static final Logger log = LoggerFactory.getLogger(GoIpServiceImpl.class);

    private final GoIpConfig config;
    private final HttpClient client;

    @Autowired
    public GoIpServiceImpl(GoIpConfig config) {
        this(config, HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(config.timeoutSeconds())).build());
    }

    GoIpServiceImpl(GoIpConfig config, HttpClient client) {
        this.config = config;
        this.client = client;
    }

    @Override
    public boolean sendMessage(String telnum, String smscontent) {
        if (isBlank(config.ipaddress()) || isBlank(config.user()) || isBlank(config.pass())
                || isBlank(config.line())) {
            log.warn("GoIP is not configured; set goip.ipaddress, goip.user, goip.pass and goip.line.");
            return false;
        }
        if (isBlank(telnum) || isBlank(smscontent)) {
            log.warn("GoIP requires a recipient and message.");
            return false;
        }

        long startedAt = System.nanoTime();
        try {
            String url = "http://" + config.ipaddress() + "/default/en_US/sms_info.html";
            String smskey = String.valueOf(System.currentTimeMillis());
            log.info("GoIP preparing SMS web form request (endpoint: {}, line: {}, smskey: {}, message length: {}, timeout: {}s).",
                    url, config.line(), smskey, smscontent.length(), config.timeoutSeconds());
            String form = "line=" + encode(config.line())
                    + "&smskey=" + encode(smskey)
                    + "&action=sms"
                    + "&telnum=" + encode(telnum)
                    + "&smscontent=" + encode(smscontent)
                    + "&send=send";
            String auth = config.user() + ":" + config.pass();
            String basicAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(config.timeoutSeconds()))
                    .header("Authorization", "Basic " + basicAuth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();
            log.info("GoIP sending SMS web form request (method: POST, line: {}, smskey: {}).",
                    config.line(), smskey);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // The web form may return HTML containing the recipient and OTP.
            boolean successful = response.statusCode() == 200;
            log.info("GoIP SMS web form responded (smskey: {}, HTTP: {}, successful: {}, elapsed: {}ms, response length: {}).",
                    smskey, response.statusCode(), successful,
                    Duration.ofNanos(System.nanoTime() - startedAt).toMillis(),
                    response.body() == null ? 0 : response.body().length());
            if (!successful) {
                log.warn("GoIP SMS web form request failed (smskey: {}, HTTP: {}).", smskey, response.statusCode());
            }
            return successful;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("GoIP web form request interrupted.");
            return false;
        } catch (IOException | IllegalArgumentException e) {
            log.warn("GoIP web form request failed ({}).", e.getClass().getSimpleName());
            return false;
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
