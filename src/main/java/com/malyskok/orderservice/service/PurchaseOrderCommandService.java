/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.malyskok.orderservice.service;

import ch.qos.logback.core.util.FixedDelay;
import com.malyskok.orderservice.client.ProductClient;
import com.malyskok.orderservice.client.UserClient;
import com.malyskok.orderservice.dto.PurchaseOrderRequestDto;
import com.malyskok.orderservice.dto.PurchaseOrderResponseDto;
import com.malyskok.orderservice.dto.RequestContext;
import com.malyskok.orderservice.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.malyskok.orderservice.util.EntityDtoUtil;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class PurchaseOrderCommandService {

    @Autowired
    PurchaseOrderRepository orderRepository;

    @Autowired
    ProductClient productClient;

    @Autowired
    UserClient userClient;

    public Mono<PurchaseOrderResponseDto> order(Mono<PurchaseOrderRequestDto> orderMono) {
        return orderMono
                .map(RequestContext::new)
                .flatMap(this::callProductService)
                .doOnNext(EntityDtoUtil::setTransactionRequest)
                .flatMap(this::callUserService)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(this.orderRepository::save) //blocking
                .map(EntityDtoUtil::toPurchaseOderResponse)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<RequestContext> callProductService(RequestContext requestContext) {
        return productClient.getProductById(
                        requestContext.getPurchaseOrderRequestDto().getProductId())
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .doOnNext(requestContext::setProductDto)
                .thenReturn(requestContext);
    }

    private Mono<RequestContext> callUserService(RequestContext requestContext) {
        return userClient.authorizeTransaction(requestContext.getTransactionRequestDto())
                .doOnNext(requestContext::setTransactionResponseDto)
                .thenReturn(requestContext);
    }
}