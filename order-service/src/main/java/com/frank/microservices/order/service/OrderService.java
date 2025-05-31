package com.frank.microservices.order.service;

import com.frank.microservices.order.client.InventoryClient;
import com.frank.microservices.order.dto.OrderRequest;
import com.frank.microservices.order.model.Order;
import com.frank.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){
         var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());

         if(isProductInStock){
             Order order = new Order();
             order.setOrderNumber(UUID.randomUUID().toString());
             order.setPrice(orderRequest.price());
             order.setSkuCode(orderRequest.skuCode());
             order.setQuantity(orderRequest.quantity());

             orderRepository.save(order);
         }else{
             throw new RuntimeException("Product with SkuCode "+orderRequest.skuCode()+" is not in stock");
         }


    }


}
