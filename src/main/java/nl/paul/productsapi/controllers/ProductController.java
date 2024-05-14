package nl.paul.productsapi.controllers;

import lombok.extern.slf4j.Slf4j;
import nl.paul.productsapi.model.Product;
import nl.paul.productsapi.model.validation.OnCreate;
import nl.paul.productsapi.model.validation.OnUpdate;
import nl.paul.productsapi.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    // Developer's note: fetching all products or limiting the results based on provided search query. Due to the limit of
    // this assessment I wasn't able to do something with caching or save its context. If the frequency of these requests
    // are high, or the query would become complex (as requested) I would definitely consider caching the answer from the
    // productService layer to avoid going to the database every time.
    // If the price was time sensitive I would store the context. For instance when the discount was customer bound and
    // only valid for a specific time.
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "search", required = false) final String search) {
        log.info("Request received: 'GET /product', with optional search value: [{}]", search);
        List<Product> result = productService.getProducts(search);
        log.info("Found [{}] items for received request", result.size());

        return ResponseEntity.ok(result);
    }

    // Developer's note: pushing data into this service (for instance a new or updated product). Depending on the use-case
    // this could be done differently. I suppose that clients could do some sort of request which has to be assessed by
    // moderators (or automated). These requests could be stored perhaps in a queue and within the UI be shown to approve
    // or adjusted. Then finally upon approval it would be received here.
    @PostMapping
    public ResponseEntity<Product> addNewProduct(@Validated(OnCreate.class) @RequestBody final Product product) {
        log.info("Request received: 'POST /product', with new product by name [{}]", product.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveNewProduct(product));
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@Validated(OnUpdate.class) @RequestBody final Product product) {
        log.info("Request received: 'PUT /product', for product by id [{}]", product.getId());
        return ResponseEntity.ok(productService.saveNewProduct(product));
    }

}
