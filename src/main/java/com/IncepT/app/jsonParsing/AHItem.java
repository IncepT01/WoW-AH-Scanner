package com.IncepT.app.jsonParsing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class AHItem {
    private final String id;
    private final long regionalPrice;
    private long serverPrice;
    private long profit;
    private long lowestPrice;
    private String lowestServer;
    private final double saleRate;
    private final double soldPerDay;


    @Override
    public String toString(){
        return "Item id: " + id  + " main server price: "+ serverPrice/(100*100) + "g profit: "+ profit/(100*100) + "g lowest on: " + lowestServer + " with " + lowestPrice/(100*100) + "g soldPerDay: " + soldPerDay;
    }
}
