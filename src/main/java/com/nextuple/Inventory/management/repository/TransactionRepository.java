package com.nextuple.Inventory.management.repository;

import com.nextuple.Inventory.management.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    List<Transaction> findAllByOrganizationId(String organizationId);
}
