package com.bnta.grechimomarketplace.services;

import com.bnta.grechimomarketplace.models.*;
import com.bnta.grechimomarketplace.models.Buyer;
import com.bnta.grechimomarketplace.models.Order;
import com.bnta.grechimomarketplace.models.Product;
import com.bnta.grechimomarketplace.models.ShoppingCartDTO;
import com.bnta.grechimomarketplace.repositories.BuyerRepository;
import com.bnta.grechimomarketplace.repositories.OrderRepository;
import com.bnta.grechimomarketplace.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BuyerService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BuyerRepository buyerRepository;

    @Autowired
    OrderRepository orderRepository;
    public Buyer addNewBuyer(Buyer buyer){
        buyerRepository.save(buyer);
        return buyer;
    }


    public ShoppingCartDTO addProductToCart(long buyerId, long productId) {
        Product product = productRepository.findById(productId).get();
        Buyer buyer = buyerRepository.findById(buyerId).get();
        buyer.getCart().add(product);
        buyerRepository.save(buyer);

        List<Product> products = buyer.getCart();

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product p : products) {
            productDTOs.add(new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getDescription(), p.getSeller().getName()));
        }


        return new ShoppingCartDTO(productDTOs, buyer.getCartTotalValue(), buyer.getCart().size());
    }

    public ShoppingCartDTO getCart(long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId).get();

        List<Product> products = buyer.getCart();

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product p : products) {
            productDTOs.add(new ProductDTO(p.getId(), p.getName(), p.getPrice(), p.getDescription(), p.getSeller().getName()));
        }


        return new ShoppingCartDTO(productDTOs, buyer.getCartTotalValue(), buyer.getCart().size());
    }

    public Order placeOrder(long buyerId, String address) {
        Optional<Buyer> buyer = buyerRepository.findById(buyerId);
        List<Product> buyerCart = buyer.get().getCart();
        Order order = new Order(buyer.get(), address);
        orderRepository.save(order);
        return order;
    }

}
