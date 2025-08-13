package com.vn.backend.controllers;

import com.vn.backend.dto.request.CartItemRequest;
import com.vn.backend.dto.response.CategoryResponse;
import com.vn.backend.dto.response.ImageUploadResponse;
import com.vn.backend.dto.response.MessageResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.repositories.ProductRepository;
import com.vn.backend.services.CartService;

import jakarta.mail.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(FeaturedCollectionsController.class);

    @Autowired
    private  CartService cartService;
    @PostMapping(value = "/validate")
    public ResponseData<MessageResponse> validateCart(List<CartItemRequest> listCartItemRequest) {
        logger.info("[IN] GET /api/carts/validate");
        var response = ResponseData.success(cartService.validateCart(listCartItemRequest));
        logger.info("[OUT] GET /api/carts/validate - Success");
        return response;
    }
}
