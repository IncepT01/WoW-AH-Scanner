package com.IncepT.app.fetching;

import com.IncepT.app.configs.TsmProperties;
import com.IncepT.app.jsonParsing.JsonFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FetchService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final JsonFilterService jsonFilterService;
    private final TsmProperties tsmProperties;

    public ArrayList<String> serverList = new ArrayList<>();

    public void fetchAllAH(String accessToken) throws IOException, InterruptedException {
        int AHcounter = 0;
        for(String serverName : tsmProperties.getScannedRealms()){
            String AHID = jsonFilterService.getServerAHID(serverName);
            Path folderPath = Paths.get("src/main/resources/CachedData", java.time.LocalDate.now().toString());
            Files.createDirectories(folderPath);

            System.out.println(AHID);

            String url = "https://pricing-api.tradeskillmaster.com/ah/" + AHID;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("User-Agent", "Java HttpClient")
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                AHcounter++;
                Path filePath = folderPath.resolve(serverName.replace(" ", "_") + "_data.json");
                Files.writeString(filePath, response.body(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
            else {
                System.out.println("something went wrong! Status code: " + response.statusCode());
            }
        }

        System.out.println("AH fetching done. AHs fetched: " + AHcounter);
    }

    public String fetchAllItemsFromSingleAH(String url, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("User-Agent", "Java HttpClient") // simulate a browser
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        saveToJson(response, "Silvermoon_data.json");
        return response.body();
    }

    public String fetchAllItemsInEU(String url, String accessToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("User-Agent", "Java HttpClient") // simulate a browser
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        saveToJson(response, "EU_data.json");
        return response.body();
    }

    private void saveToJson(HttpResponse<String> response, String fileName){
        try {
            Path path = Path.of(fileName);

            Files.writeString(path, response.body(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save JSON", e);
        }
    }


    private void getServerAHID(String serverName){

    }

}
