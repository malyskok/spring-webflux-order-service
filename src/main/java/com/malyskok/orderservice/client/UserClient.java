/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.malyskok.orderservice.client;

import com.malyskok.orderservice.dto.TransactionRequestDto;
import com.malyskok.orderservice.dto.TransactionResponseDto;
import com.malyskok.orderservice.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${user.service.url}") String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<TransactionResponseDto> authorizeTransaction(TransactionRequestDto transactionRequestDto) {
        return this.webClient
                .post()
                .uri("transaction")
                .bodyValue(transactionRequestDto)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class);
    }

    public Flux<UserDto> getAllUsers(){
        return this.webClient
                .get()
                .uri("all")
                .retrieve()
                .bodyToFlux(UserDto.class);
    }
}