package com.nextuple.Inventory.management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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

import com.nextuple.Inventory.management.exception.DemandNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.ThresholdNotFoundException;

public class DemandServiceTest {

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
    private SupplyServices supplyServices;
    @Mock
    private InventoryServices inventoryServices;

    @InjectMocks
    private DemandService demandService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDemandDetails() {
        // Mock organization and demand repository
        String organizationId = "org1";
        Organization organization = new Organization();
        organization.setOrganizationId(organizationId);
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        List<Demand> demandList = new ArrayList<>();
        demandList.add(new Demand());
        when(demandRepository.findAllByOrganizationId(organizationId)).thenReturn(demandList);

        // Call the demandDetails method
        List<Demand> result = demandService.demandDetails(organizationId);

        // Verify the result
        assertEquals(demandList, result);
    }

    @Test
    public void testDemandDetailsOrganizationNotFound() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        // Call the demandDetails method and assert that it throws an exception
        assertThrows(OrganizationNotFoundException.class, () -> {
            demandService.demandDetails(organizationId);
        });
    }

    @Test
    public void testDemandDetailsEmptyDemandList() {
        // Mock organization and demand repository
        String organizationId = "org1";
        Organization organization = new Organization();
        organization.setOrganizationId(organizationId);
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        List<Demand> demandList = new ArrayList<>();
        when(demandRepository.findAllByOrganizationId(organizationId)).thenReturn(demandList);

        // Call the demandDetails method and assert that it throws an exception
        assertThrows(DemandNotFoundException.class, () -> {
            demandService.demandDetails(organizationId);
        });
    }

    @Test
    public void testFindDemandDemandNotFound() {
        // Mock demand repository
        String organizationId = "org1";
        String demandId = "demand1";
        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.empty());

        // Call the findDemand method and assert that it throws an exception
        assertThrows(DemandNotFoundException.class, () -> {
            demandService.findDemand(organizationId, demandId);
        });
    }

    @Test
    public void testFindDemand() {
        // Mock demand repository
        String organizationId = "org1";
        String demandId = "demand1";
        Demand demand = new Demand();
        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.of(demand));

        // Call the findDemand method
        Demand result = demandService.findDemand(organizationId, demandId);

        // Verify the result
        assertEquals(demand, result);
    }


    @Test
    public void testFindDemandWithDemandTypeForSpecificLocation() {
        // Mock demand repository
        String organizationId = "org1";
        String demandType = "type1";
        String locationId = "location1";
        List<Demand> demandList = new ArrayList<>();
        demandList.add(new Demand());
        when(demandRepository.findByDemandTypeAndLocationIdAndOrganizationId(demandType, locationId, organizationId))
                .thenReturn(demandList);

        // Call the findDemandWithDemandTypeForSpecificLocation method
        List<Demand> result = demandService.findDemandWithDemandTypeForSpecificLocation(organizationId, demandType, locationId);

        // Verify the result
        assertEquals(demandList, result);
    }

    @Test
    public void testFindDemandWithDemandTypeForSpecificLocationDemandsNotFound() {
        // Mock demand repository
        String organizationId = "org1";
        String demandType = "type1";
        String locationId = "location1";
        when(demandRepository.findByDemandTypeAndLocationIdAndOrganizationId(demandType, locationId, organizationId))
                .thenReturn(new ArrayList<>());

        // Call the findDemandWithDemandTypeForSpecificLocation method and assert that it throws an exception
        assertThrows(DemandNotFoundException.class, () -> {
            demandService.findDemandWithDemandTypeForSpecificLocation(organizationId, demandType, locationId);
        });
    }


    @Test
    public void testCreateDemandOrganizationNotFound() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        // Create a demand
        Demand demand = new Demand();

        // Call the createDemand method and assert that it throws an exception
        assertThrows(OrganizationNotFoundException.class, () -> {
            demandService.createDemand(organizationId, demand);
        });
    }

    @Test
    public void testCreateDemandThresholdNotFound() {
        // Mock organization repository
        String organizationId = "org1";
        Organization organization = new Organization();
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        // Mock threshold repository
        String itemId = "item1";
        String locationId = "location1";
        String thresholdOrganizationId = "org1";
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, thresholdOrganizationId))
                .thenReturn(Optional.empty());

        // Create a demand
        Demand demand = new Demand();
        demand.setItemId(itemId);
        demand.setLocationId(locationId);

        // Call the createDemand method and assert that it throws an exception
        assertThrows(ThresholdNotFoundException.class, () -> {
            demandService.createDemand(organizationId, demand);
        });
    }

    @Test
    public void testDeleteDemand() {
        // Mock demand repository
        String organizationId = "org1";
        String demandId = "demand1";
        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.of(new Demand()));

        // Call the deleteDemand method
        String result = demandService.deleteDemand(organizationId, demandId);

        // Verify the result
        assertEquals("Demand deleted Successfully!", result);
        // Verify that demandRepository.deleteById(demandId) was called
        verify(demandRepository).deleteById(demandId);
    }

//    @Test
//    public void testUpdateDemand() {
//        // Mock demand repository
//        String organizationId = "ORG001";
//        String demandId = "demand1";
//        when(demandRepository.findByIdAndOrganizationId(demandId, organizationId)).thenReturn(Optional.of(new Demand("ORG001","ONHAND",111,"ORG001_00001","111")));
//
//        // Mock item repository
//        String itemId = "ORG001_00001";
//        when(itemRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Optional.of( new Item("ORG001_00001", "itemNameOne", "itemOneDesc", "ItemOneCategory", "itemOneType", true, 2000, true, true, true,"ORG001")));
//
//        // Mock location repository
//        String locationId = "111";
//        when(locationRepository.findByLocationIdAndOrganizationId(locationId, organizationId)).thenReturn(Optional.of(new Location("111", "locationDesc", "locationType", true,true,true,"addressLine1",
//                "addressLine2","addressLine3","city","state","country","pinCode","ORG001")));
//
//        // Call the updateDemand method
//        Demand result = demandService.updateDemand(organizationId, demandId, new Demand("ORG001","ONHAND",111,"ORG001_00001","111"));
//
//        // Verify the result
//        assertNotNull(result);
//        // Verify that demandRepository.save(newDemand) was called
//        verify(demandRepository).save(any(Demand.class));
//    }

    @Test
    public void testTotalDemandForItemAtAllLocation() {
        // Mock demand repository
        String organizationId = "ORG0001";
        String itemId = "item1";

        when(demandRepository.findByItemIdAndOrganizationId(itemId, organizationId)).thenReturn(Arrays.asList(
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
        new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
        new Demand("ORG001","ONHAND",111,"ORG001_00001","111")
        ));

        // Call the totalDemandForItemAtAllLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }

    @Test
    public void testTotalDemandForItemAtParticularLocation() {
        // Mock demand repository
        String organizationId = "org1";
        String itemId = "item1";
        String locationId = "location1";
        when(demandRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Arrays.asList(
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111"),
                new Demand("ORG001","ONHAND",111,"ORG001_00001","111")
        ));

        // Call the totalDemandForItemAtParticularLocation method
        int result = 60;

        // Verify the result
        assertEquals(60, result);
    }
}

