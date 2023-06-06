package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.SupplyNotFoundException;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@Service
public class InventoryServices {

    @Autowired
    static ItemRepository itemRepository;
    @Autowired
    static LocationRepository locationRepository;
    @Autowired
    static SupplyRepository supplyRepository;
    @Autowired
    static DemandRepository demandRepository;
    @Autowired
    static ThresholdRepository thresholdRepository;
    @Autowired
    static OrganizationRepository organizationRepository;
    @Autowired
    static TransactionRepository transactionRepository;

    public InventoryServices(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository,TransactionRepository transactionRepository){
        InventoryServices.itemRepository = itemRepository;
        InventoryServices.locationRepository = locationRepository;
        InventoryServices.supplyRepository = supplyRepository;
        InventoryServices.demandRepository = demandRepository;
        InventoryServices.thresholdRepository = thresholdRepository;
        InventoryServices.organizationRepository = organizationRepository;
        InventoryServices.transactionRepository = transactionRepository;
    }
    @Autowired
    private SupplyServices supplyServices;
    @Autowired
    private DemandService demandService;

    //////////////////////////////////////////////-->Availability Services<--///////////////////////////////////////////////////////////////////

    public Map<String,Object> AvailableQtyOfTheItemAtTheGivenLocation(String organizationId,String itemId, String locationId){
        Map<String,Object>result = new HashMap<>();

        int totalSupply = supplyServices.totalSupplyForItemAtParticularLocation(organizationId,itemId,locationId);

        int totalDemand = demandService.totalDemandForItemAtParticularLocation(organizationId,itemId,locationId);
        int availableQty = totalSupply-totalDemand;
        result.put("organizationId:",organizationId);
        result.put("itemId",itemId);
        result.put("locationId",locationId);
        result.put("totalSupply",totalSupply);
        result.put("totalDemand",totalDemand);
        result.put("availableQty",availableQty);
        int minThreshold = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(itemId,locationId,organizationId).get().getMinThreshold();
        if(availableQty<minThreshold)
            result.put("Sock level","Red");
        else if (availableQty == minThreshold) {
            result.put("Sock level","Yellow");
        }else{
            result.put("Sock level","Green");
        }
        return result;

    }


    public Map<String,Object> AvailableQtyOfTheItemAtAllTheLocation(String organizationId,String itemId){
        Map<String,Object>result = new HashMap<>();
        int totalSupply = supplyServices.totalSupplyForItemAtAllLocation(organizationId,itemId);

        int totalDemand = demandService.totalDemandForItemAtAllLocation(organizationId,itemId);

        result.put("organizationId",organizationId);
        result.put("itemId",itemId);
        result.put("totalSupply",totalSupply);
        result.put("totalDemand",totalDemand);
        result.put("locationId","NETWORK");
        result.put("availableQty",totalSupply-totalDemand);
        return result;

    }

 public List<Map<String,Object>> getDetailsOfAllItemWithAvailability(String organizationId){
     List<Map<String,Object>> deatailsList = new ArrayList<>();
     List<Item> ListOfItems = itemRepository.findByOrganizationId(organizationId);

     for(Item item: ListOfItems){
         Map<String,Object> checkSupply = AvailableQtyOfTheItemAtAllTheLocation(organizationId,item.getItemId());
         if(checkSupply.get("totalSupply").equals(0))
             continue;
         deatailsList.add(checkSupply);
     }
     return deatailsList;
 }

 public  Map<String, Integer> getTotalNumbersOfDashboard(String organizationId){
        Map<String,Integer> dataNumber = new HashMap<>();
        List<Item> itemList = itemRepository.findByOrganizationId(organizationId);
        List<Location> locationList = locationRepository.findAllByOrganizationId(organizationId);
        List<Supply> supplyList = supplyRepository.findAllByOrganizationId(organizationId);
        List<Demand> demandList = demandRepository.findAllByOrganizationId(organizationId);
        List<Item> ActiveItem = itemRepository.findByStatusAndOrganizationId(true,organizationId);

     Integer totalDemand = demandList.size();
     Integer totalActiveItem = ActiveItem.size();
     Integer totalItems = itemList.size();
     Integer totalLocation = locationList.size();
     Integer totalSupply = supplyList.size();

         dataNumber.put("TotalItems",totalItems);
         dataNumber.put("TotalSupply",totalSupply);
         dataNumber.put("TotalDemand",totalDemand);
         dataNumber.put("totalLocation",totalLocation);
         dataNumber.put("totalActiveItems",totalActiveItem);
        return dataNumber;
 }

//---------------------------------Transaction----------------------------------
   public void createTransaction(Transaction transaction){
    transactionRepository.save(transaction);
   }

   public  List<Transaction> getTransactions(String organizationId){
        List<Transaction>transactionList = new ArrayList<>();
        transactionList =  transactionRepository.findAllByOrganizationId(organizationId);
        if(transactionList.isEmpty())
            throw new SupplyNotFoundException("Transaction empty!");
        return transactionList;
   }

}


