package com.nextuple.Inventory.management.model;

import ch.qos.logback.classic.Level;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
//@Getter @Setter @NoArgsConstructor
@Document(collection = "supply")
public class Supply {
    @Id
    private String id;
    private String itemId;
    private String locationId;
    private String supplyType;
    private int quantity;
    @DBRef
    private Item item;
    @DBRef
    private Location location;

    public Supply( String itemId, String locationId,String supplyType, int quantity) {
        this.itemId = itemId;
        this.locationId = locationId;
        this.supplyType = supplyType;
        this.quantity = quantity;
    }
    public enum existSupplyTypes {ONHAND, INTRANSIT, DAMAGED}

    //to String method
}

/*
{
    "itemid": "203420939",
    "locationid": "01990",
    "supplyType":"ONHAND",
    "quantity": 7
}

Id  for this table should be auto generated field.
That id should be part of the response field.

Diff Supply Types:
ONHAND
INTRANSIT
DAMAGED
*/