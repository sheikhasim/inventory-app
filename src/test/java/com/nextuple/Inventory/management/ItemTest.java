package com.nextuple.Inventory.management;

import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.repository.ItemRepository;
import com.nextuple.Inventory.management.service.InventoryServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @InjectMocks
    private InventoryServices inventoryServices;

    //please use customized exception to cover test case replace by finding appropriate exception
    /////////////////////////////////////-->Test Cases<--///////////////////////////////////////////////

    @Test
    void testItemDetails(){
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("00001","itemNameOne","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true));
        itemList.add(new Item("00002","itemNameTwo","itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true));

        when(itemRepository.findAll()).thenReturn(itemList);

        List<Item> actualResult = inventoryServices.itemDetails();
        assertEquals(2,actualResult.size());
        assertEquals("00001",actualResult.get(0).getItemId());
        assertEquals("itemOneDesc",actualResult.get(0).getItemDescription());
    }

    @Test
    void testItemDetailsWithEmptyLis(){
        when(itemRepository.findAll()).thenReturn(new ArrayList<Item>());

        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            inventoryServices.itemDetails();
        });
        assertEquals("There is no items are Exist in the Inventory!",exception.getMessage());
    }

    @Test
    void testFindItem(){
        Item item  =  new Item("00001","itemNameOne","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);
        when(itemRepository.findById("00001")).thenReturn(Optional.of(item));

        Item actualResult = inventoryServices.findItem("00001");
        assertEquals("00001",actualResult.getItemId());
        assertEquals("01",actualResult.getStatus());
    }

    @Test
    void testFindItemWithInvalidItemId(){
        when(itemRepository.findById("00003")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,()->{
            inventoryServices.findItem("00003");
        });
        assertEquals("item with itemId:00003 not found",exception.getMessage());
    }

    @Test
    void testValidateItem(){
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("00001","itemNameOne","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true));
        itemList.add(new Item("00002","itemNameTwo","itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true));

        Item itemNotFound = new Item("00003","itemNameThree","itemThreeeDesc", "ItemThreeCategory", "itemThreeType", "01", "1999", true, true, true);
        Item itemFound = new Item("00001","itemNameOne","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);

        when(itemRepository.findAll()).thenReturn(itemList);

        assertTrue(inventoryServices.validateItem(itemNotFound));
        assertFalse(inventoryServices.validateItem(itemFound));
    }

    @Test
    void testCreateTest(){
        Item item= new Item("00001","itemOneName","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item actualResult = inventoryServices.createItem(item);

        assertEquals("00001",actualResult.getItemId());
        assertEquals("itemOneName",actualResult.getItemName());
    }

//    @Test
//    void testCreateItemWithExistItemId(){
//        List<Item> itemList = new ArrayList<>();
//        itemList.add(new Item("00001","itemNameOne","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true));
//        itemList.add(new Item("00002","itemNameTwo","itemTwoDesc", "itemTwoCategory", "itemOneType", "01", "3999", true, true, true));
//
//        Item item = new Item("00001","itemNameOne","itemOneDesc", "ItemOneCategory", "itemOneType", "01", "1999", true, true, true);
//        when(itemRepository.findAll()).thenReturn(itemList);
//
//        when(inventoryServices.validateItem(item)).thenReturn(false);
//
//
//        RuntimeException exception = assertThrows(RuntimeException.class,()->{
//            inventoryServices.createItem(item);
//        });
//        assertEquals("Item with ID 00001 already exists",exception.getMessage());
//
//    }



}
