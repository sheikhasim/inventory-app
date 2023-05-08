package com.nextuple.Inventory.management.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor //@NonNull
@Document(collection = "items")
public  class Item {

    @Id @Indexed(unique = true) //I think here  i have to create one more id i.e private String id;
    private String itemId;
    private String itemName;
    private String itemDescription;
    private String category;
    private  String type;
    private  String status;
    private  String price;
    private boolean pickupAllowed;
    private boolean shippingAllowed;
    private boolean deliveryAllowed;

    public Item(String itemId, String itemName, String itemDescription, String category, String type, String status, String price, boolean pickupAllowed, boolean shippingAllowed, boolean deliveryAllowed) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.category = category;
        this.type = type;
        this.status = status;
        this.price = price;
        this.pickupAllowed = pickupAllowed;
        this.shippingAllowed = shippingAllowed;
        this.deliveryAllowed = deliveryAllowed;
    }


}




/*
        {
            "itemId": "6383728",
            "itemName":"Jeans Pant"
            "itemDescription": "Blue Jeans",
            "category": "Apparel",
            "type": "HSN001",
            "status": "00/01",
            "price": "100",
            "pickupAllowed": true,
            "shippingAllowed": true,
            "deliveryAllowed": true
        }

        API Should honour the itemId value passed in the input i.e. it's a mandatory field of the API.
        Should not be auto-generated id field.

        Table level id/primary key field should be diff one.
 */