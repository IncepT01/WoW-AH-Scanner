package com.IncepT.app.tokenGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AccessTokenGeneratorService {

    private final String apiToken = "Pik54bgLHwcMNGRtoALR_0FmD0qgE3aK";
    private final String url = "https://auth.tradeskillmaster.com/oauth2/token";

    private final HttpClient client = HttpClient.newHttpClient();

    public String generateAccessToken() throws IOException, InterruptedException {
        String formData = "client_id=c260f00d-1071-409a-992f-dda2e5498536"
                + "&grant_type=api_token"
                + "&scope=app:realm-api%20app:pricing-api"
                + "&token=" + apiToken;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Java HttpClient")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return extractAccessToken(response.body());
    }

    public String extractAccessToken(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonResponse);
        return node.get("access_token").asText();
    }
}
