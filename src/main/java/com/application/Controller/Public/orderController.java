package com.application.Controller.Public;

import com.application.Object.order;
import com.application.Object.order_status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller("order")
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("api/public/order")
public class orderController {

    @Autowired
    private com.application.Service.orderServicce orderService;

    @Autowired
    private com.application.Service.order_statusService order_statusService;

    @GetMapping("/orders")
    public ResponseEntity<List<order>> getAllOrders() {
        try {
            List<order> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<order> getOrderById(@PathVariable Long id) {
        try {
            order order = orderService.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<order> addOrder(@RequestBody order newOrder) {
        try {
            order createdOrder = orderService.addOrder(newOrder);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<order> updateOrder(@PathVariable Long id, @RequestBody order orderDetails) {
        try {
            order updatedOrder = orderService.updateOrder(id, orderDetails);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Long id) {
        try {
            boolean result = orderService.deleteOrder(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/orders/deliveryDate/{deliveryDate}")
    public ResponseEntity<List<order>> getOrdersByDeliveryDate(@PathVariable Date deliveryDate) {
        try {
            List<order> orders = orderService.getOrdersByDeliveryDate(deliveryDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/orders/phone/{phone}")
    public ResponseEntity<List<order>> getOrdersByPhone(@PathVariable String phone) {
        try {
            List<order> orders = orderService.getOrdersByPhone(phone);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/orderDate/{orderDate}")
    public ResponseEntity<List<order>> getOrdersByOrderDate(@PathVariable Date orderDate) {
        try {
            List<order> orders = orderService.getOrdersByOrderDate(orderDate);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<order>> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Long> orderIds = order_statusService.getIdsByStatus(status);
            List<order> orders = orderIds.stream()
                    .map(orderService::getOrderById)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/orders/status/{id}/{status}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @PathVariable String status) {
        try {
            order_statusService.updateStatus(id, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/orders/status/{id}")
    public ResponseEntity<String> getStatus(@PathVariable Long id) {
        try {
            String status = order_statusService.getStatus(id);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/orders/status/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        try {
            order_statusService.deleteStatus(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/orders/statuses")
    public ResponseEntity<List<order_status>> getAllStatuses() {
        try {
            List<order_status> statuses = order_statusService.getAllStatuses();
            return new ResponseEntity<>(statuses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/statuses/{status}")
    public ResponseEntity<List<order_status>> getStatusesByStatus(@PathVariable String status) {
        try {
            List<order_status> statuses = order_statusService.getStatusesByStatus(status);
            return new ResponseEntity<>(statuses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
