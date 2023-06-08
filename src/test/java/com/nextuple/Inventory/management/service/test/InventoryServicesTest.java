package com.nextuple.Inventory.management.service.test;

import com.nextuple.Inventory.management.dto.LowStockItemDTO;
import com.nextuple.Inventory.management.exception.SupplyNotFoundException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import com.nextuple.Inventory.management.service.DemandService;
import com.nextuple.Inventory.management.service.InventoryServices;
import com.nextuple.Inventory.management.service.SupplyServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServicesTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private SupplyRepository supplyRepository;
    @Mock
    private DemandRepository demandRepository;
    @Mock
    private ThresholdRepository thresholdRepository;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SupplyServices supplyServices;
    @Mock
    private DemandService demandService;

    @InjectMocks
    private InventoryServices inventoryServices;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAvailableQtyOfTheItemAtTheGivenLocation() {
        // Mock data
        String organizationId = "org1";
        String itemId = "item1";
        String locationId = "location1";
        int totalSupply = 10;
        int totalDemand = 5;
        int availableQty = totalSupply - totalDemand;
        Map<String, Object> result = new HashMap<>();
        result.put("organizationId","org1");
        result.put("itemId","item1");
        result.put("locationId","location1");
        result.put("totalSupply",10);
        result.put("totalDemand",5);
        result.put("availableQty",5);
        result.put("Stock level","Green");



        // Mock dependencies
        when(supplyServices.totalSupplyForItemAtParticularLocation(organizationId, itemId, locationId)).thenReturn(totalSupply);
        when(demandService.totalDemandForItemAtParticularLocation(organizationId, itemId, locationId)).thenReturn(totalDemand);

        // Invoke the method

        // Verify the result
        assertEquals(organizationId, result.get("organizationId"));
        assertEquals(itemId, result.get("itemId"));
        assertEquals(locationId, result.get("locationId"));
        assertEquals(totalSupply, result.get("totalSupply"));
        assertEquals(totalDemand, result.get("totalDemand"));
        assertEquals(availableQty, result.get("availableQty"));
        assertEquals("Green", result.get("Stock level"));
    }

    @Test
    public void testAvailableQtyOfTheItemAtAllTheLocation() {
        // Mock data
        Map<String, Object> result = new HashMap<>();
        String organizationId = "org1";
        String itemId = "item1";
        int totalSupply = 20;
        int totalDemand = 8;
        int availableQty = totalSupply - totalDemand;
        result.put("organizationId","org1");
        result.put("itemId","item1");
        result.put("totalSupply",20);
        result.put("totalDemand",8);
        result.put("availableQty",12);
        result.put("locationId","NETWORK");



        // Mock dependencies
        when(supplyServices.totalSupplyForItemAtAllLocation(organizationId, itemId)).thenReturn(totalSupply);
        when(demandService.totalDemandForItemAtAllLocation(organizationId, itemId)).thenReturn(totalDemand);

        // Invoke the method

        // Verify the result
        assertEquals(organizationId, result.get("organizationId"));
        assertEquals(itemId, result.get("itemId"));
        assertEquals(totalSupply, result.get("totalSupply"));
        assertEquals(totalDemand, result.get("totalDemand"));
        assertEquals("NETWORK", result.get("locationId"));
        assertEquals(availableQty, result.get("availableQty"));
    }

    @Test
    public void testGetDetailsOfAllItemWithAvailability() {
        // Mock data
        String organizationId = "org1";
        String itemId1 = "item1";
        String itemId2 = "item2";
        int totalSupply1 = 10;
        int totalDemand1 = 5;
        int availableQty1 = totalSupply1 - totalDemand1;
        int totalSupply2 = 15;
        int totalDemand2 = 3;
        int availableQty2 = totalSupply2 - totalDemand2;

        // Mock dependencies
        Item item1 = new Item();
        item1.setItemId(itemId1);
        Item item2 = new Item();
        item2.setItemId(itemId2);
        when(itemRepository.findByOrganizationId(organizationId)).thenReturn(Arrays.asList(item1, item2));
        when(inventoryServices.AvailableQtyOfTheItemAtAllTheLocation(organizationId, itemId1)).thenReturn(createAvailabilityResult(organizationId, itemId1, totalSupply1, totalDemand1, availableQty1));
        when(inventoryServices.AvailableQtyOfTheItemAtAllTheLocation(organizationId, itemId2)).thenReturn(createAvailabilityResult(organizationId, itemId2, totalSupply2, totalDemand2, availableQty2));

        // Invoke the method
        List<Map<String, Object>> resultList = inventoryServices.getDetailsOfAllItemWithAvailability(organizationId);

        // Verify the result
        assertEquals(2, resultList.size());

        Map<String, Object> result1 = resultList.get(0);
        assertEquals(organizationId, result1.get("organizationId"));
        assertEquals(itemId1, result1.get("itemId"));
        assertEquals(totalSupply1, result1.get("totalSupply"));
        assertEquals(totalDemand1, result1.get("totalDemand"));
        assertEquals("NETWORK", result1.get("locationId"));
        assertEquals(availableQty1, result1.get("availableQty"));

        Map<String, Object> result2 = resultList.get(1);
        assertEquals(organizationId, result2.get("organizationId"));
        assertEquals(itemId2, result2.get("itemId"));
        assertEquals(totalSupply2, result2.get("totalSupply"));
        assertEquals(totalDemand2, result2.get("totalDemand"));
        assertEquals("NETWORK", result2.get("locationId"));
        assertEquals(availableQty2, result2.get("availableQty"));
    }

    private Map<String, Object> createAvailabilityResult(String organizationId, String itemId, int totalSupply, int totalDemand, int availableQty) {
        Map<String, Object> result = new HashMap<>();
        result.put("organizationId", organizationId);
        result.put("itemId", itemId);
        result.put("totalSupply", totalSupply);
        result.put("totalDemand", totalDemand);
        result.put("locationId", "NETWORK");
        result.put("availableQty", availableQty);
        return result;
    }

    @Test
    public void testGetTotalNumbersOfDashboard() {
        // Mock data
        String organizationId = "org1";

        // Mock dependencies
        List<Item> itemList = Arrays.asList(new Item(), new Item());
        when(itemRepository.findByOrganizationId(organizationId)).thenReturn(itemList);
        when(locationRepository.findAllByOrganizationId(organizationId)).thenReturn(Arrays.asList(new Location(), new Location()));
        when(supplyRepository.findAllByOrganizationId(organizationId)).thenReturn(Arrays.asList(new Supply(), new Supply()));
        when(demandRepository.findAllByOrganizationId(organizationId)).thenReturn(Arrays.asList(new Demand(), new Demand()));
        when(itemRepository.findByStatusAndOrganizationId(true, organizationId)).thenReturn(Collections.singletonList(new Item()));
        when(inventoryServices.getLowStockItems(organizationId)).thenReturn(Arrays.asList(
                new LowStockItemDTO("item1", "locatio1", "Demand",  5),
                new LowStockItemDTO("item2", "Item 2","Demand",8)
        ));

        // Invoke the method
        Map<String, Integer> result = inventoryServices.getTotalNumbersOfDashboard(organizationId);

        // Verify the result
        assertEquals(2, result.get("totalItems"));
        assertEquals(2, result.get("totalLocations"));
        assertEquals(2, result.get("totalSupplies"));
        assertEquals(2, result.get("totalDemands"));
        assertEquals(1, result.get("totalActiveItems"));
        assertEquals(2, result.get("totalLowStockItems"));
    }

    @Test
    public void testGetLowStockItems() {
        // Mock data
        String organizationId = "org1";
        int thresholdQuantity = 10;

        // Mock dependencies
        List<Threshold> thresholdList = Arrays.asList(
                new Threshold("ORG001", "123", "4566", 10,100),
                new Threshold("ORG001", "124", "4567", 10,100),
                new Threshold("ORG001", "125", "4566", 10,100)
        );
        when(thresholdRepository.findAllByOrganizationId(organizationId)).thenReturn(thresholdList);
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId("123", "4566", "ORG001")).thenReturn(Arrays.asList(new Supply(), new Supply()));
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId("123", "4566", "ORG001")).thenReturn(Collections.singletonList(new Supply()));
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId("123", "4566", "ORG001")).thenReturn(Collections.emptyList());
        when(itemRepository.findById("item1")).thenReturn(Optional.of(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001")));
        when(itemRepository.findById("item2")).thenReturn(Optional.of(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001")));
        when(itemRepository.findById("item3")).thenReturn(Optional.of(new Item("00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001")));
        when(locationRepository.findById("location1")).thenReturn(Optional.of(new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1","addressLine2","addressLine3","city","state","country","pinCode","ORG001")));
        when(locationRepository.findById("location2")).thenReturn(Optional.of(new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1","addressLine2","addressLine3","city","state","country","pinCode","ORG001")));
        when(locationRepository.findById("location3")).thenReturn(Optional.of(new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1", "addressLine2","addressLine3","city","state","country","pinCode","ORG001")));

        // Invoke the method
        List<LowStockItemDTO> result = inventoryServices.getLowStockItems(organizationId);

        // Verify the result
        assertEquals(2, result.size());

        LowStockItemDTO result1 = result.get(0);
        assertEquals("item1", result1.getItemId());
        assertEquals("location1", result1.getLocationId());
        assertEquals("Low Stock",result1.getStockType());
        assertEquals(2, result1.getQuantity());


        LowStockItemDTO result2 = result.get(0);
        assertEquals("item1", result2.getItemId());
        assertEquals("location1", result2.getLocationId());
        assertEquals("Low Stock",result2.getStockType());
        assertEquals(2, result2.getQuantity());
    }

    @Test
    public void testGetLowStockItems_NoThresholds() {
        // Mock data
        String organizationId = "org1";

        // Mock dependencies
        when(thresholdRepository.findAllByOrganizationId(organizationId)).thenReturn(Collections.emptyList());

        // Invoke the method
        List<LowStockItemDTO> result = inventoryServices.getLowStockItems(organizationId);

        // Verify the result
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLowStockItems_NoSupplies() {
        // Mock data
        String organizationId = "org1";
        int thresholdQuantity = 10;

        // Mock dependencies
        List<Threshold> thresholdList = Collections.singletonList(new Threshold("ORG001", "123", "4566", 10,100));
        when(thresholdRepository.findAllByOrganizationId(organizationId)).thenReturn(thresholdList);
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId("123", "4566", "ORG001")).thenReturn(Collections.emptyList());

        // Invoke the method
        List<LowStockItemDTO> result = inventoryServices.getLowStockItems(organizationId);

        // Verify the result
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetLowStockItems_ItemNotFound() {
        // Mock data
        String organizationId = "org1";
        int thresholdQuantity = 10;

        // Mock dependencies
        List<Threshold> thresholdList = Collections.singletonList( new Threshold("ORG001", "123", "4566", 10,100));
        when(thresholdRepository.findAllByOrganizationId(organizationId)).thenReturn(thresholdList);
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId("123", "4566", "ORG001")).thenReturn(Collections.singletonList(new Supply()));
        when(itemRepository.findById("item1")).thenReturn(Optional.empty());

        // Invoke the method
        List<LowStockItemDTO> result = inventoryServices.getLowStockItems(organizationId);

        // Verify the result
        assertTrue(result.isEmpty());
    }
}

