package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Threshold;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ThresholdRepository extends MongoRepository<Threshold,String> {
    Optional<Threshold> findByItemIdAndLocationId(String itemId, String locationId);
}
