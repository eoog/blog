package com.www.timesaleservice.controller.v1;

import com.www.timesaleservice.domain.Product;
import com.www.timesaleservice.dto.ProductDto;
import com.www.timesaleservice.dto.ProductDto.Response;
import com.www.timesaleservice.service.v1.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  // 상품 등록
  @PostMapping
  public ResponseEntity<ProductDto.Response> createProduct(@Valid @RequestBody ProductDto.CreateReqeust reqeust) {
    Product product = productService.createProduct(reqeust);
    return ResponseEntity.status(HttpStatus.CREATED).body(ProductDto.Response.from(product));
  }

  // 단일 상품 조회
  @GetMapping("/{productId}")
  public ResponseEntity<ProductDto.Response> getProduct(@PathVariable Long productId) {
    Product product = productService.getProduct(productId);
    return ResponseEntity.ok(ProductDto.Response.from(product));
  }

  // 전체 상품 조회
  @GetMapping
  public ResponseEntity<List<Response>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products.stream()
        .map(product -> ProductDto.Response.from(product))
        .collect(Collectors.toList())
    );
  }
}
