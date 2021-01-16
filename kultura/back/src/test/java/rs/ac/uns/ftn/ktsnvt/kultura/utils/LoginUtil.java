package rs.ac.uns.ftn.ktsnvt.kultura.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.auth.LoginDto;

public class LoginUtil {

    public static String login(TestRestTemplate restTemplate, String email, String password) {
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/auth/login",
                        new LoginDto(email, password),
                        String.class);
        JsonNode parent;
        String body = responseEntity.getBody();
        if(body==null){
            System.out.println("Can't log in");
        }
        try {
            parent = new ObjectMapper().readTree(body);
            return parent.path("token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
