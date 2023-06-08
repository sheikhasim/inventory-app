package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.ItemNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.model.Supply;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
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
    @Autowired
    private static OrganizationRepository organizationRepository;

    public ItemService(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository){
        ItemService.itemRepository = itemRepository;
        ItemService.locationRepository = locationRepository;
        ItemService.supplyRepository = supplyRepository;
        ItemService.demandRepository = demandRepository;
        ItemService.thresholdRepository = thresholdRepository;
        ItemService.organizationRepository = organizationRepository;
    }

    //---------------------------------Item Service------------------------------------
    //Basic: Return all the items from item table
    public List<Item> itemDetails(String organizationId) {
        List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
        if(itemList.isEmpty())
            throw new ItemNotFoundException("There is no items are Exist in the Inventory!");
        return itemList;
    }
    public List<Item> getActiveItems(String organizationId) {
        List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
        if(itemList.isEmpty())
            throw new ItemNotFoundException("There is no items are Exist in the Inventory!");

        return itemList.stream()
                .filter(Item::isStatus).toList();
    }


    //Return the details of specific itemId
    public Item findItem(String itemId, String organizationId) {
        System.out.println(itemId);
        Optional<Item> optionalItem = itemRepository.findByItemIdAndOrganizationId(itemId,organizationId);
        if(optionalItem.isEmpty())
            throw new ItemNotFoundException("item with itemId:"+itemId+" not found");
        return optionalItem.get();
    }

    /* Create an item in the item table.
    New record should not be created if itemId already exists */
    public Item createItem(Item item,String organizationId) {
        Organization organizationExist = organizationRepository.findById(organizationId).orElseThrow(()->new OrganizationNotFoundException("organization not found for Id:"+organizationId));
            item.setItemId(organizationId+"_"+item.getItemId());//adding organizationId as prefix to the itemId
            item.setOrganization(organizationExist);
            item.setOrganizationId(organizationId);
            if (validateItem(item,organizationId)) return itemRepository.save(item);
            else throw new ItemNotFoundException("Item with ID " + item.getItemId() + " already exists");
    }
    public boolean validateItem(Item item,String organizationId) {
        List<Item> dbItemsList = itemRepository.findByOrganizationId(organizationId);
        for (Item itemFromDb : dbItemsList) {
            if (item.getItemId().equals(itemFromDb.getItemId())) {
                return false;
            }
        }
        return true;
    }

    //Delete only if no supply or demand exists for this item
    public String deleteItemIfNotPresentInReferenceCollection(String itemId,String organizationId) throws RuntimeException {
        Optional<Item> itemOptional = itemRepository.findByItemIdAndOrganizationId(itemId,organizationId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            List<Supply> supplyReference = supplyRepository.findByItemIdAndOrganizationId(itemId,organizationId);
            List<Demand> demandReference = demandRepository.findByItemIdAndOrganizationId(itemId,organizationId);
            if (supplyReference.isEmpty() && demandReference.isEmpty()) {
                itemRepository.delete(item);
                return "item deleted";
            } else {
                throw new SupplyAndDemandExistException("Item Exist with supply or demand");
            }
        } else {
            throw new ItemNotFoundException("Item with itemId:"+itemId+" not found!");
        }
    }

    //    Update the existing item (Handle Item Not found)
    public  Item updateItem(String organizationId, Item item, String itemId){
        return itemRepository.findByItemIdAndOrganizationId(itemId,organizationId).map(
                        newItem->{
                            newItem.setItemName(item.getItemName());
                            newItem.setItemDescription(item.getItemDescription());
                            newItem.setCategory(item.getCategory());
                            newItem.setPrice(item.getPrice());
                            newItem.setType(item.getType());
                            newItem.setStatus(item.isStatus());
                            newItem.setDeliveryAllowed(item.isDeliveryAllowed());
                            newItem.setPickupAllowed(item.isPickupAllowed());
                            newItem.setShippingAllowed(item.isShippingAllowed());
                            return itemRepository.save(newItem);
                        }
                )
                .orElseThrow(() -> new ItemNotFoundException("Item with itemId:"+itemId+" Not found"));

    }



}
