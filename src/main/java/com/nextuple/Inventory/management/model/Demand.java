package com.nextuple.Inventory.management.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "demand")
public class Demand {
    @Id
    private  String id;
    private String itemId;
    private String locationId;
    private String demandType;
    private int quantity;
    @DBRef
    private Item item;
    @DBRef
    private Location location;

    public Demand( String demandType, int quantity, String itemId, String locationId) {
        this.demandType = demandType;
        this.quantity = quantity;
        this.itemId = itemId;
        this.locationId = locationId;
    }
    public enum existDemandTypes{HARDPROMISED, PLANNED}
}

/*
{
    "demandType":"HARDPROMISED",
    "quantity":30,
    "itemId":"6383728",
    "locationId":"01504"
}

Id  for this table should be auto generated field.
That id should be part of the response field.
*/