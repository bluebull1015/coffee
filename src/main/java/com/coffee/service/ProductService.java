package com.coffee.service;

import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository ;

    public List<Product> listProducts(){
        return this.productRepository.findAllByOrderByIdDesc() ;
    }

    public Product getProductById(Long id) {
        Optional<Product> product = this.productRepository.findById(id);

        // 해당 상품이 존재하지 않으면 null 값을 반환합니다.
        return product.orElse(null) ;
    }

    public void save(Product product) {
        this.productRepository.save(product) ;
    }
}
