package com.nextuple.Inventory.management;

import com.nextuple.Inventory.management.service.ThresholdService;
import org.junit.jupiter.api.Test;

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
import static org.mockito.Mockito.when;

public class ThresholdServicesTest {
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
    private ThresholdService thresholdService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testThresholdDetails() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        when(thresholdRepository.findAllByOrganizationId(organizationId)).thenReturn(Arrays.asList(
                new Threshold(),
                new Threshold(),
                new Threshold()
        ));

        // Call the thresholdDetails method
        List<Threshold> result = thresholdService.thresholdDetails(organizationId);

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindThresholdById() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        String thresholdId = "threshold1";
        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId)).thenReturn(Optional.of(new Threshold()));

        // Call the findThresholdById method
        Threshold result = thresholdService.findThresholdById(organizationId, thresholdId);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testFindThresholdByItemIdAtLocation() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        String itemId = "item1";
        String locationId = "location1";
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Optional.of(new Threshold()));

        // Call the findThresholdByItemIdAtLocation method
        Threshold result = thresholdService.findThresholdByItemIdAtLocation(organizationId, itemId, locationId);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testCreateThreshold() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
        when(thresholdRepository.save(any(Threshold.class))).thenReturn(new Threshold());

        // Call the createThreshold method
        Threshold result = thresholdService.createThreshold(organizationId, new Threshold());

        // Verify the result
        assertNotNull(result);
        // Verify that thresholdRepository.save(threshold) was called
        verify(thresholdRepository).save(any(Threshold.class));
    }

    @Test
    public void testDeleteThreshold() {
        // Mock organization repository
        String organizationId = "org1";
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(new Organization()));

        // Mock threshold repository
        String thresholdId = "threshold1";
        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId)).thenReturn(Optional.of(new Threshold()));

        // Call the deleteThreshold method
        thresholdService.deleteThreshold(organizationId, thresholdId);

        // Verify that thresholdRepository.deleteById(thresholdId) was called
        verify(thresholdRepository).deleteById(thresholdId);
    }

//    @Test
//    public void testUpdateThreshold() {
//        // Mock threshold repository
//        String organizationId = "org1";
//        String thresholdId = "threshold1";
//        when(thresholdRepository.findByIdAndOrganizationId(thresholdId, organizationId)).thenReturn(Optional.of(new Threshold()));
//
//        // Call the updateThreshold method
//        Threshold result = thresholdService.updateThreshold(organizationId, thresholdId, new Threshold());
//
//        // Verify the result
//        assertNotNull(result);
//        // Verify that thresholdRepository.save(newThreshold) was called
//        verify(thresholdRepository).save(any(Threshold.class));
//    }

//    @Test
//    public void testUpdateThresholdByItemIdAndLocationId() {
//        // Mock threshold repository
//        String organizationId = "org1";
//        String itemId = "item1";
//        String locationId = "location1";
//        when(thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId)).thenReturn(Optional.of(new Threshold()));
//
//        // Call the updateThresholdByItemIdAndLocationId method
//        Threshold result = thresholdService.updateThresholdByItemIdAndLocationId(organizationId, itemId, locationId, new Threshold());
//
//        // Verify the result
//        assertNotNull(result);
//        // Verify that thresholdRepository.save(newThreshold) was called
//        verify(thresholdRepository).save(any(Threshold.class));
//    }
}







