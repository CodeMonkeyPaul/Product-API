package nl.paul.productsapi.services;

import lombok.extern.slf4j.Slf4j;
import nl.paul.productsapi.model.Product;
import nl.paul.productsapi.repository.ProductRepository;
import nl.paul.productsapi.repository.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSpecification productSpecification;
    private final RandomHelper randomHelper;

    public ProductService(final ProductRepository productRepository,
                          final ProductSpecification productSpecification,
                          final RandomHelper randomHelper) {
        this.productRepository = productRepository;
        this.productSpecification = productSpecification;
        this.randomHelper = randomHelper;
    }

    public List<Product> getProducts(final String searchValue) {
        if (StringUtils.hasText(searchValue)) {
            Specification<Product> searchSpecification = productSpecification.searchBy(searchValue);
            return productRepository.findAll(searchSpecification);

        } else {
            return productRepository.findAll();
        }
    }

    public Product saveNewProduct(final Product product) {
        return productRepository.save(product);
    }

    public List<Product> randomiseDiscountsOnProducts() {
        List<Product> products = productRepository.findAll().stream().map(this::generateRandomDiscount).toList();
        return productRepository.saveAll(products);
    }

    private Product generateRandomDiscount(final Product product) {
        // Flipping a coin to see if the product will be discounted.
        if (randomHelper.getRandomBoolean()) {
            BigDecimal discountPercentage = randomHelper.getDiscountPercentage();
            product.setDiscountedPrice(product.getPrice().multiply(discountPercentage).setScale(2, RoundingMode.CEILING));

        } else if (product.getDiscountedPrice() != null) {
            product.setDiscountedPrice(null);
        }

        return product;
    }

}
