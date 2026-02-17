package com.IncepT.app.controller;

import com.IncepT.app.fetching.FetchService;
import com.IncepT.app.tokenGenerator.AccessTokenGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FetchController {
    private final FetchService fetchService;
    private final AccessTokenGeneratorService accessTokenGeneratorService;

    @GetMapping("/fetchAH")
    public String fetchAllItemsFromSingleAH() throws Exception {
        String accessToken = accessTokenGeneratorService.generateAccessToken();

        int ah_id = 160; // EU - Silver moon
        String url = "https://pricing-api.tradeskillmaster.com/ah/" + ah_id;

        return fetchService.fetchAllItemsFromSingleAH(url, accessToken);
    }

    @GetMapping("/fetchEU")
    public String fetchAllItemsInEU() throws Exception {
        String accessToken = accessTokenGeneratorService.generateAccessToken();

        int region_id = 2; // EU
        String url = "https://pricing-api.tradeskillmaster.com/region/" + region_id;

        return fetchService.fetchAllItemsInEU(url, accessToken);
    }

    @GetMapping("/fetchAll")
    public void fetchAll() throws IOException, InterruptedException {
        String accessToken = accessTokenGeneratorService.generateAccessToken();
        fetchService.fetchAllAH(accessToken);
    }
}
