package nl.paul.productsapi.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import nl.paul.productsapi.model.validation.OnCreate;
import nl.paul.productsapi.model.validation.OnUpdate;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    // Developer's note: I always separate my DTOs from Entities. Usually I take some library to do this like Mapstruct,
    // but sometimes (for complex mappings) I write them myself. Most of the time it's to avoid creating cyclic references
    // that cannot be translated into String (like JSON) and sent out of my endpoints. But also to reduce the amount of
    // information coming from the endpoint (for instance just sending back what is required in data overviews).

    @Null(groups = OnCreate.class, message = "The product ID must be null when creating a new product.")
    @NotNull(groups = OnUpdate.class, message = "The product ID cannot be null when updating an existing product.")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product name cannot be empty.")
    private String name;

    @NotNull(message = "Product category cannot be empty.")
    private String category;

    @NotNull(message = "Product location cannot be empty.")
    private String location;

    @NotNull(message = "Product description cannot be empty.")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Product price should be set (above 0).")
    private BigDecimal price;

    private BigDecimal discountedPrice;
    private String currency;

}
