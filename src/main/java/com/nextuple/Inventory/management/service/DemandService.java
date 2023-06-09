package com.nextuple.Inventory.management.service;
import com.nextuple.Inventory.management.exception.*;
import com.nextuple.Inventory.management.model.*;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class DemandService {
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
    private static TransactionRepository transactionRepository;
    @Autowired
    private SupplyServices supplyServices;
    @Autowired
    private  InventoryServices inventoryServices;

    public DemandService(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository,TransactionRepository transactionRepository){
        DemandService.itemRepository = itemRepository;
        DemandService.locationRepository = locationRepository;
        DemandService.supplyRepository = supplyRepository;
        DemandService.demandRepository = demandRepository;
        DemandService.thresholdRepository = thresholdRepository;
        DemandService.organizationRepository = organizationRepository;
        DemandService.transactionRepository = transactionRepository;
    }

    //date time formatter
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    //////////////////////////////////////////////////-->Demand Services<--////////////////////////////////////////////////////////////////

    public List<Demand> demandDetails(String organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if(organization.isPresent()){
            List<Demand>demandList = demandRepository.findAllByOrganizationId(organizationId);
            if(demandList.isEmpty())
                throw new DemandNotFoundException("demand is Empty!");
            else
                return demandList;
        }else{
            throw new OrganizationNotFoundException("organization not found!");
        }
    }

    public Demand findDemand( String organizationId, String demandId) {
        Optional<Demand> foundDemand = demandRepository.findByIdAndOrganizationId(demandId,organizationId);
        if(foundDemand.isEmpty())
            throw new DemandNotFoundException("Demand with demandId:"+demandId+" is not found");
        else
            return foundDemand.get();
    }

    public List<Demand> findDemandForItem(String organizationId,String itemId, String locationId) {
        List<Demand> demandList = demandRepository.findByItemIdAndLocationIdAndOrganizationId(itemId,locationId,organizationId);
        if(demandList.isEmpty())
            throw new DemandNotFoundException("Demands Not Found!");

        return demandList;
    }

    public List<Demand> findDemandWithDemandTypeForSpecificLocation(String organizationId,String demandType, String locationId) {
        List<Demand> DemandList = demandRepository.findByDemandTypeAndLocationIdAndOrganizationId(demandType,locationId,organizationId);
        if (DemandList.isEmpty())
            throw new DemandNotFoundException("Demands Not Found!");
        return DemandList;
    }

    public Demand createDemand(String organizationId, Demand demand) {

        Optional<Organization> organization = organizationRepository.findById(organizationId);
        if(organization.isEmpty()){
            throw new OrganizationNotFoundException("Organization not found");
        }

        Optional<Threshold> existThreshold = thresholdRepository.findByItemIdAndLocationIdAndOrganizationId(demand.getItemId(),demand.getLocationId(),organizationId);
        if(existThreshold.isEmpty())
            throw new ThresholdNotFoundException("Threshold not found!");

        int totalSupply = supplyServices.totalSupplyForItemAtParticularLocation(organizationId,demand.getItemId(),demand.getLocationId());
        int totalDemand = totalDemandForItemAtParticularLocation(organizationId,demand.getItemId(),demand.getLocationId());
        int availableQty = totalSupply-totalDemand;


        if((availableQty-demand.getQuantity())<existThreshold.get().getMinThreshold())
            throw new DemandNotFoundException("The available quantity - given demand is lesser than min threshold please reduce demans");

        try {
            demand.setDemandType(demand.getDemandType().toUpperCase());
            Demand.existDemandTypes.valueOf(demand.getDemandType());
        }catch (IllegalArgumentException e){
            throw new DemandNotFoundException("demand Type doesn't Exist!");
        }

        demand.setItem(itemRepository.findById(demand.getItemId()).get());
        demand.setLocation(locationRepository.findById(demand.getLocationId()).get());
        demand.setOrganizationId(organizationId);
        demand.setOrganization(organization.get());
        demandRepository.save(demand);

        //create transaction
        Transaction transaction = new Transaction();
        transaction.setItemId(demand.getItemId());
        transaction.setLocationId(demand.getLocationId());
        transaction.setType("Demand");
        transaction.setOrganizationId(organizationId);
        transaction.setDate(dtf.format(now));
        transaction.setQuantity(demand.getQuantity());
        transactionRepository.save(transaction);
        return demand;
    }

    public String deleteDemand(String organizationId, String demandId){
        Optional<Demand> demand = demandRepository.findByIdAndOrganizationId(demandId,organizationId);
        if(demand.isEmpty())
            throw new DemandNotFoundException("Demand Not Found!");
        demandRepository.deleteById(demandId);
        return "Demand deleted Successfully!"; //this string is not displaying in response body take care of it
    }

    public Demand updateDemand(String organizationId, String demandId, Demand demand){
        return demandRepository.findByIdAndOrganizationId(demandId, organizationId).map(
                newDemand->{
                    newDemand.setItemId(demand.getItemId());
                    newDemand.setLocationId(demand.getLocationId());

                    newDemand.setDemandType(demand.getDemandType());
                    int quantity = newDemand.getQuantity();
                    newDemand.setQuantity(demand.getQuantity());

                    Optional<Item> newItem = itemRepository.findByItemIdAndOrganizationId(demand.getItemId(),organizationId);
                    if(newItem.isEmpty())
                        throw new ItemNotFoundException("Item with itemId:"+demand.getItemId()+" Not Found!");
                    newDemand.setItem(newItem.get());
                    Optional<Location> newLocation = locationRepository.findByLocationIdAndOrganizationId(demand.getLocationId(),organizationId);

                    if (newLocation.isEmpty())
                        throw new LocationNotFoundException("Location with locationId:"+demand.getLocationId()+" Not found!");
                    newDemand.setLocation(newLocation.get());

                    //create transaction
                    Transaction transaction = new Transaction();
                    transaction.setItemId(demand.getItemId());
                    transaction.setLocationId(demand.getLocationId());
                    transaction.setType("Demand");
                    transaction.setOrganizationId(organizationId);
                    transaction.setDate(dtf.format(now));
                    transaction.setQuantity(demand.getQuantity()-quantity);
                    transactionRepository.save(transaction);
                    return demandRepository.save(newDemand);
                }
        ).orElseThrow(()->new DemandNotFoundException("Demand not Found"));
    }



    public int totalDemandForItemAtAllLocation(String organizationId, String itemId){
        int totalDemand = 0;
        List<Demand> demandList = demandRepository.findByItemIdAndOrganizationId(itemId,organizationId);
        if(!demandList.isEmpty()) {
            for (Demand demand : demandList) {
                totalDemand += demand.getQuantity();

            }
        }
        return totalDemand;
    }
    public int totalDemandForItemAtParticularLocation(String organizationId, String itemId, String locationId){
        int totalDemand = 0;
        List<Demand> demandList = demandRepository.findByItemIdAndLocationIdAndOrganizationId(itemId, locationId, organizationId);

        if(!demandList.isEmpty()) {
            for (Demand demand : demandList) {
                totalDemand += demand.getQuantity();
                System.out.println(demand);
            }
        }
        return totalDemand;
    }

}
