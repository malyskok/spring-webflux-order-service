/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package com.malyskok.orderservice.util;

import com.malyskok.orderservice.dto.*;
import com.malyskok.orderservice.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
    public static void setTransactionRequest(RequestContext requestContext) {
        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        transactionRequestDto.setAmount(requestContext.getProductDto().getPrice());
        requestContext.setTransactionRequestDto(transactionRequestDto);
    }

    public static PurchaseOrder getPurchaseOrder(RequestContext requestContext) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        purchaseOrder.setProductId(requestContext.getPurchaseOrderRequestDto().getProductId());
        purchaseOrder.setAmount(requestContext.getProductDto().getPrice());
        purchaseOrder.setStatus(
                TransactionStatus.APPROVED.equals(requestContext.getTransactionResponseDto().getStatus()) ?
                        OrderStatus.COMPLETED : OrderStatus.FAILED);
        return purchaseOrder;
    }

    public static PurchaseOrderResponseDto toPurchaseOderResponse(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDto responseDto = new PurchaseOrderResponseDto();
        BeanUtils.copyProperties(purchaseOrder, responseDto);
        responseDto.setOrderId(purchaseOrder.getId());
        return responseDto;
    }
}