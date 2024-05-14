package nl.paul.productsapi.services;

import nl.paul.productsapi.model.Product;
import nl.paul.productsapi.repository.ProductRepository;
import nl.paul.productsapi.repository.specification.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    // Developer's note: I didn't have time to create unit tests for everything. I hope this displays enough to have a
    // talk about it. Someday I'd like to try the approach to write integration tests only, instead of unit testing. I
    // have used Cucumber in the past, but not recently.

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductSpecification productSpecification;

    @Mock
    RandomHelper randomHelper;

    @InjectMocks
    ProductService classUnderTest;

    @Captor
    private ArgumentCaptor<List<Product>> listCaptor;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"city"})
    void getProducts(final String searchValue) {
        if (StringUtils.hasText(searchValue)) {
            when(productSpecification.searchBy(anyString())).thenReturn(mock(Specification.class));
        }

        classUnderTest.getProducts(searchValue);

        if (StringUtils.hasText(searchValue)) {
            verify(productRepository, times(1)).findAll(any(Specification.class));

        } else {
            verify(productRepository, times(1)).findAll();
        }
    }

    @Test
    void saveNewProduct_verifyRepositoryCall() {
        classUnderTest.saveNewProduct(mock(Product.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void randomiseDiscountsOnProducts_whenNoDiscountsAdded_thenReturnListOfProductsWithoutDiscounts() {
        when(productRepository.findAll()).thenReturn(createProductList());
        when(randomHelper.getRandomBoolean()).thenReturn(false);

        classUnderTest.randomiseDiscountsOnProducts();

        verify(productRepository).saveAll(listCaptor.capture());
        List<Product> capturedList = listCaptor.getValue();

        assertEquals(2, capturedList.stream().filter(product -> product.getDiscountedPrice() == null).count());
    }

    @Test
    void randomiseDiscountsOnProducts_whenAllProductsDiscounted_thenReturnListOfProductsWithDiscounts() {
        when(productRepository.findAll()).thenReturn(createProductList());
        when(randomHelper.getRandomBoolean()).thenReturn(true);
        when(randomHelper.getDiscountPercentage()).thenReturn(BigDecimal.ONE);

        classUnderTest.randomiseDiscountsOnProducts();

        verify(productRepository).saveAll(listCaptor.capture());
        List<Product> capturedList = listCaptor.getValue();

        assertEquals(2, capturedList.stream().filter(product -> product.getDiscountedPrice() != null).count());
    }

    private List<Product> createProductList() {
        return List.of(Product.builder().name("Price 10 without discount").price(BigDecimal.TEN).build(),
                Product.builder().name("Price 10 with discount 2").price(BigDecimal.TEN).discountedPrice(BigDecimal.TWO).build());
    }

}