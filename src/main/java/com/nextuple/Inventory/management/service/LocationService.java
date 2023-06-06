package com.nextuple.Inventory.management.service;

import com.nextuple.Inventory.management.exception.LocationNotFoundException;
import com.nextuple.Inventory.management.exception.OrganizationNotFoundException;
import com.nextuple.Inventory.management.exception.SupplyAndDemandExistException;
import com.nextuple.Inventory.management.model.Demand;
import com.nextuple.Inventory.management.model.Location;
import com.nextuple.Inventory.management.model.Supply;
import com.nextuple.Inventory.management.model.Organization;
import com.nextuple.Inventory.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
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

    public LocationService(ItemRepository itemRepository, LocationRepository locationRepository, SupplyRepository supplyRepository, DemandRepository demandRepository, ThresholdRepository thresholdRepository, OrganizationRepository organizationRepository){
        LocationService.itemRepository = itemRepository;
        LocationService.locationRepository = locationRepository;
        LocationService.supplyRepository = supplyRepository;
        LocationService.demandRepository = demandRepository;
        LocationService.thresholdRepository = thresholdRepository;
        LocationService.organizationRepository = organizationRepository;
    }


    //////////////////////////////////////////////////////-->Location API<--//////////////////////////////////////////////////////
    public List<Location>locationDetails(String organizationId) {
        List<Location>locationList = locationRepository.findAllByOrganizationId(organizationId);
        if(locationList.isEmpty())
            throw new LocationNotFoundException("There is no Locations present in the Inventory details");
        return locationList;
    }

    public Location findLocation(String organizationId,String locationId) {
        Optional<Location> foundLocation = locationRepository.findByLocationIdAndOrganizationId(locationId,organizationId);
        if(foundLocation.isEmpty())
            throw new LocationNotFoundException("location with locationId:"+locationId+" is not found");
        return foundLocation.get();
    }

    public Location createLocation(String organizationId, Location location) {
        Optional<Organization>organizationExist = organizationRepository.findById(organizationId);
        if(organizationExist.isPresent()) {
            location.setLocationId(organizationId+"_"+location.getLocationId());
            location.setOrganizationId(organizationId);
            location.setOrganization(organizationExist.get());
            Optional<Location> foundLocation = locationRepository.findByLocationIdAndOrganizationId(location.getLocationId(), organizationId);
            if (foundLocation.isEmpty()) {
                locationRepository.save(location);
                return location;
            } else {
                throw new LocationNotFoundException("location with locationId:" + location.getLocationId() + " is already exist");
            }
        }else{
            throw new OrganizationNotFoundException("organization not found");
        }
    }

    public String deleteLocationIfNotPresentInReferenceCollection(String locationId,String organizationId) {
        Optional<Location> locationOptional = locationRepository.findByLocationIdAndOrganizationId(locationId,organizationId);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            List<Supply> supplyReference = supplyRepository.findByLocationIdAndOrganizationId(location.getLocationId(),organizationId);
            List<Demand> demandReference = demandRepository.findByLocationIdAndOrganizationId(location.getLocationId(),organizationId);
            if (supplyReference.isEmpty() && demandReference.isEmpty()) {
                locationRepository.delete(location);
                return "Location deleted!";
            } else {
                throw new SupplyAndDemandExistException("Location Exist with supply or demand");
            }
        } else {
            throw new LocationNotFoundException("Location with locationId:"+locationId+" not found!");
        }
    }

    public Optional<Location> updateLocation(Location location, String locationId,String organizationId) {
        return Optional.ofNullable(locationRepository.findByLocationIdAndOrganizationId(locationId,organizationId).map(
                newLocation -> {
                    newLocation.setLocationDesc(location.getLocationDesc());
                    newLocation.setLocationType(location.getLocationType());
                    newLocation.setPickupAllowed(location.isPickupAllowed());
                    newLocation.setDeliveryAllowed(location.isDeliveryAllowed());
                    newLocation.setShippingAllowed(location.isShippingAllowed());
                    newLocation.setAddressLine1(location.getAddressLine1());
                    newLocation.setAddressLine2(location.getAddressLine2());
                    newLocation.setAddressLine3(location.getAddressLine3());
                    newLocation.setCity(location.getCity());
                    newLocation.setState(location.getState());
                    newLocation.setCountry(location.getCountry());
                    newLocation.setPinCode(location.getPinCode());
                    return locationRepository.save(newLocation);
                }
        ).orElseThrow(() -> new LocationNotFoundException("location with:" + locationId + " is not found")));
    }

}
