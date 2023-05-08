package com.nextuple.Inventory.management.service;
import com.nextuple.Inventory.management.exception.UserNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryServices {

    @Autowired
    private static ItemRepository itemRepository;
    @Autowired
    private static LocationRepository locationRepository;
    @Autowired
    private static SupplyRepository supplyRepository;
    @Autowired
    private static DemandRepository demandRepository;
    @Autowired
    private static ThresholdRepository thresholdRepository;

//    Item item1 = new Item("6383728", "Blue Jeans", "Apparel", "HSN001", "00/01", "100", true, true, true);
//    Location location1 = new Location("01504", "Big Bazar - Koramangala", "Store/DC/Vendor", true, false, true, "", "", "", "", "", "India", "585-310");

    public InventoryServices(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository) {
        InventoryServices.itemRepository = itemRepository;
        InventoryServices.locationRepository = locationRepository;
        InventoryServices.supplyRepository = supplyRepository;
        InventoryServices.demandRepository = demandRepository;
        InventoryServices.thresholdRepository = thresholdRepository;
    }

    //---------------------------------Item Service------------------------------------
    //Basic: Return all the items from item table
    public List<Item> itemDetails() {
        List<Item> itemList = itemRepository.findAll();
        if(itemList.isEmpty())
            throw new RuntimeException("There is no items are Exist in the Inventory!");
        return itemList;
    }


    //Return the details of specific itemId
    public Item findItem(String itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalItem.isEmpty())
            throw new UserNotFoundException("item with itemId:"+itemId+" not found");
        return optionalItem.get();
    }

    /* Create an item in the item table.
    New record should not be created if itemId already exists */
    public Item createItem(Item item) {
        if (validateItem(item)) return itemRepository.save(item);
        else throw new UserNotFoundException("Item with ID " + item.getItemId() + " already exists");
    }
    public boolean validateItem(Item item) {
        List<Item> dbItemsList = itemRepository.findAll();
        for (Item itemFromDb : dbItemsList) {
            if (item.getItemId().equals(itemFromDb.getItemId())) {
                return false;
            }
        }
        return true;
    }

    //Delete only if no supply or demand exists for this item
    public void deleteItemIfNotPresentInReferenceCollection(String itemId) throws RuntimeException {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            List<Supply> supplyReference = supplyRepository.findByItem(itemId);
            List<Demand> demandReference = demandRepository.findByItem(itemId);
            if (supplyReference.isEmpty() && demandReference.isEmpty()) {
                itemRepository.delete(item);
            } else {
                throw new SupplyAndDemandExistException("Item Exist with supply or demand");
            }
        } else {
            throw new UserNotFoundException("Item with itemId:"+itemId+" not found!");
        }
    }

//    Update the existing item (Handle Item Not found)
    public  Item updateItem(Item item, String itemId){
       return itemRepository.findById(itemId).map(
                newItem->{
                    newItem.setItemName(item.getItemName());
                    newItem.setItemDescription(item.getItemDescription());
                    newItem.setCategory(item.getCategory());
                    newItem.setPrice(item.getPrice());
                    newItem.setType(item.getType());
                    newItem.setStatus(item.getStatus());
                    newItem.setDeliveryAllowed(item.isDeliveryAllowed());
                    newItem.setPickupAllowed(item.isPickupAllowed());
                    newItem.setShippingAllowed(item.isShippingAllowed());
                    return itemRepository.save(newItem);
                    }
                )
                .orElseThrow(() -> new UserNotFoundException("Item with itemId:"+itemId+" Not found"));

    }

    //////////////////////////////////////////////////////-->Location API<--//////////////////////////////////////////////////////
    public List<Location>locationDetails() {
    List<Location>locationList = locationRepository.findAll();
    if(locationList.isEmpty())
        throw new RuntimeException("There is no Locations present in the Inventory details");
    return locationList;
    }

    public Location findLocation(String locationId) {
    Optional<Location> foundLocation = locationRepository.findById(locationId);
    if(foundLocation.isEmpty())
        throw new UserNotFoundException("location with locationId:"+locationId+"is not found");
    return foundLocation.get();
    }

    public Location createLocation(Location location) {
        Optional <Location>foundlocation = locationRepository.findById(location.getLocationId());
        if(foundlocation.isEmpty()){
            locationRepository.save(location);
            return location;
        }else {
            throw new RuntimeException("location with locationId:"+location.getLocationId()+" is already exist");
        }
    }

    public void deleteLocationIfNotPresentInReferenceCollection(String locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            List<Supply> supplyReference = supplyRepository.findByItem(location.getLocationId());
            List<Demand> demandReference = demandRepository.findByItem(location.getLocationId());
            if (supplyReference.isEmpty() || demandReference.isEmpty()) {
                locationRepository.delete(location);
            } else {
                throw new SupplyAndDemandExistException("Location Exist with supply or demand");
            }
        } else {
            throw new UserNotFoundException("Location with locationId:"+locationId+" not found!");
        }
    }

    public Optional<Location> updateLocation(Location location, String locationId) {
        return Optional.ofNullable(locationRepository.findById(locationId).map(
                newLocation -> {
                    newLocation.setLocationDesc(location.getLocationDesc());
                    newLocation.setLocationType(location.getLocationType());
                    newLocation.setPickupAllowed(location.isPickupAllowed());
                    newLocation.setDeliveryAllowed(location.isDeliveryAllowed());
                    newLocation.setShippingAllowed(location.isShippingAllowed());
                    newLocation.setAddressLine1(location.getAddressLine1());
                    newLocation.setAddressLine2(location.getAddressLine2());
                    newLocation.setAddressLine3(location.getAddressLine3());
                    newLocation.setCity(location.getCity());
                    newLocation.setState(location.getState());
                    newLocation.setCountry(location.getCountry());
                    newLocation.setPinCode(location.getPinCode());
                    return locationRepository.save(newLocation);
                }
        ).orElseThrow(() -> new UserNotFoundException("location with:" + locationId + " is not found")));
    }


///////////////////////////////////////////////////////-->Supply Services<--///////////////////////////////////////////////////////////

    public List<Supply> supplyDetails() {
        List<Supply>supplyList = supplyRepository.findAll();
        if(supplyList.isEmpty())
            throw new RuntimeException("Supply is Empty!");
        else
            return supplyList;
    }

    public Supply findSupply(String supplyId) {
        Optional<Supply> foundSupply = supplyRepository.findById(supplyId);
        if(foundSupply.isEmpty())
            throw new UserNotFoundException("Supply with supplyId:"+supplyId+" is not found");
        else
            return foundSupply.get();
    }

    public List<Supply> findSupplyForItem(String itemId, String locationId) {
        List<Supply> supplyList = supplyRepository.findByItemIdAndLocationId(itemId,locationId).get();
        if(supplyList.isEmpty())
            throw new UserNotFoundException("Supplies Not Found!");
        return supplyList;
    }

    public List<Supply> findSupplyWithSupplyTypeForSpecificLocation(String supplyType, String locationId) {
        List<Supply> supplyList = supplyRepository.findBySupplyTypeAndLocationId(supplyType,locationId);
        if (supplyList.isEmpty())
            throw new UserNotFoundException("Supplies Not Found!");
        return supplyList;
    }

    public Supply createSupply(Supply supply) {
        Optional<Item> item = itemRepository.findById(supply.getItemId());
        if(item.isEmpty())
            throw new UserNotFoundException("Item Not Found");
        supply.setItem(item.get());

        Optional<Location>location = locationRepository.findById(supply.getLocationId());
        if (location.isEmpty())
            throw new UserNotFoundException("location Not found");
        supply.setLocation(location.get());

        //check given supplyTypes Exist Or Not in Supply Types (Note: if user enters supply types in lowerCase convert it into upperCase)
        try {
            supply.setSupplyType(supply.getSupplyType().toUpperCase());
            Supply.existSupplyTypes.valueOf(supply.getSupplyType());

        }catch (IllegalArgumentException e){
            throw new RuntimeException("Supply Type doesn't Exist!");
        }
        supplyRepository.save(supply);
        return supply;
    }

    public String deleteSupply(String supplyId){
        Optional<Supply> supply = supplyRepository.findById(supplyId);
        if(supply.isEmpty())
            throw new UserNotFoundException("Supply Not Found!");
        supplyRepository.deleteById(supplyId);
        return "Supply deleted Successfully!"; //this string is not displaying in response body take care of it
    }

    public Supply updateSupply(String supplyId, Supply supply){
        return supplyRepository.findById(supplyId).map(
                newSupply->{
                    newSupply.setItemId(supply.getItemId());
                    newSupply.setLocationId(supply.getLocationId());
                    // redundant  code u can reduce it by making another fun for this (repeated in createSupply)
                    try {
                        supply.setSupplyType(supply.getSupplyType().toUpperCase());
                        Supply.existSupplyTypes.valueOf(supply.getSupplyType());

                    }catch (IllegalArgumentException e){
                        throw new RuntimeException("Supply Type doesn't Exist!");
                    }
                    newSupply.setSupplyType(supply.getSupplyType());
                    newSupply.setQuantity(supply.getQuantity());

                    Optional<Item> newItem = itemRepository.findById(supply.getItemId());
                    if(newItem.isEmpty())
                        throw new UserNotFoundException("Item with itemId:"+supply.getItemId()+" Not Found!");
                    newSupply.setItem(newItem.get());
                    Optional<Location> newLocation = locationRepository.findById(supply.getLocationId());
                    if (newLocation.isEmpty())
                        throw new UserNotFoundException("Location with locationId:"+supply.getLocationId()+" Not found!");
                    newSupply.setLocation(newLocation.get());
                    return supplyRepository.save(newSupply);
                }
        ).orElseThrow(()->new UserNotFoundException("Supply not Found"));
    }

    //////////////////////////////////////////////////-->Demand Services<--////////////////////////////////////////////////////////////////


    public List<Demand> demandDetails() {
        List<Demand>demandList = demandRepository.findAll();
        if(demandList.isEmpty())
            throw new RuntimeException("demand is Empty!");
        else
            return demandList;
    }

    public Demand findDemand(String demandId) {
        Optional<Demand> foundDemand = demandRepository.findById(demandId);
        if(foundDemand.isEmpty())
            throw new UserNotFoundException("Demand with demandId:"+demandId+" is not found");
        else
            return foundDemand.get();
    }

    public List<Demand> findDemandForItem(String itemId, String locationId) {
        List<Demand> demandList = demandRepository.findByItemIdAndLocationId(itemId,locationId);
        if(demandList.isEmpty())
            throw new UserNotFoundException("Demands Not Found!");
        for (Demand demand:demandList) {
            System.out.println(demand);
        }
        return demandList;
    }

    public List<Demand> findDemandWithDemandTypeForSpecificLocation(String demandType, String locationId) {
        List<Demand> DemandList = demandRepository.findByDemandTypeAndLocationId(demandType,locationId);
        if (DemandList.isEmpty())
            throw new UserNotFoundException("Demands Not Found!");
        return DemandList;
    }

    public Demand createDemand(Demand demand) {
        System.out.println(demand);
        Optional<Item> item = itemRepository.findById(demand.getItemId());
        if(item.isEmpty())
            throw new UserNotFoundException("Item Not Found");
        demand.setItem(item.get());

        Optional<Location>location = locationRepository.findById(demand.getLocationId());
        if (location.isEmpty())
            throw new UserNotFoundException("location Not found");
        demand.setLocation(location.get());

        //check given supplyTypes Exist Or Not in Supply Types (Note: if user enters supply types in lowerCase convert it into upperCase)
        try {
            demand.setDemandType(demand.getDemandType().toUpperCase());
            Demand.existDemandTypes.valueOf(demand.getDemandType());

        }catch (IllegalArgumentException e){
            throw new RuntimeException("demand Type doesn't Exist!");
        }
        demandRepository.save(demand);
        return demand;
    }

    public String deleteDemand(String demandId){
        Optional<Demand> demand = demandRepository.findById(demandId);
        if(demand.isEmpty())
            throw new UserNotFoundException("Demand Not Found!");
        demandRepository.deleteById(demandId);
        return "Demand deleted Successfully!"; //this string is not displaying in response body take care of it
    }

    public Demand updateDemand(String demandId, Demand demand){
        return demandRepository.findById(demandId).map(
                newDemand->{
                    newDemand.setItemId(demand.getItemId());
                    newDemand.setLocationId(demand.getLocationId());
                    // redundant  code u can reduce it by making another fun for this (repeated in createSupply)
                    try {
                        demand.setDemandType(demand.getDemandType().toUpperCase());
                        Supply.existSupplyTypes.valueOf(demand.getDemandType());

                    }catch (IllegalArgumentException e){
                        throw new RuntimeException("Supply Type doesn't Exist!");
                    }
                    newDemand.setDemandType(demand.getDemandType());
                    newDemand.setQuantity(demand.getQuantity());

                    Optional<Item> newItem = itemRepository.findById(demand.getItemId());
                    if(newItem.isEmpty())
                        throw new UserNotFoundException("Item with itemId:"+demand.getItemId()+" Not Found!");
                    newDemand.setItem(newItem.get());
                    Optional<Location> newLocation = locationRepository.findById(demand.getLocationId());
                    if (newLocation.isEmpty())
                        throw new UserNotFoundException("Location with locationId:"+demand.getLocationId()+" Not found!");
                    newDemand.setLocation(newLocation.get());
                    return demandRepository.save(newDemand);
                }
        ).orElseThrow(()->new UserNotFoundException("Demand not Found"));
    }

//////////////////////////////////////////////-->Availability Services<--///////////////////////////////////////////////////////////////////
    public Map<String,Object> AvailableQtyOfTheItemAtTheGivenLocation(String itemId, String locationId){
        Map<String,Object>result = new HashMap<>();

        int totalSupply = 0;
        List<Supply> supplyList = findSupplyForItem(itemId, locationId);
        if(supplyList.isEmpty())
            throw new RuntimeException("Supply not found for this Item At This Location");
        for (Supply supply:supplyList) {
            totalSupply+=supply.getQuantity();
            System.out.println(supply);
        }
        int totalDemand = 0;
        List<Demand> demandList = findDemandForItem(itemId, locationId);
        if(demandList!=null) {
            for (Demand demand : demandList) {
                totalDemand += demand.getQuantity();
                System.out.println(demand);
            }
        }
        result.put("itemId",itemId);
        result.put("locationId",locationId);
        result.put("availableQty",totalSupply-totalDemand);
        return result;

    }


    public Map<String,Object> AvailableQtyOfTheItemAtAllTheLocation(String itemId){
        Map<String,Object>result = new HashMap<>();

        int totalSupply = 0;

        List<Supply> supplyList = supplyRepository.findByItem(itemId);
        if(supplyList.isEmpty())
            throw new RuntimeException("Supply not found for this Item At This Location");
        for (Supply supply:supplyList) {
            totalSupply+=supply.getQuantity();
            System.out.println(supply);
        }
        int totalDemand = 0;

            List<Demand> demandList = demandRepository.findByItem(itemId);
            if(!demandList.isEmpty()) {
                for (Demand demand : demandList) {
                    totalDemand += demand.getQuantity();
                    System.out.println(demand);
                }
        }

        result.put("itemId",itemId);
        result.put("locationId","NETWORK");
        result.put("availableQty",totalSupply-totalDemand);
        return result;

    }

    public List<Threshold> thresholdDetails() {
        List<Threshold>thresholdList  = thresholdRepository.findAll();
        if(thresholdList.isEmpty())
            throw  new RuntimeException("Thresholds Are not available for any item!");
        return thresholdList;
    }

    public Threshold findThresholdById(String thresholdId) {
        Optional<Threshold> threshold = thresholdRepository.findById(thresholdId);
        if(threshold.isEmpty())
            throw new UserNotFoundException("Threshold not found!");
        return threshold.get();
    }


    public Threshold findThresholdByItemIdAtLocation(String itemId, String locationId) {
        Optional<Threshold>threshold = thresholdRepository.findByItemIdAndLocationId(itemId,locationId);
        if (threshold.isEmpty())
            throw new RuntimeException("threshold not found!");
        return threshold.get();
    }

    public Threshold createThreshold(Threshold threshold){
        Optional<Threshold>thresholdExist = thresholdRepository.findByItemIdAndLocationId(threshold.getItemId(),threshold.getLocationId());
        if(thresholdExist.isPresent())
            throw new RuntimeException("Threshold with itemId:"+threshold.getItemId()+" & locationId:"+threshold.getLocationId()+" is Exist!");
        else
            return thresholdRepository.save(threshold);
    }

    public void deleteThreshold(String thresholdId) {
        Optional<Threshold> threshold = thresholdRepository.findById(thresholdId);
        if (thresholdId.isEmpty())
            throw new RuntimeException("Threshold Not Found!");
        thresholdRepository.deleteById(thresholdId);
    }

    public Threshold updateThreshold(String thresholdId, Threshold threshold) {
        return thresholdRepository.findById(thresholdId).map(
                newThreshold->{
                    newThreshold.setItemId(threshold.getItemId());
                    newThreshold.setLocationId(threshold.getLocationId());
                    newThreshold.setMinThreshold(threshold.getMinThreshold());
                    newThreshold.setMaxThreshold(threshold.getMaxThreshold());
                return thresholdRepository.save(newThreshold);
        }).orElseThrow(()-> new RuntimeException("Threshold not found!"));
    }

    public Threshold updateThresholdByItemIdAndLocationId(String itemId, String locationId, Threshold threshold) {
        return thresholdRepository.findByItemIdAndLocationId(itemId,locationId).map(
                newThreshold->{
                    newThreshold.setMinThreshold(threshold.getMinThreshold());
                    newThreshold.setMaxThreshold(threshold.getMaxThreshold());
                    return thresholdRepository.save(newThreshold);
                }).orElseThrow(()-> new RuntimeException("Threshold not found!"));
    }
}


