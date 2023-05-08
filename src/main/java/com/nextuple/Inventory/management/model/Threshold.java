package com.nextuple.Inventory.management.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "threshold")
public class Threshold {
   @Id
   private String id;
   private String itemId;
   private String locationId;
   private int minThreshold;
   private int maxThreshold;

    public Threshold(String itemId, String locationId, int minThreshold, int maxThreshold) {
        this.itemId = itemId;
        this.locationId = locationId;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }
}

/*
{
    "itemId": "123",
    "locationId": "4566",
    "minThreshold": 10,
    "maxThreshold": 100,
}

Id  for this table should be auto generated field.
That id should be part of the response field.
*/