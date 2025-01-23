package com.smart.controller;

import java.security.Principal;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.*;
import com.smart.dao.MyOrderRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.MyOrder;

@Controller
@RequestMapping("/user")
public class PaymentController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MyOrderRepository myOrderRepository;
	
	//Creating order for payment
	@PostMapping("/create_order")
	@ResponseBody	//I want to return only string not to return view
	public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws Exception {
		
 System.out.println("Create order function is executed");

		System.out.println(data);
		
		int amt = Integer.parseInt(data.get("amount").toString());
		
		//We can also use var to declare variable which was introduced in java 10, also it detects the data type automatically
		var client = new RazorpayClient("rzp_test_unGf0fgN6itTxn", "VqfNjxIbxKO3vRC0oRsAc3xE");
		
		JSONObject ob = new JSONObject();
		ob.put("amount", amt*100); //We use *100 because we want to convert paisa into rupees
		ob.put("currency", "INR");
		ob.put("receipt", "txn_12345");
		
		//Creating new order
		Order order = client.orders.create(ob);
		System.out.println(order);
		
		//We can also save this into our database
		MyOrder myOrder = new MyOrder();
		
		myOrder.setAmount(order.get("amount")+"");
		myOrder.setOrderId(order.get("id"));
		myOrder.setPaymentId(null);
		myOrder.setStatus("created");
		myOrder.setUser(this.userRepository.getUserByUserName(principal.getName()));
		myOrder.setReceipt(order.get("receipt"));
		
		this.myOrderRepository.save(myOrder);
		
		return order.toString();
	}
	
	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data){
		
		MyOrder myOrder = this.myOrderRepository.findByOrderId(data.get("order_id").toString());
		
		myOrder.setPaymentId(data.get("payment_id").toString());
		myOrder.setStatus(data.get("status").toString());
		
		this.myOrderRepository.save(myOrder);
		
		System.out.println(data);
		return ResponseEntity.ok(Map.of("msg", "updated"));
	}
	
}
