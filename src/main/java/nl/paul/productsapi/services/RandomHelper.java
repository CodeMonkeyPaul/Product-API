package nl.paul.productsapi.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Service
public class RandomHelper {

    private final Random random = new Random();

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public BigDecimal getDiscountPercentage() {
        return BigDecimal.valueOf(random.nextInt(50) + 1).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
    }

}
