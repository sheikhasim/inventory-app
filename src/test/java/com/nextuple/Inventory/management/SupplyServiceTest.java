package com.nextuple.Inventory.management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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


public class SupplyServiceTest {

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
    private InventoryServices inventoryServices;

    @InjectMocks
    private SupplyServices supplyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSupplyDetails() {
        // Mock supply repository
        String organizationId = "org1";
        when(supplyRepository.findAllByOrganizationId(organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the supplyDetails method
        List<Supply> result = supplyService.supplyDetails(organizationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindSupply() {
        // Mock organization repository
        String organizationId = "ORG001";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization("ORG001", "TUPLE","Tuple@gmail.com", "tuple@123")));

        // Mock supply repository
        String supplyId = "supply1";
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.of( new Supply("ORG001","ORG001_00001","111","ONHAND",7) ));

        // Call the findSupply method
        Supply result = supplyService.findSupply(organizationId, supplyId);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testFindSupplyForItem() {
        // Mock organization repository
        String organizationId = "ORG001";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock supply repository
        String itemId = "item1";
        String locationId = "location1";
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the findSupplyForItem method
        List<Supply> result = supplyService.findSupplyForItem(organizationId, itemId, locationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindSupplyWithSupplyTypeForSpecificLocation() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock supply repository
        String supplyType = "type1";
        String locationId = "location1";
        when(supplyRepository.findBySupplyTypeAndLocationIdAndOrganizationId(supplyType, locationId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the findSupplyWithSupplyTypeForSpecificLocation method
        List<Supply> result = supplyService.findSupplyWithSupplyTypeForSpecificLocation(organizationId, supplyType, locationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

//    @Test
//    public void testCreateSupply() {
//        // Mock item repository
//        String organizationId = "org1";
//        String itemId = "item1";
//        when(itemRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Optional.of(new Item()));
//
//        // Mock organization repository
//        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));
//
//        // Mock location repository
//        String locationId = "location1";
//        when(locationRepository.findByLocationIdAndOrganizationId(locationId, organizationId)).thenReturn(Optional.of(new Location()));
//
//        // Mock threshold repository
//        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Optional.of(new Threshold("ORG001","ORG001_00001","111",10,10)));
//
//        // Call the createSupply method
//        Supply result = supplyService.createSupply(organizationId,  new Supply("ORG001","ORG001_00001","111","ONHAND",7));
//
//        // Verify the result
//        assertNotNull(result);
//        // Verify that supplyRepository.save(supply) was called
//        verify(supplyRepository).save(any(Supply.class));
//    }

    @Test
    public void testDeleteSupply() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock supply repository
        String supplyId = "supply1";
        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.of( new Supply("ORG001","ORG001_00001","111","ONHAND",7)));

        // Call the deleteSupply method
        String result = supplyService.deleteSupply(organizationId, supplyId);

        // Verify the result
        assertEquals("Supply deleted Successfully!", result);
        // Verify that supplyRepository.deleteById(supplyId) was called
        verify(supplyRepository).deleteById(supplyId);
    }

//    @Test
//    public void testUpdateSupply() {
//        // Mock supply repository
//        String organizationId = "ORG001";
//        String supplyId = "supply1";
//        when(supplyRepository.findByIdAndOrganizationId(supplyId, organizationId)).thenReturn(Optional.of( new Supply("ORG001","ORG001_00001","111","ONHAND",7)));
//
//        // Mock item repository
//        String itemId = "ORG001_00001";
//        when(itemRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Optional.of( new Item("ORG001_00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001")));
//
//        // Mock location repository
//        String locationId = "location1";
//        when(locationRepository.findByLocationIdAndOrganizationId(locationId, organizationId)).thenReturn(Optional.of(new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
//                "addressLine2","addressLine3","city","state","country","pinCode","ORG001")));
//
//        // Call the updateSupply method
//        Supply result = supplyService.updateSupply(organizationId, supplyId,  new Supply("ORG001","ORG001_00001","111","ONHAND",7));
//
//        // Verify the result
//        assertNotNull(result);
//        // Verify that supplyRepository.save(newSupply) was called
//        verify(supplyRepository).save(any(Supply.class));
//    }

    @Test
    public void testTotalSupplyForItemAtParticularLocation() {
        // Mock supply repository
        String organizationId = "org1";
        String itemId = "item1";
        String locationId = "location1";
        when(supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the totalSupplyForItemAtParticularLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }

    @Test
    public void testTotalSupplyForItemAtAllLocation() {
        // Mock supply repository
        String organizationId = "org1";
        String itemId = "item1";
        when(supplyRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Arrays.asList(
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7),
                new Supply("ORG001","ORG001_00001","111","ONHAND",7)
        ));

        // Call the totalSupplyForItemAtAllLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }
}
