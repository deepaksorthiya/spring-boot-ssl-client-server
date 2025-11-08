package com.example.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ClientServerController {
    private final RestClient restClient;
    private final Environment environment;

    public ClientServerController(RestClient restClient, Environment environment) {
        this.restClient = restClient;
        this.environment = environment;
    }

    @GetMapping("/server-call")
    public ResponseEntity<String> serverCall() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/client-call")
    public ResponseEntity<String> clientCall() {
        String serverPort = environment.getProperty("server.port");
        if (serverPort == null || serverPort.isEmpty() || serverPort.equals("0")) {
            serverPort = environment.getProperty("local.server.port");
        }
        String url = "https://localhost:" + serverPort + "/server-call";
        ResponseEntity<String> result = restClient.get().uri(url).retrieve().toEntity(String.class);
        return result;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = {"/server-info", "/api/server-info"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getRequestInfo(@RequestHeader LinkedHashMap<String, String> httpHeaders, HttpServletRequest httpServletRequest) {
        httpHeaders.put("remoteHost", httpServletRequest.getRemoteHost());
        httpHeaders.put("localAddress", httpServletRequest.getLocalAddr());
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            httpHeaders.put("hostName", localHost.getHostName());
            httpHeaders.put("hostAddress", localHost.getHostAddress());
            httpHeaders.put("canonicalHostName", localHost.getCanonicalHostName());
            httpHeaders.put("serverLocalDateTime", LocalDateTime.now().toString());
            httpHeaders.put("serverZonedDateTime", ZonedDateTime.now().toString());
            httpHeaders.put("serverOffsetDateTime", OffsetDateTime.now().toString());
        } catch (UnknownHostException e) {
            throw new InvalidEndpointRequestException(e.getMessage(), e.getMessage());
        }
        return httpHeaders;
    }
}
