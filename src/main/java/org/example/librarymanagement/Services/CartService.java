package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.CartDTO;
import org.example.librarymanagement.Model.Requests.CartRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    Object getAllCartsByUser(String idReader);
    MessageResponse addToCart(CartRequest cartRequest);
    MessageResponse removeFromCart(CartRequest cartRequest);
}
