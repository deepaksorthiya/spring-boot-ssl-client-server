package com.example;

import com.example.config.TestRestTemplateConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRestTemplateConfig.class)
class SpringBootSslServerApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AbstractConfigurableWebServerFactory webServerFactory;

    @Test
    void test_ssl() {
        assertThat(this.webServerFactory.getSsl().isEnabled()).isTrue();
    }

    @Test
    void test_server_ssl() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/server-call", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("Hello");
    }

    @Test
    void test_client_ssl() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/client-call", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo("Hello");
    }

    /**
     * Info endpoint still return valid output if SSL Certificate expired, check below health endpoint
     */
    @Test
    void test_ssl_info() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/actuator/info", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonContent body = new JsonContent(entity.getBody());
        assertThat(body).extractingPath("ssl.bundles[1].name").isEqualTo("server");
        assertThat(body).extractingPath("ssl.bundles[1].certificateChains[0].alias")
                .isEqualTo("springbootssl");
        assertThat(body).extractingPath("ssl.bundles[1].certificateChains[0].certificates[0].issuer")
                .isEqualTo("CN=localhost,OU=springbootssl,O=springbootssl,L=AHM,ST=GUJ,C=IN");
        assertThat(body).extractingPath("ssl.bundles[1].certificateChains[0].certificates[0].subject")
                .isEqualTo("CN=localhost,OU=springbootssl,O=springbootssl,L=AHM,ST=GUJ,C=IN");
        assertThat(body).extractingPath("ssl.bundles[1].certificateChains[0].certificates[0].validity.status")
                .isEqualTo("VALID");
    }

    /**
     * Test this using expired ssl certificate
     * Health endpoint not return valid output if SSL Certificate expired
     */
    @Test
    @Disabled
    void test_ssl_health() {
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/actuator/health", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        JsonContent body = new JsonContent(entity.getBody());
        assertThat(body).extractingPath("status").isEqualTo("OUT_OF_SERVICE");
        assertThat(body).extractingPath("components.ssl.status").isEqualTo("OUT_OF_SERVICE");
        assertThat(body).extractingPath("components.ssl.details.invalidChains[0].alias")
                .isEqualTo("springbootssl");
        assertThat(body).extractingPath("components.ssl.details.invalidChains[0].certificates[0].issuer")
                .isEqualTo("CN=localhost,OU=springbootssl,O=springbootssl,L=AHM,ST=GUJ,C=IN");
        assertThat(body).extractingPath("components.ssl.details.invalidChains[0].certificates[0].subject")
                .isEqualTo("CN=localhost,OU=springbootssl,O=springbootssl,L=AHM,ST=GUJ,C=IN");
        assertThat(body).extractingPath("components.ssl.details.invalidChains[0].certificates[0].validity.status")
                .isEqualTo("EXPIRED");
        assertThat(body).extractingPath("components.ssl.details.invalidChains[0].certificates[0].validity.message")
                .asString()
                .startsWith("Not valid after ");
    }

}
