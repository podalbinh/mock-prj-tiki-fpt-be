package com.vn.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vn.backend.dto.request.CartItemRequest;
import com.vn.backend.dto.response.MessageResponse;
import com.vn.backend.entities.Product;
import com.vn.backend.exceptions.InvalidDataException;
import com.vn.backend.exceptions.NotFoundException;
import com.vn.backend.repositories.ProductRepository;

@Service
public class CartService {
    @Autowired
    private ProductRepository productRepository;
    public MessageResponse validateCart(List<CartItemRequest> listCartItemRequest) {
        for (CartItemRequest cartItemRequest : listCartItemRequest) {
            Product product = productRepository.findById(cartItemRequest.getProductId()).get();
            if(product == null){
                throw new NotFoundException("Sản phẩm không tồn tại");
            }
            Boolean isValidate = productRepository.validateCart(cartItemRequest.getProductId(), cartItemRequest.getQuantity());
            if (!isValidate) {
                throw new InvalidDataException ("Sản phẩm " + product.getName() + " hiện không đủ số lượng");
            }
        }
        return new MessageResponse("Đã xác nhận giỏ hàng");
    }
}
