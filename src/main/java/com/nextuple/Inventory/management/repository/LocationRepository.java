package com.nextuple.Inventory.management.repository;
import com.nextuple.Inventory.management.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<Location,String>{

}
