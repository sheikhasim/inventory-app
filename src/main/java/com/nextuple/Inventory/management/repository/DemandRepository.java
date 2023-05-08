package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Demand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DemandRepository extends MongoRepository<Demand,String> {
    List<Demand> findByItem(String itemId);

    List<Demand> findByItemIdAndLocationId(String itemId, String locationId);

    List<Demand> findByDemandTypeAndLocationId(String demandType, String locationId);
}
