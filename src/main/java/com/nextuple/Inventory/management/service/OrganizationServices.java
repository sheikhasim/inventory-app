package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServices {
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

    public OrganizationServices(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository){
        OrganizationServices.itemRepository = itemRepository;
        OrganizationServices.locationRepository = locationRepository;
        OrganizationServices.supplyRepository = supplyRepository;
        OrganizationServices.demandRepository = demandRepository;
        OrganizationServices.thresholdRepository = thresholdRepository;
        OrganizationServices.organizationRepository = organizationRepository;
    }


    ////////////////////////////////////////Organization Services////////////////////////////////////////////////
    public  Organization createOrganization(Organization organization){
        Optional<Organization> organizationExist =organizationRepository.findById(organization.getOrganizationId());
        if(organizationRepository.existsById(organization.getOrganizationId())){
            throw new RuntimeException("Organization Id exist");
        }
        if(organizationRepository.existsByOrganizationName(organization.getOrganizationName())){
            throw new RuntimeException("Organization name exist");
        }

        if (organizationExist.isPresent()){
            throw new OrganizationNotFoundException("Organization already exist!");
        }
        return organizationRepository.save(organization);
    }

    public Organization findOrganizationById(String organizationId){
        Optional<Organization> OrganizationExist =organizationRepository.findById(organizationId);
        if (OrganizationExist.isEmpty())
            throw new OrganizationNotFoundException("Organization not found!");
        return OrganizationExist.get();
    }
    public Organization findByEmailId(String organizationEmail){
        Optional<Organization> OrganizationExist =organizationRepository.findByOrganizationEmail(organizationEmail);
        if (OrganizationExist.isEmpty())
            throw new OrganizationNotFoundException("Organization not found for "+ organizationEmail + " Email Id!");
        return OrganizationExist.get();
    }
    public List<Organization> findAllOrganization(){
        List<Organization>OrganizationList = organizationRepository.findAll();
        if(OrganizationList.isEmpty())
            throw new OrganizationNotFoundException("Organizations List Empty!");
        return OrganizationList;
    }

    public String deleteOrganization(String OrganizationId){
        Optional<Organization> OrganizationExist = organizationRepository.findById(OrganizationId);
        if(OrganizationExist.isEmpty())
            throw new OrganizationNotFoundException("Organization Not Found!");

        List<Item> items = itemRepository.findByOrganizationId(OrganizationId);

        if(items.isEmpty()){
            organizationRepository.deleteById(OrganizationId);
        } else {
            throw new OrganizationNotFoundException("Items found for  this  Organization!");
        }
        return "Organization deleted";
    }

    public Organization updateOrganization(Organization Organization, String OrganizationId){
        return organizationRepository.findById(OrganizationId).map(
                newOrganization->{
                    newOrganization.setOrganizationEmail(Organization.getOrganizationEmail());
                    newOrganization.setOrganizationName(Organization.getOrganizationName());
                    newOrganization.setPassword(Organization.getPassword());
                    return organizationRepository.save(newOrganization);
                }
        ).orElseThrow(()->new OrganizationNotFoundException("Organization not found!"));
    }


}
