package com.application.Service;

import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.application.Repository.orderRepository;
import com.application.Object.order;
import com.application.Repository.order_statusRepository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import com.application.Object.OrderHistoryDTO;
@Service
public class orderServicce {

    @Autowired
    private orderRepository orderRepository;
    @Autowired
    private order_statusService order_statusService;


    public order addOrder(order newOrder) {
        newOrder.setOderDate(new Date());
        newOrder.setOderTime(new Time(System.currentTimeMillis()));
        order savedOrder = orderRepository.save(newOrder);
        order_statusService.addStatus(savedOrder.getId());
        return savedOrder;
    }

    public order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public order updateOrder(Long id, order orderDetails) {
        order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setPhone(orderDetails.getPhone());
            order.setQuantity(orderDetails.getQuantity());
            order.setOderDate(orderDetails.getOderDate());
            order.setOderTime(orderDetails.getOderTime());
            order.setDeliveryDate(orderDetails.getDeliveryDate());
            order.setTotalAmount(orderDetails.getTotalAmount());
            return orderRepository.save(order);
        }
        return null;
    }

    public boolean deleteOrder(Long id) {
        order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<order> getOrdersByDeliveryDate(Date deliveryDate) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getDeliveryDate().equals(deliveryDate))
                .collect(Collectors.toList());
    }

    public List<order> getOrdersByOrderDate(Date orderDate) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOderDate().equals(orderDate))
                .collect(Collectors.toList());
    }

    public List<order> getOrdersByPhone(String phone) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getPhone().equals(phone))
                .collect(Collectors.toList());
    }

    public List<order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Map<String, Integer> getFutureForecast(int days) {
        // Get current date
        LocalDate today = LocalDate.now();
        
        // Create a sorted map to store the forecast (date -> quantity)
        Map<String, Integer> forecast = new TreeMap<>();
        
        // Initialize all dates with zero
        for (int i = 0; i < days; i++) {
            LocalDate date = today.plusDays(i);
            forecast.put(date.toString(), 0);
        }
        
        // Get all future orders with status "placed" or "processing"
        Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.plusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<order> futureOrders = orderRepository.findFutureOrdersForForecast(startDate, endDate);
        
        // Process each order
        for (order o : futureOrders) {
            // Convert delivery date to LocalDate
            LocalDate deliveryDate = o.getDeliveryDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
            
            // Add the quantity to the respective date
            String dateKey = deliveryDate.toString();
            if (forecast.containsKey(dateKey)) {
                forecast.put(dateKey, forecast.get(dateKey) + o.getQuantity());
            }
        }
        
        return forecast;
    }

    public List<OrderHistoryDTO> getUserOrderHistory(String phone) {
        List<order> orders = orderRepository.findOrdersByPhoneOrderByDateDesc(phone);
        List<OrderHistoryDTO> orderHistory = new ArrayList<>();
        
        for (order order : orders) {
            String status = order_statusService.getStatus(order.getId());
            orderHistory.add(new OrderHistoryDTO(order, status));
        }
        
        return orderHistory;
    }

}
