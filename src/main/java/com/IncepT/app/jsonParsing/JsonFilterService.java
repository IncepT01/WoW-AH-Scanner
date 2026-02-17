package com.IncepT.app.jsonParsing;

import com.IncepT.app.configs.TsmProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JsonFilterService {
    private List<AHItem> itemsToSell = new ArrayList<>();
    private final TsmProperties tsmProperties;

    public String getServerAHID(String serverName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(new File("EU_SERVER_ID.json"));
        JsonNode items = root.get("items");

        for(JsonNode item : items){
            if(serverName.equals(item.get("name").asText())){
                JsonNode AHs = item.get("auctionHouses");
               return AHs.get(0).get("auctionHouseId").asText();
            }
        }

        return "N/A";
    }

    public void readAndFilterRegionalData() throws Exception {
        long minPriceInGold = 20_000;
        long minPriceInCopper = minPriceInGold * 100 * 100;

        long maxPriceInGold = 40_000;
        long maxPriceInCopper = maxPriceInGold * 100 * 100;

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(new File("EU_data.json"));

        for(JsonNode node : root){
            String id = node.get("itemId").asText();

            long marketValue = node.get("marketValue").asLong();
            double saleRate = node.get("saleRate").asDouble();
            double soldPerDay = node.get("soldPerDay").asDouble();

            //Find the possible items, that can be sold
            if(isBetween(marketValue, minPriceInCopper, maxPriceInCopper) && isBetween(saleRate, 0.03,0.2) && isBetween(soldPerDay, 0.10,0.8)){
                itemsToSell.add(new AHItem(id, marketValue, saleRate, soldPerDay));
            }
        }

        showBestDeals();
    }

    public void showBestDeals() throws Exception {
        System.out.println("Best deals:");
        updateItemsWithMainServerValue("Silvermoon");
        searchForLowestPrice();

        long profitInGold = 3000;
        long profitInCopper = profitInGold * 100 * 100;

        int bestDealCounter= 0;
        for(AHItem item : itemsToSell){
            if(item.getProfit() >= profitInCopper){
                bestDealCounter++;
                System.out.println(item);
            }
        }

        if (bestDealCounter == 0){
            System.out.println("There are no deals right now :(");
        }
    }

    private void updateItemsWithMainServerValue(String serverName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path folderPath = Paths.get("src/main/resources/CachedData", java.time.LocalDate.now().toString());

        Path filePath = folderPath.resolve(serverName.replace(" ", "_") + "_data.json");
        JsonNode root = mapper.readTree(filePath.toFile());

        for(AHItem item : itemsToSell){
            String id = item.getId();

            for(JsonNode node : root){
                String nodeID = node.get("itemId").asText();
                long marketValue = node.get("minBuyout").asLong();

                if(Objects.equals(nodeID, id)){
                    item.setServerPrice(marketValue);
                    item.setLowestPrice(marketValue);
                    break;
                }
            }
        }
    }

    private void searchForLowestPrice() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path folderPath = Paths.get("src/main/resources/CachedData", java.time.LocalDate.now().toString());

        int serverCounter = 0;
        for(String serverName : tsmProperties.getScannedRealms()){
            Path filePath = folderPath.resolve(serverName.replace(" ", "_") + "_data.json");
            JsonNode root = mapper.readTree(filePath.toFile());

            for(AHItem item : itemsToSell){
                String id = item.getId();

                for(JsonNode node : root){
                    if(node.has("itemId") && node.get("minBuyout").asLong() > 0){
                        String nodeID = node.get("itemId").asText();
                        long marketValue = node.get("minBuyout").asLong();

                        if(Objects.equals(nodeID, id)){
                            if(marketValue < item.getLowestPrice()){
                                item.setLowestPrice(marketValue);
                                item.setLowestServer(serverName);
                                item.setProfit(item.getServerPrice() - marketValue);
                            }
                            break;
                        }
                    }
                }
            }

            serverCounter++;
            System.out.println(serverCounter + "/" + tsmProperties.getScannedRealms().size());
        }
    }

    private boolean isBetween(long value, long min, long max){
        return value <= max && value >= min;
    }

    private boolean isBetween(double value, double min, double max){
        return value <= max && value >= min;
    }

}
