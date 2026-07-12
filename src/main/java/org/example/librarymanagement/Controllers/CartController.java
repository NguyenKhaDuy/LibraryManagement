package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.Requests.CartRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping(value = "/api/reader/cart/idReader={idReader}")
    public ResponseEntity<Object> getAllCartByReader(@PathVariable String idReader) {
        Object result = cartService.getAllCartsByUser(idReader);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/reader/cart")
    public ResponseEntity<Object> addToCart(@RequestBody CartRequest cartRequest){
        MessageResponse messageResponse = cartService.addToCart(cartRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/reader/cart")
    public ResponseEntity<Object> removeFromCart(@RequestBody CartRequest cartRequest){
        MessageResponse messageResponse = cartService.removeFromCart(cartRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
