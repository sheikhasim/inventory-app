package com.nextuple.Inventory.management.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.service.InventoryServices;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

@RestController
public class ItemController {
    @Autowired
    private InventoryServices inventoryServices;
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
//     we cn write this logger in any method

    ////////////////////////////////////////////////--item API--///////////////////////////////////////////////
    @GetMapping("/items")
    public ResponseEntity<List<Item>> itemDetails(){
        logger.info("i am inside the item now");
        return new ResponseEntity<>(inventoryServices.itemDetails(),HttpStatus.OK);
    }
    @GetMapping("/items/{id}")
    public ResponseEntity<Item>findItem(@PathVariable("id") String itemId){
        return ResponseEntity.ok(inventoryServices.findItem(itemId));
    }
    @PostMapping("/items")
    public ResponseEntity<Item>createItem(@RequestBody Item item)
    {
           Item itemCreated = inventoryServices.createItem(item);

           URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                       .path("/{id}")
                       .buildAndExpand(itemCreated.getItemId())
                       .toUri();
               return ResponseEntity.created(uri).body(itemCreated);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") String itemId){
        String message = inventoryServices.deleteItemIfNotPresentInReferenceCollection(itemId);
        return new ResponseEntity<>(message,HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/items/{id}")
    public ResponseEntity<Item>updateItem(@RequestBody Item item,@PathVariable("id") String itemId){
        Item updatedItem = inventoryServices.updateItem(item,itemId);
        return  new ResponseEntity<>(updatedItem,HttpStatus.CREATED);
    }

    //////////////////////////////////////////-->Location-API-->//////////////////////////////////////////

    @GetMapping("/location")
    public ResponseEntity<List<Location>> locationDetails(){
        return new ResponseEntity<>(inventoryServices.locationDetails(),HttpStatus.OK);
    }
    @GetMapping("/location/{id}")
    public ResponseEntity<Location>findLocation(@PathVariable("id") String locationId){
        Location foundLocation = inventoryServices.findLocation(locationId);
        return ResponseEntity.ok(foundLocation);
    }
    @PostMapping("/location")
    public ResponseEntity<Location>createLocation(@RequestBody Location location)
            throws URISyntaxException{
        Location locationCreated = inventoryServices.createLocation(location);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(locationCreated.getLocationId())
                    .toUri();
            return ResponseEntity.created(uri).body(locationCreated);
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") String locationId){
        String result = inventoryServices.deleteLocationIfNotPresentInReferenceCollection(locationId);
        return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/location/{id}")
    public ResponseEntity<Location>updateLocation(@RequestBody Location location, @PathVariable("id") String locationId){
        Location updatedLocation = inventoryServices.updateLocation(location,locationId).get();
        return  new ResponseEntity<>(updatedLocation,HttpStatus.CREATED);
    }


/////////////////////////////////////////////////-->Supply API<--//////////////////////////////////////////////////////////
    @GetMapping("/supply")
    public ResponseEntity<List<Supply>>supplyDetails(){
        return new ResponseEntity<>(inventoryServices.supplyDetails(),HttpStatus.OK);
    }

    @GetMapping("/supply/{id}")
    public ResponseEntity<Supply>findSupply(@PathVariable("id") String supplyId){
        return new ResponseEntity<>(inventoryServices.findSupply(supplyId),HttpStatus.FOUND);
    }

    @GetMapping("/api/supply/{itemId}/{locationId}")
    public ResponseEntity<List<Supply>>findSupplyIfItemExistAtLocation(@PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(inventoryServices.findSupplyForItem(itemId,locationId),HttpStatus.OK);
    }

    @GetMapping("/api1/supply/{supplyType}/{locationId}")
    public ResponseEntity<List<Supply>>findSupplyWithSupplyTypeForSpecificLocation(@PathVariable("supplyType") String supplyType, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(inventoryServices.findSupplyWithSupplyTypeForSpecificLocation(supplyType,locationId),HttpStatus.OK);
    }


    @PostMapping("/supply")
    public ResponseEntity<Supply>createSupply(@RequestBody Supply supply){
        Supply supplyCreated = inventoryServices.createSupply(supply);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{supplyId}")
                .buildAndExpand(supplyCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(supplyCreated);
    }

    @DeleteMapping("/supply/{supplyId}")
    public ResponseEntity<String>deleteSupply(@PathVariable("supplyId")  String supplyId){
        return new ResponseEntity<>(inventoryServices.deleteSupply(supplyId),HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/supply/{supplyId}")
    public ResponseEntity<Supply>updateSupply(@PathVariable("supplyId") String supplyId, @RequestBody Supply supply){
        Supply updatedSupply = inventoryServices.updateSupply(supplyId, supply);
        return new ResponseEntity<>(updatedSupply,HttpStatus.OK);
    }

    //////////////////////////////////////////////////-->Demand API<--////////////////////////////////////////////////////////
    @GetMapping("/demand")
    public ResponseEntity<List<Demand>>demandDetails(){
        return new ResponseEntity<>(inventoryServices.demandDetails(),HttpStatus.OK);
    }

    @GetMapping("/demand/{id}")
    public ResponseEntity<Demand>findDemand(@PathVariable("id") String demandId){
        return new ResponseEntity<>(inventoryServices.findDemand(demandId),HttpStatus.FOUND);
    }

    @GetMapping("/api/demand/{item}/{location}")
    public ResponseEntity<List<Demand>> findDemandForItemAndLocation(@PathVariable("item") String itemId, @PathVariable("location") String loactionId){
       return new ResponseEntity<>(inventoryServices.findDemandForItem(itemId,loactionId),HttpStatus.OK);
    }

    @GetMapping("/api1/demand/{demandType}/{locationId}")
    public ResponseEntity<List<Demand>>findDemandWithDemandTypeForSpecificLocation(@PathVariable("demandType") String demandType, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(inventoryServices.findDemandWithDemandTypeForSpecificLocation(demandType,locationId),HttpStatus.OK);
    }


    @PostMapping("/demand")
    public ResponseEntity<Demand>createSupply(@RequestBody Demand demand){
        Demand demandCreated = inventoryServices.createDemand(demand);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{supplyId}")
                .buildAndExpand(demandCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(demandCreated);
    }

    @DeleteMapping("/demand/{demandId}")
    public ResponseEntity<String>deleteDemand(@PathVariable("demandId")  String demandId){
        return new ResponseEntity<>(inventoryServices.deleteDemand(demandId),HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/demand/{demandId}")
    public ResponseEntity<Demand>updateSupply(@PathVariable("demandId") String demandId, @RequestBody Demand demand){
        Demand updatedDemand = inventoryServices.updateDemand(demandId, demand);
        return new ResponseEntity<>(updatedDemand,HttpStatus.OK);
    }

    ////////////////////////////////////////////////////-->Availability API<--////////////////////////////////////////////////

    @GetMapping("/v1/availability/{itemId}/{locationId}")
    public ResponseEntity<Map<String,Object>>AvailableQtyOfTheItemAtTheGivenLocation(@PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId) throws JsonProcessingException {
        Map<String,Object> result  = inventoryServices.AvailableQtyOfTheItemAtTheGivenLocation(itemId,locationId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/v2/availability/{itemId}")
    public ResponseEntity<Map<String,Object>>AvailableQtyOfTheItemAtAllTheLocation(@PathVariable("itemId") String itemId) throws JsonProcessingException {
        Map<String,Object> result  = inventoryServices.AvailableQtyOfTheItemAtAllTheLocation(itemId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //////////////////////////////////////////////////////////////-->Threshold API<--///////////////////////////////////////////

    @GetMapping("/apt/threshold")
    public ResponseEntity<List<Threshold>> thresholdDetails(){
        return new ResponseEntity<>(inventoryServices.thresholdDetails(),HttpStatus.OK);
    }

    @GetMapping("/apt/threshold/{thresholdId}")
    public ResponseEntity<Threshold> findThresholdById(@PathVariable("thresholdId") String thresholdId){
        return  new ResponseEntity<>(inventoryServices.findThresholdById(thresholdId),HttpStatus.OK);
    }
    @GetMapping("/apt/threshold/{itemId}/{locationId}")
    public ResponseEntity<Threshold>findThresholdByItemIdAtLocationId(@PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId){
        return new ResponseEntity<>(inventoryServices.findThresholdByItemIdAtLocation(itemId,locationId),HttpStatus.OK);
    }
    @PostMapping("/apt/threshold")
    public ResponseEntity<Threshold>createThreshold(@RequestBody Threshold threshold){
        Threshold thresholdCreated = inventoryServices.createThreshold(threshold);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/thresholdId}")
                .buildAndExpand(thresholdCreated.getId())
                .toUri();
        return ResponseEntity.created(uri).body(thresholdCreated);
    }

    @DeleteMapping("/apt/threshold/{thresholdId}")
    public ResponseEntity<String>deleteThreshold(@PathVariable("thresholdId") String thresholdId){
        inventoryServices.deleteThreshold(thresholdId);
        return  new ResponseEntity<>("Threshold is deleted!",HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/apt/threshold/{thresholdId}")
    public ResponseEntity<Threshold>updateThreshold(@PathVariable("thresholdId") String thresholdId, @RequestBody Threshold threshold){
        Threshold updatedThreshold = inventoryServices.updateThreshold(thresholdId,threshold);
        return new ResponseEntity<>(updatedThreshold,HttpStatus.OK);
    }
    @PatchMapping("/api/apt/threshold/{itemId}/{locationId}")
    public ResponseEntity<Threshold>updateThreshold(@PathVariable("itemId") String itemId, @PathVariable("locationId")String locationId,  @RequestBody Threshold threshold){
        Threshold updatedThreshold = inventoryServices.updateThresholdByItemIdAndLocationId(itemId,locationId,threshold);
        return new ResponseEntity<>(updatedThreshold,HttpStatus.OK);
    }

}
