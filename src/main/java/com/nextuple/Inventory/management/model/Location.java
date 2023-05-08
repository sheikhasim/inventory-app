package com.nextuple.Inventory.management.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter @Setter @NoArgsConstructor
@Document(collection = "locations")
public class Location {
    @Id @Indexed(unique = true)
    private String locationId;
    private String locationDesc;
    private String locationType;
    private boolean  pickupAllowed;
    private boolean shippingAllowed;
    private boolean deliveryAllowed;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    public Location(String locationId, String locationDesc, String locationType, boolean pickupAllowed, boolean shippingAllowed, boolean deliveryAllowed, String addressLine1, String addressLine2,String addressLine3, String city, String state, String country, String pinCode) {
        this.locationId = locationId;
        this.locationDesc = locationDesc;
        this.locationType = locationType;
        this.pickupAllowed = pickupAllowed;
        this.shippingAllowed = shippingAllowed;
        this.deliveryAllowed = deliveryAllowed;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3=addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
    }


    @Override
    public String toString() {
        return "Location{" +
                "locationId='"+locationId+'\''+
                "locationDesc='" + locationDesc + '\'' +
                ", locationType='" + locationType + '\'' +
                ", pickupAllowed=" + pickupAllowed +
                ", shippingAllowed=" + shippingAllowed +
                ", deliveryAllowed=" + deliveryAllowed +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pinCode='" + pinCode + '\'' +
                '}';
    }
}

/*
{
  "locationId": "01504",
  "locationDesc": "Big Bazar - Koramangala",
  "locationType": "Store/DC/Vendor",
  "pickupAllowed":true,
  "shippingAllowed":false,
  "deliveryAllowed":true,
  "addressLine1":"",
  "addressLine2":"",
  "addressLine3":"",
  "city":"",
  "state":"",
  "country":"",
  "pincode":""
}

API Should honour the itemId value passed in the input i.e. it's a mandatory field of the API.
Should not be auto-generated id field.

Table level id/primary key field should be diff one.

*/
