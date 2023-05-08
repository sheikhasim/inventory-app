package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Item;
import com.nextuple.Inventory.management.model.Supply;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SupplyRepository extends MongoRepository<Supply,String> {

    List<Supply> findByItem(String itemId);

   Optional <List<Supply>> findByItemIdAndLocationId(String itemId, String locationId);

    List<Supply> findBySupplyTypeAndLocationId(String supplyType, String locationId);

}
