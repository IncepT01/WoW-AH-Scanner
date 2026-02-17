package com.IncepT.app.controller;

import com.IncepT.app.tokenGenerator.AccessTokenGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AccessTokenGeneratorController {

    private final AccessTokenGeneratorService accessTokenGeneratorService;

    @GetMapping("/GAT")
    public String generateToken() throws IOException, InterruptedException {
        return accessTokenGeneratorService.generateAccessToken();
    }

}
