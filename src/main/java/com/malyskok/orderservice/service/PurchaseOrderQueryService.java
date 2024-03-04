/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.malyskok.orderservice.service;

import com.malyskok.orderservice.client.ProductClient;
import com.malyskok.orderservice.client.UserClient;
import com.malyskok.orderservice.dto.PurchaseOrderRequestDto;
import com.malyskok.orderservice.dto.PurchaseOrderResponseDto;
import com.malyskok.orderservice.dto.RequestContext;
import com.malyskok.orderservice.repository.PurchaseOrderRepository;
import com.malyskok.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class PurchaseOrderQueryService {

    @Autowired
    PurchaseOrderRepository orderRepository;

    public Flux<PurchaseOrderResponseDto> findByUserId(Integer userId) {
        return Flux.fromStream(orderRepository.findByUserId(userId).stream()) //blocking
                .map(EntityDtoUtil::toPurchaseOderResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }
}