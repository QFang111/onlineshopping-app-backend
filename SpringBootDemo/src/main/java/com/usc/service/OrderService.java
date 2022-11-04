package com.usc.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.usc.beans.Order;
import com.usc.beans.OrderProduct;
import com.usc.beans.Product;
import com.usc.dao.OrderDao;
import com.usc.dao.ProductDao;
import com.usc.dao.UserDao;
import com.usc.http.Response;

@Service
@Transactional
public class OrderService {
	@Autowired
	OrderDao orderDao;

	@Autowired
	ProductDao productDao;

	@Autowired
	UserDao userDao;

	public Response addOrder(Order order, Authentication authentication) {
		order.setPurchase_Date(order.getPurchase_Date());
		List<OrderProduct> purchases = order.getPurchases();
		purchases.forEach((orderProduct) -> {
			Product name = orderProduct.getProduct();
			Product product = (Product) productDao.findByProductname(orderProduct.getProduct().getProductname());
			orderProduct.setProduct(product);
			orderProduct.setOrder(order);
		});
		order.setUser(userDao.findByUsername(authentication.getName()));
		orderDao.save(order);
		// send to kafka for further processing
		order.setPurchases(purchases);
		// orderProcessService.processOrder(order);
		return new Response(true);
	}

	public Response updateOrder(Order order) {
		Order o = orderDao.findById(order.getId()).get();
		o.setPurchase_Date(o.getPurchase_Date());
		for (OrderProduct i : o.getPurchases()) {
			if (i.getQuantity() > i.getProduct().getStock()) {
				return new Response(false, "Not enough products in stock");
			}
		}
		o.setPurchases(o.getPurchases());
		orderDao.save(o);
		return new Response(true);

	}

	public Response deleteOrder(int id) {
		if (orderDao.findById(id) != null) {
			orderDao.deleteById(id);
			return new Response(true);
		} else {
			return new Response(false, "order is not found");
		}
	}
}