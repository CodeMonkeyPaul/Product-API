package nl.paul.productsapi.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import nl.paul.productsapi.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductSpecification {

    // Developer's note: In microservices with simple data models that have basic CRUD functionalities I'd prefer to stay
    // away from Specification's because they introduce a significant amount of complexity to the code. I do use them occasionally
    // for use cases such as described in this assessment (searching through multiple fields) or complex joins.

    public Specification<Product> searchBy(final String keyword) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
            );
        };
    }

}
