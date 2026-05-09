package com.nazarukiv.tradeinsight;

import com.nazarukiv.tradeinsight.calculator.PositionSizeCalculator;
import com.nazarukiv.tradeinsight.calculator.TradeRequest;
import com.nazarukiv.tradeinsight.calculator.TradeResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TradeInsightApplicationTests {

    @Test
    void calculatesPositionSize() {
        PositionSizeCalculator calculator = new PositionSizeCalculator();
        TradeResult result = calculator.calculate(
                new TradeRequest(
                        new BigDecimal("1000"),
                        new BigDecimal("1"),
                        new BigDecimal("1.1000"),
                        new BigDecimal("1.0950"),
                        "EURUSD"
                )
        );

        assertEquals(new BigDecimal("10.00"), result.getRiskAmount());
        assertEquals(new BigDecimal("50.0"), result.getStopLossTicks());
        assertEquals(new BigDecimal("0.02"), result.getLotSize());
    }
}
