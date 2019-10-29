package com.medvedkov.paypal.repository;

import org.springframework.data.repository.CrudRepository;

import com.medvedkov.paypal.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}