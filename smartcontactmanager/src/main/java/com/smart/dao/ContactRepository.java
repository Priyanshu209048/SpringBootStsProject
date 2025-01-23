package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.smart.entities.Contact;
import com.smart.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

	/*
	 * A page is a sublist of a list of objects. It allows gain information about
	 * the position of it in the containing entire list.
	 */
	@Query("Select c from Contact as c where c.user.id =:userId")
	//It contains current page and contacts per page
	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pePageable);
	
	@Modifying
	@Transactional
	@Query("delete from Contact c where c.cId =:id")
	public void deleteContactById(@Param("id")int id);
	
	//Search
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
}
