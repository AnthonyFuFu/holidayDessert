package com.holidaydessert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "opensearch")
public class OpenSearchProperties {

    private String ip;
    private int port;
    private String username;
    private String password;
    private String indexMember;

}
