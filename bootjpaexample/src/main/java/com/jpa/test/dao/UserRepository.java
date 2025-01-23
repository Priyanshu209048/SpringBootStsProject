package com.jpa.test.dao;

import java.util.List;

import org.aspectj.weaver.tools.Trace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jpa.test.entities.User;

public interface UserRepository extends CrudRepository<User, Integer>{

	public List<User> findByName(String name);
	
	public List<User> findByNameAndCity(String name, String city);
	
	//Here u is alies what we want to get
	@Query("Select u FROM User u")	//This is the way if we still want execute a query
	public List<User> getAlUser();
	
	@Query("Select u FROM User u WHERE u.name =:n and u.city =:c")
	public List<User> getUserByName(@Param("n") String name, @Param("c") String city);	//Here we map the data of n into name
	
	@Query(value = "select * from user", nativeQuery = true)
	public List<User> getUsers();
	 
}
