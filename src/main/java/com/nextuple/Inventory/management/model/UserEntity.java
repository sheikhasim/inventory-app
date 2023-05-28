package com.nextuple.Inventory.management.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

import java.util.Set;

@NoArgsConstructor
@Setter @Getter @Data
@Document(collection = "userEntity")
public class UserEntity {
    @Id @Indexed(unique = true)
    private String id;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String organizationId;
    private Set<Role> roles =  new HashSet<>();

    @DBRef
    Organization organization;


    public UserEntity(String id, String userName, String userEmail, String userPassword, String organizationId) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.organizationId = organizationId;
    }
}
