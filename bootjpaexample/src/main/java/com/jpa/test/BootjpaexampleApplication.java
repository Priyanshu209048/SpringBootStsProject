package com.jpa.test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jpa.test.dao.UserRepository;
import com.jpa.test.entities.User;

@SpringBootApplication
public class BootjpaexampleApplication {

	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(BootjpaexampleApplication.class, args);
		
		UserRepository userRepository = context.getBean(UserRepository.class);
		
//		User user = new User();
//		user.setName("Priyanshu Baral");
//		user.setCity("Delhi");
//		user.setStatus("Web Developer");
//		
//		User save = userRepository.save(user);
//		
//		System.out.println(save.toString());
		
		//Create object of user
//		User user1 = new User();
//		user1.setName("Priyanshu");
//		user1.setCity("Delhi");
//		user1.setStatus("Web Developer");
//		
//		User user2 = new User();
//		user2.setName("Rohan");
//		user2.setCity("Kanpur");
//		user2.setStatus("Python Developer");

		//Saving single data
//		User result = userRepository.save(user1);
		
//		List<User> users = List.of(user1, user2);
//		//Save multiple objects
//		Iterable<User> result = userRepository.saveAll(users);
//		
//		result.forEach(user->{
//			System.out.println(user);
//		});
//		
////		System.out.println("Saved User " + result);
//		System.out.println("Done");
		
		
		//Update the user
//		Optional<User> optional = userRepository.findById(2);
//		User user = optional.get();
//		
//		user.setName("Sohan");
//		User result = userRepository.save(user);
//		
//		System.out.println(result);
		
		
		//How to get the data
		//findById(id) - return Optional Containing your data
		
		//Get all data
		//Iterable<User> itr = userRepository.findAll();
//		Iterator<User> iterator = itr.iterator();
//		while(iterator.hasNext()) {
//			User user = iterator.next();
//			System.out.println(user);
//		}
		
		//OR using api
//		itr.forEach(new Consumer<User>() {
//
//			@Override
//			public void accept(User t) {
//				System.out.println(t);
//			}
//			
//		});
		
		//OR using lambda
		//itr.forEach(user->{System.out.println(user);});
		
		
		//Deleting the user element
//		userRepository.deleteById(2);
//		System.out.println("deleted");
		
//		Iterable<User> allusers = userRepository.findAll();
//		
//		allusers.forEach(user->System.out.println(user));
//		
//		userRepository.deleteAll(allusers);
		
		
		//Custom finder methods
//		List<User> users = userRepository.findByName("Priyanshu");
//		users.forEach(user->System.out.println(user));
		
//		List<User> users = userRepository.findByNameAndCity("Rohan", "Kanpur");
//		users.forEach(user->System.out.println(user));
		
		List<User> allUser = userRepository.getAlUser();
		allUser.forEach(u->System.out.println(u));
		
		System.out.println("--------------------------");
		
		List<User> userByName = userRepository.getUserByName("Priyanshu", "Delhi");
		userByName.forEach(u->System.out.println(u));
		
		System.out.println("--------------------------");
		
		List<User> getUser = userRepository.getUsers();
		getUser.forEach(u->System.out.println(u));
		
	}

}
