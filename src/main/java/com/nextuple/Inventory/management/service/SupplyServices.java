package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.*;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
public class SupplyServices {
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

    @Autowired
    private  static TransactionRepository transactionRepository;

    @Autowired
    private DemandService demandService;

    public SupplyServices(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository,TransactionRepository transactionRepository){
        SupplyServices.itemRepository = itemRepository;
        SupplyServices.locationRepository = locationRepository;
        SupplyServices.supplyRepository = supplyRepository;
        SupplyServices.demandRepository = demandRepository;
        SupplyServices.thresholdRepository = thresholdRepository;
        SupplyServices.organizationRepository = organizationRepository;
        SupplyServices.transactionRepository = transactionRepository;
    }

    //date time formatter
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

///////////////////////////////////////////////////////-->Supply Services<--///////////////////////////////////////////////////////////

    public List<Supply> supplyDetails(String organizationId) {
        List<Supply>supplyList = supplyRepository.findAllByOrganizationId(organizationId);
        if(supplyList.isEmpty())
            throw new SupplyAndDemandExistException("Supply is Empty!");
        else
            return supplyList;
    }

    public Supply findSupply(String organizationId,String supplyId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if(organization.isPresent()){
            Optional<Supply> foundSupply = supplyRepository.findByIdAndOrganizationId(supplyId,organizationId);
            if(foundSupply.isEmpty())
                throw new SupplyAndDemandExistException("Supply with supplyId:"+supplyId+" is not found");
            else
                return foundSupply.get();
        }else {
            throw new OrganizationNotFoundException("organization not found!");
        }
    }

    public List<Supply> findSupplyForItem(String organizationId,String itemId, String locationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if(organization.isPresent()){
            List<Supply> supplyList = supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId,locationId,organizationId);
            if(supplyList.isEmpty())
                throw new SupplyAndDemandExistException("Supplies Not Found!");
            return supplyList;
        }else{
            throw new OrganizationNotFoundException("organization not found!");
        }
    }

    public List<Supply> findSupplyWithSupplyTypeForSpecificLocation(String organizationId,String supplyType, String locationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if (organization.isPresent()){
            List<Supply> supplyList = supplyRepository.findBySupplyTypeAndLocationIdAndOrganizationId(supplyType,locationId,organizationId);
            if (supplyList.isEmpty())
                throw new SupplyAndDemandExistException("Supplies Not Found!");
            return supplyList;
        }else {
            throw new OrganizationNotFoundException("organization not found!");
        }
    }

    public Supply createSupply(String organizationId,Supply supply) {
        Optional<Item> item = itemRepository.findByItemIdAndOrganizationId(supply.getItemId(),organizationId);
        Optional<Organization> organization  = organizationRepository.findById(organizationId);
        Optional<Location>location = locationRepository.findByLocationIdAndOrganizationId(supply.getLocationId(),organizationId);
        Optional<Threshold> existThreshold = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(supply.getItemId(),supply.getLocationId(),organizationId);

        if(existThreshold.isEmpty())
            throw new ThresholdNotFoundException("Threshold not found!");

        int totalSupply = totalSupplyForItemAtParticularLocation(organizationId,supply.getItemId(),supply.getLocationId());
        int totalDemand = demandService.totalDemandForItemAtParticularLocation(organizationId,supply.getItemId(),supply.getLocationId());
        int availableQuantity = totalSupply-totalDemand;

        if((availableQuantity+supply.getQuantity()) > existThreshold.get().getMaxThreshold())
            throw new ThresholdNotFoundException("The supply quantity + available quantity is higher than the maximum threshold pleases reduce supply qty");

        supply.setOrganizationId(organizationId);
        supply.setOrganization(organization.get());
        supply.setItem(item.get());
        supply.setLocation(location.get());

        //check given supplyTypes Exist Or Not in Supply Types (Note: if organization enters supply types in lowerCase convert it into upperCase)
        try {
            supply.setSupplyType(supply.getSupplyType().toUpperCase());
            Supply.existSupplyTypes.valueOf(supply.getSupplyType());

        }catch (IllegalArgumentException e){
            throw new SupplyNotFoundException("Supply Type doesn't Exist!");
        }
        supplyRepository.save(supply);

        //create transaction
        Transaction transaction = new Transaction();
        transaction.setItemId(supply.getItemId());
        transaction.setLocationId(supply.getLocationId());
        transaction.setType("Supply");
        transaction.setOrganizationId(organizationId);
        transaction.setDate(dtf.format(now));
        transaction.setQuantity(supply.getQuantity());
        transactionRepository.save(transaction);
        return supply;
    }

    public String deleteSupply(String organizationId,String supplyId){
        Optional<Organization>organization = organizationRepository.findById(organizationId);
        if(organization.isPresent()){
            Optional<Supply> supply = supplyRepository.findByIdAndOrganizationId(supplyId, organizationId);
            if (supply.isEmpty())
                throw new SupplyNotFoundException("Supply Not Found!");
            supplyRepository.deleteById(supplyId);
            return "Supply deleted Successfully!"; //this string is not displaying in response body take care of it
        }else{
            throw new OrganizationNotFoundException("organization not found!");
        }
    }

    public Supply updateSupply(String organizationId, String supplyId, Supply supply){
        return supplyRepository.findByIdAndOrganizationId(supplyId,organizationId).map(
                newSupply->{
                    newSupply.setItemId(supply.getItemId());
                    newSupply.setLocationId(supply.getLocationId());
                    // redundant  code u can reduce it by making another fun for this (repeated in createSupply)
                    try {
                        supply.setSupplyType(supply.getSupplyType().toUpperCase());
                        Supply.existSupplyTypes.valueOf(supply.getSupplyType());

                    }catch (IllegalArgumentException e){
                        throw new SupplyNotFoundException("Supply Type doesn't Exist!");
                    }
                    newSupply.setSupplyType(supply.getSupplyType());
                    int quantity = newSupply.getQuantity();
                    newSupply.setQuantity(supply.getQuantity());

                    Optional<Item> newItem = itemRepository.findByItemIdAndOrganizationId(supply.getItemId(),organizationId);

                    if(newItem.isEmpty())
                        throw new ItemNotFoundException("Item with itemId:"+supply.getItemId()+" Not Found!");
                    newSupply.setItem(newItem.get());
                    Optional<Location> newLocation = locationRepository.findByLocationIdAndOrganizationId(supply.getLocationId(),organizationId);

                    if (newLocation.isEmpty())
                        throw new LocationNotFoundException("Location with locationId:"+supply.getLocationId()+" Not found!");
                    newSupply.setLocation(newLocation.get());

                    //create transaction
                    Transaction transaction = new Transaction();
                    transaction.setItemId(supply.getItemId());
                    transaction.setLocationId(supply.getLocationId());
                    transaction.setType("Supply");
                    transaction.setOrganizationId(organizationId);
                    transaction.setDate(dtf.format(now));

                    transaction.setQuantity(supply.getQuantity()-quantity);
                    transactionRepository.save(transaction);
                    return supplyRepository.save(newSupply);
                }
        ).orElseThrow(()->new SupplyNotFoundException("Supply not Found"));
    }

    public int totalSupplyForItemAtParticularLocation(String organizationId, String itemId, String locationId){
        int totalSupply = 0;
        List<Supply> supplyList = supplyRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId,organizationId);
        if(supplyList.isEmpty())
           return 0;
        for (Supply supply:supplyList) {
            totalSupply+=supply.getQuantity();
            System.out.println(supply);
        }
        return totalSupply;
    }
    public int totalSupplyForItemAtAllLocation(String organizationId, String itemId){
        int totalSupply = 0;
        List<Supply> supplyList = supplyRepository.findByItemIdAndOrganizationId(itemId,organizationId);

        for (Supply supply:supplyList) {
            totalSupply+=supply.getQuantity();
        }
        return totalSupply;
    }

}
