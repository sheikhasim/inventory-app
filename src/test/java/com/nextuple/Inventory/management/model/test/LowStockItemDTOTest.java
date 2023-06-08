package com.nextuple.Inventory.management.model.test;

import com.nextuple.Inventory.management.dto.LowStockItemDTO;
import org.junit.Assert;
import org.junit.Test;

public class LowStockItemDTOTest {

    @Test
    public void testConstructorAndGetters() {
        String itemId = "12345";
        String locationId = "A1";
        String stockType = "Normal";
        int quantity = 10;

        LowStockItemDTO lowStockItemDTO = new LowStockItemDTO(itemId, locationId, stockType, quantity);

        Assert.assertEquals(itemId, lowStockItemDTO.getItemId());
        Assert.assertEquals(locationId, lowStockItemDTO.getLocationId());
        Assert.assertEquals(stockType, lowStockItemDTO.getStockType());
        Assert.assertEquals(quantity, lowStockItemDTO.getQuantity());
    }

    @Test
    public void testSetters() {
        LowStockItemDTO lowStockItemDTO = new LowStockItemDTO();

        String itemId = "12345";
        String locationId = "A1";
        String stockType = "Normal";
        int quantity = 10;

        lowStockItemDTO.setItemId(itemId);
        lowStockItemDTO.setLocationId(locationId);
        lowStockItemDTO.setStockType(stockType);
        lowStockItemDTO.setQuantity(quantity);

        Assert.assertEquals(itemId, lowStockItemDTO.getItemId());
        Assert.assertEquals(locationId, lowStockItemDTO.getLocationId());
        Assert.assertEquals(stockType, lowStockItemDTO.getStockType());
        Assert.assertEquals(quantity, lowStockItemDTO.getQuantity());
    }
}
