package com.nextuple.Inventory.management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nextuple.Inventory.management.service.InventoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InventoryController {
    @Autowired
    private InventoryServices inventoryServices;

    ////////////////////////////////////////////////////-->Availability API<--////////////////////////////////////////////////

    @GetMapping("/v1/availability/{userId}/{itemId}/{locationId}")
    public ResponseEntity<Map<String,Object>> AvailableQtyOfTheItemAtTheGivenLocation(@PathVariable("userId")String userId, @PathVariable("itemId") String itemId, @PathVariable("locationId") String locationId) throws JsonProcessingException {
        Map<String,Object> result  = inventoryServices.AvailableQtyOfTheItemAtTheGivenLocation(userId,itemId,locationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/v2/availability/{userId}/{itemId}")
    public ResponseEntity<Map<String,Object>>AvailableQtyOfTheItemAtAllTheLocation(@PathVariable("userId")String userId,@PathVariable("itemId") String itemId) throws JsonProcessingException {
        Map<String,Object> result  = inventoryServices.AvailableQtyOfTheItemAtAllTheLocation(userId,itemId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
