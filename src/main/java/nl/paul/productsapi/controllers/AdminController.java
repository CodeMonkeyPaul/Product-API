package nl.paul.productsapi.controllers;

import lombok.extern.slf4j.Slf4j;
import nl.paul.productsapi.model.Product;
import nl.paul.productsapi.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class AdminController {

    private final ProductService productService;

    public AdminController(final ProductService productService) {
        this.productService = productService;
    }

    // Developer's note: this class was created to mimic external changes being made to the products in this database.
    // In this dataset that might be third parties updating their offered products by regularly discounting them. As an
    // example I implemented a simple method applying new discounts for all products.

    @PutMapping("/admin/randomise-discounts")
    public ResponseEntity<List<Product>> randomiseDiscountsOnProducts() {
        log.info("Request received: 'PUT /admin/randomise-discounts'");
        return ResponseEntity.ok(productService.randomiseDiscountsOnProducts());
    }

}
