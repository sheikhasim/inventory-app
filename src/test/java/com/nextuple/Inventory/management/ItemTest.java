package com.nextuple.Inventory.management;

import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.exception.UserNotFoundException;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.model.Supply;
import com.nextuple.Inventory.management.repository.*;
import com.nextuple.Inventory.management.service.InventoryServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.util.RaceTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private SupplyRepository supplyRepository;

    @Mock
    private DemandRepository demandRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private ThresholdRepository thresholdRepository;
    @InjectMocks
    private InventoryServices inventoryServices;

    //please use customized exception to cover test case replace by finding appropriate exception
    /////////////////////////////////////-->Test Cases<--///////////////////////////////////////////////

    @Test
    void testItemDetails() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true));
        itemList.add(new Item("00002", "itemNameTwo", "itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true));

        when(itemRepository.findAll()).thenReturn(itemList);

        List<Item> actualResult = inventoryServices.itemDetails();
        assertEquals(2, actualResult.size());
        assertEquals("00001", actualResult.get(0).getItemId());
        assertEquals("itemOneDesc", actualResult.get(0).getItemDescription());
    }

    @Test
    void testItemDetailsWithEmptyLis() {
        when(itemRepository.findAll()).thenReturn(new ArrayList<Item>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.itemDetails();
        });
        assertEquals("There is no items are Exist in the Inventory!", exception.getMessage());
    }

    @Test
    void testFindItem() {
        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);

        when(itemRepository.findById("00001")).thenReturn(Optional.of(item));

        Item actualResult = inventoryServices.findItem("00001");
        assertEquals("00001", actualResult.getItemId());
        assertEquals("01", actualResult.getStatus());
    }

    @Test
    void testFindItemWithInvalidItemId() {
        when(itemRepository.findById("00003")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.findItem("00003");
        });
        assertEquals("item with itemId:00003 not found", exception.getMessage());
    }

    @Test
    void testValidateItem() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true));
        itemList.add(new Item("00002", "itemNameTwo", "itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true));

        Item itemNotFound = new Item("00003", "itemNameThree", "itemThreeeDesc", "ItemThreeCategory", "itemThreeType", "01", "1999", true, true, true);
        Item itemFound = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);

        when(itemRepository.findAll()).thenReturn(itemList);

        assertTrue(inventoryServices.validateItem(itemNotFound));
        assertFalse(inventoryServices.validateItem(itemFound));
    }

    @Test
    void testCreateTest() {
        Item item = new Item("00001", "itemOneName", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item actualResult = inventoryServices.createItem(item);

        assertEquals("00001", actualResult.getItemId());
        assertEquals("itemOneName", actualResult.getItemName());
    }

    @Test
    void testCreateItemWithExistItemId() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true));
        itemList.add(new Item("00002", "itemNameTwo", "itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true));

        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);

        when(itemRepository.findAll()).thenReturn(itemList);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.createItem(item);
        });
        assertEquals("Item with ID 00001 already exists", exception.getMessage());
    }

    @Test
    void testDeleteItem() {
        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);

        when(itemRepository.findById("00001")).thenReturn(Optional.of(item));
        when(supplyRepository.findByItem("00001")).thenReturn(new ArrayList<>());
        when(demandRepository.findByItem("00001")).thenReturn(new ArrayList<>());

        String actualResult = inventoryServices.deleteItemIfNotPresentInReferenceCollection("00001");

        assertEquals("item deleted", actualResult);
    }

    @Test
    void testDeleteItemWithSupplyOrDemandExist() {
        Item item = new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);
        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("00001", "11111", "ONHAND", 100));
        when(itemRepository.findById("00001")).thenReturn(Optional.of(item));
        when(supplyRepository.findByItem("00001")).thenReturn(supplyList);
        when(demandRepository.findByItem("00001")).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.deleteItemIfNotPresentInReferenceCollection("00001");
        });
        assertEquals("Item Exist with supply or demand", exception.getMessage());
    }


//    @Test
//    public void testUpdateItem() {
//        // create test item
//        Item item= new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);
//        Item newItem = new  Item("00002", "itemNameTwo", "itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true);
//
//
//        // mock repository findById to return test item
//        when(itemRepository.findById("00001")).thenReturn(Optional.of(item));
//
//        // update item
//        Item updatedItem = inventoryServices.updateItem(newItem, "00001");
//
//        // verify that the item has been updated
//        assertEquals(updatedItem.getItemName(), newItem.getItemName());
//        assertEquals(updatedItem.getItemDescription(), newItem.getItemDescription());
//        assertEquals(updatedItem.getCategory(), newItem.getCategory());
//        assertEquals(updatedItem.getPrice(), newItem.getPrice());
//        assertEquals(updatedItem.getType(), newItem.getType());
//        assertEquals(updatedItem.getStatus(), newItem.getStatus());
//        assertEquals(updatedItem.isDeliveryAllowed(), newItem.isDeliveryAllowed());
//        assertEquals(updatedItem.isPickupAllowed(), newItem.isPickupAllowed());
//        assertEquals(updatedItem.isShippingAllowed(), newItem.isShippingAllowed());
//    }

//    @Test
//    public void testUpdateItemNotFound() {
//        // mock repository findById to return null
//        when(itemRepository.findById("1")).thenReturn(null);
//
//        // create new item to update with
//        Item newItem = new Item();
//        newItem.setItemName("New Test Item Name");
//        newItem.setItemDescription("New Test Description");
//        newItem.setCategory("New Test Category");
//        newItem.setPrice("20.0");
//        newItem.setType("New Test Type");
//        newItem.setStatus("New Test Status");
//        newItem.setDeliveryAllowed(false);
//        newItem.setPickupAllowed(true);
//        newItem.setShippingAllowed(false);
//
//        // verify that UserNotFoundException is thrown
//        assertThrows(UserNotFoundException.class, () -> inventoryServices.updateItem(newItem, "1"));
//    }

    //////////////////////////////////////////Location Test///////////////////////////////////////////
    @Test
    public void testLocationDetails() {
        Location location1 = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        Location location2 = new Location("2222", ":Lulu Mall", "store", true, true, true, "Line One", "Line Two", "Line Three", "Gulbarga", "Karnataka", "india", "123456");

        List<Location> locationList = new ArrayList<>();
        locationList.add(location1);
        locationList.add(location2);

        when(locationRepository.findAll()).thenReturn(locationList);

        List<Location> result = inventoryServices.locationDetails();

        assertEquals(locationList, result);
    }

    @Test
    void testFindLocationById() {
        Location location1 = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        Location location2 = new Location("2222", ":Lulu Mall", "store", true, true, true, "Line One", "Line Two", "Line Three", "Gulbarga", "Karnataka", "india", "123456");

        when(locationRepository.findById("1111")).thenReturn(Optional.of(location1));

        Location result = inventoryServices.findLocation("1111");
        assertEquals("Forum Mall", result.getLocationDesc());
    }

    @Test
    void testFindLocationByInvalidId() {
        when(locationRepository.findById("1111")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.findLocation("1111");
        });
        assertEquals("location with locationId:1111 is not found", exception.getMessage());
    }

    @Test
    public void testCreateNewLocation() {
        Location location = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        when(locationRepository.findById("1111")).thenReturn(Optional.empty());
        when(locationRepository.save(location)).thenReturn(location);

        Location result = inventoryServices.createLocation(location);

        assertNotNull(result);
        assertEquals(location, result);
    }

    @Test
    void testCreateLocationWithExistingId() {
        Location location = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        when(locationRepository.findById("1111")).thenReturn(Optional.of(location));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.createLocation(location);
        });
        assertEquals("location with locationId:1111 is already exist", exception.getMessage());
    }

    @Test
    void testDeleteLocation() {
        Location location = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        when(locationRepository.findById("1111")).thenReturn(Optional.of(location));
        when(supplyRepository.findByItem("1111")).thenReturn(new ArrayList<>());
        when(demandRepository.findByItem("1111")).thenReturn(new ArrayList<>());

        String actualResult = inventoryServices.deleteLocationIfNotPresentInReferenceCollection("1111");

        assertEquals("Location deleted!", actualResult);
    }

    @Test
    void testDeleteLocationWhenSupplyAndDemandExist() {
        Location location = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("00001", "1111", "ONHAND", 100));
        when(locationRepository.findById("1111")).thenReturn(Optional.of(location));
        when(supplyRepository.findByItem("00001")).thenReturn(supplyList);
        when(demandRepository.findByItem("00001")).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryServices.deleteLocationIfNotPresentInReferenceCollection("1111");
        });
        assertEquals("Location Exist with supply or demand", exception.getMessage());
    }

    @Test
    public void deleteLocationIfNotPresentInReferenceCollectionWhenSupplyAndDemandIsNotEmpty() {
        Location location = new Location("1111", "Forum Mall", "DC", true, true, true, "Line One", "Line Two", "Line Three", "banglore", "Karnataka", "india", "123456");

        List<Supply> supplyList = new ArrayList<>();
        supplyList.add(new Supply("00001", "1111", "ONHAND", 100));


        when(locationRepository.findById("1111")).thenReturn(Optional.of(location));
        when(supplyRepository.findByItem("1111")).thenReturn(supplyList);

        // When & Then
        SupplyAndDemandExistException exception = assertThrows(SupplyAndDemandExistException.class, () -> inventoryServices.deleteLocationIfNotPresentInReferenceCollection("1111"));
        assertEquals("Location Exist with supply or demand", exception.getMessage());
    }

    @Test
    public void deleteLocationIfNotPresentInReferenceCollection_ShouldThrowUserNotFoundException_WhenLocationNotFound() {
        when(locationRepository.findById("1111")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> inventoryServices.deleteLocationIfNotPresentInReferenceCollection("1111"));
        assertEquals("Location with locationId:1111 not found!", exception.getMessage());
    }

    @Test
    public void updateLocation_ShouldReturnUpdatedLocation_WhenLocationExists() {
        // Given
        String locationId = "1";
        Location existingLocation = new Location();
        existingLocation.setLocationId(locationId);
        existingLocation.setCity("New York");
        existingLocation.setCountry("USA");

        Location updatedLocation = new Location();
        updatedLocation.setLocationId(locationId);
        updatedLocation.setCity("Los Angeles");
        updatedLocation.setCountry("USA");

        when(locationRepository.findById(locationId)).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(existingLocation)).thenReturn(updatedLocation);

        // When
        Optional<Location> result = inventoryServices.updateLocation(updatedLocation, locationId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(updatedLocation, result.get());
    }

    @Test
    public void updateLocation_ShouldReturnEmptyOptional_WhenLocationDoesNotExist() {
        // Given
        String locationId = "1";
        Location updatedLocation = new Location();
        updatedLocation.setLocationId(locationId);
        updatedLocation.setCity("Los Angeles");
        updatedLocation.setCountry("USA");

        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        // When
        Optional<Location> result = inventoryServices.updateLocation(updatedLocation, locationId);

        // Then
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void updateLocation_ShouldThrowUserNotFoundException_WhenLocationDoesNotExist() {
        // Given
        String locationId = "1";
        Location updatedLocation = new Location();
        updatedLocation.setLocationId(locationId);
        updatedLocation.setCity("Los Angeles");
        updatedLocation.setCountry("USA");

        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> inventoryServices.updateLocation(updatedLocation, locationId));
    }

}


//-----------------------------------Pending test case--------------------------------------
//update item
//supply->full
//demand->full
//threshold->full

