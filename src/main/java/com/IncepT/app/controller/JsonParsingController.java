package com.IncepT.app.controller;

import com.IncepT.app.jsonParsing.JsonFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JsonParsingController {

    private final JsonFilterService jsonFilterService;

    @GetMapping("/top")
    public void getTopSales() throws Exception {
        jsonFilterService.readAndFilterRegionalData();
    }
}
