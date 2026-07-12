package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.BooksEntity;
import org.example.librarymanagement.Entity.CartEntity;
import org.example.librarymanagement.Entity.ReadersEntity;
import org.example.librarymanagement.Model.DTO.CartDTO;
import org.example.librarymanagement.Model.DTO.CartDetailDTO;
import org.example.librarymanagement.Model.DTO.ImageDTO;
import org.example.librarymanagement.Model.Requests.CartRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.BookRepository;
import org.example.librarymanagement.Repository.CartRepository;
import org.example.librarymanagement.Repository.ReaderRepository;
import org.example.librarymanagement.Services.CartService;
import org.example.librarymanagement.Utils.ConvertByteToBase64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;


    @Override
    public Object getAllCartsByUser(String idReader) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse  dataResponse = new DataResponse();
        ReadersEntity readersEntity = null;
        try {
            readersEntity = readerRepository.findById(idReader).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        List<CartEntity> cartEntities = cartRepository.findByReadersEntity(readersEntity);
        List<CartDTO> cartDTOS = cartEntities.stream().map(cartEntity -> {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setIdCart(cartEntity.getIdCart());

            List<CartDetailDTO> cartDetailDTOS = cartEntity.getBooksEntities().stream().map(booksEntity -> {
                CartDetailDTO cartDetailDTO = new CartDetailDTO();
                modelMapper.map(booksEntity, cartDetailDTO);
                cartDetailDTO.setNameAuthor(booksEntity.getAuthorEntity().getFullName());

                //IMAGE
                List<ImageDTO> imageDTOS = booksEntity.getImageEntities().stream().map(imageEntity -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setIdImage(imageEntity.getIdImage());
                    imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                    return imageDTO;
                }).toList();

                cartDetailDTO.setImageDTOS(imageDTOS);

                return cartDetailDTO;
            }).toList();
            cartDTO.setCartDetailDTOS(cartDetailDTOS);

            return cartDTO;
        }).toList();

        dataResponse.setMessage("Success");
        dataResponse.setData(cartDTOS);
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public MessageResponse addToCart(CartRequest cartRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BooksEntity booksEntity = null;
        CartEntity cartEntity = null;
        try{
            booksEntity = bookRepository.findById(cartRequest.getIdBook()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such book");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try{
            cartEntity = cartRepository.findById(cartRequest.getIdCart()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such cart");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        cartEntity.getBooksEntities().add(booksEntity);
        cartRepository.save(cartEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse removeFromCart(CartRequest cartRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BooksEntity booksEntity = null;
        CartEntity cartEntity = null;
        try{
            booksEntity = bookRepository.findById(cartRequest.getIdBook()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such book");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try{
            cartEntity = cartRepository.findById(cartRequest.getIdCart()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such cart");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        cartEntity.getBooksEntities().remove(booksEntity);
        cartRepository.save(cartEntity);
        messageResponse.setStatus(HttpStatus.OK);
        messageResponse.setMessage("Success");
        return messageResponse;
    }
}
