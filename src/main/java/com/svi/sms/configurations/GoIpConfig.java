package com.svi.sms.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "goip")
public record GoIpConfig(
        @DefaultValue("") String ipaddress,
        @DefaultValue("") String user,
        @DefaultValue("") String pass,
        @DefaultValue("1") String line,
        @DefaultValue("30") long timeoutSeconds
) {
    public GoIpConfig {
        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("goip.timeout-seconds must be greater than zero");
        }
    }
}
