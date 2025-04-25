package com.example.web;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

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
}
