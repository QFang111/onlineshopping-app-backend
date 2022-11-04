package com.usc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usc.beans.Order;
import com.usc.dao.OrderDao;
import com.usc.http.Response;
import com.usc.service.OrderService;

@RestController()
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	OrderDao orderDao;
	
	@Autowired
	OrderService orderService;

	@GetMapping
	public List<Order> getAllOrders() {
		return orderDao.findAll();
	}
	
	@GetMapping("/{id}")
	public Order getOrder(@PathVariable int id) {
		return orderDao.findById(id).get();
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	@PostMapping
	public Response placeOrder(@RequestBody Order order, Authentication authentication) {
		return orderService.addOrder(order, authentication);
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	@PutMapping
	public Response updateOrder(@RequestBody Order order) {
		return orderService.updateOrder(order);
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
	@DeleteMapping("/{id}")
	public Response deleteProduct(@PathVariable int id) {
		return orderService.deleteOrder(id);
	}
}