package com.example.crowdfunding.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserName(String userName);
    User findById(ObjectId userId);
    User findByEmail(String email);
}
