package com.nexio.domain;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;


@CacheConfig(cacheNames = "users")
@SuppressWarnings("unchecked")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Cacheable(key = "#p0")
    Customer findByName(String name);

    @CachePut(key = "#p0.name")
    Customer save(Customer user);

    @CacheEvict(key = "#p0.name")
    void delete(Customer user);

//    @Cacheable(value="usersCache", key="{ #user.age, #user.name}")
//    Customer findCustomer(Customer user);
}
