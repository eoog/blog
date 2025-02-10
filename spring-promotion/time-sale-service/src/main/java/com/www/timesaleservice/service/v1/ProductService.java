package com.www.timesaleservice.service.v1;

import com.www.timesaleservice.domain.Product;
import com.www.timesaleservice.dto.ProductDto;
import com.www.timesaleservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    public Product createProduct(ProductDto.CreateReqeust reqeust) {
        Product product = Product.builder()
            .name(reqeust.getName())
            .price(reqeust.getPrice())
            .description(reqeust.getDescription())
            .build();

        return productRepository.save(product);
    }

    // 단일 상품 조회
    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다"));
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
