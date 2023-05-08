package com.nextuple.Inventory.management.repository;
import com.nextuple.Inventory.management.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<Item,String>{


}
