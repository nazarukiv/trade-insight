package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PositionSizeCalculator {
    BigDecimal eurUsdRate = new BigDecimal("1.08"); // TODO: replace with real-time EURUSD rate (API later)

    public TradeResult calculate(TradeRequest request) {

        if (request.getEntryPrice().compareTo(request.getStopLossPrice()) == 0) {
            throw new IllegalArgumentException("Entry price and stop loss price cannot be equal");
        }

        SymbolInfo symbolInfo = SymbolInfo.fromSymbol(request.getSymbol());

        BigDecimal riskAmount = request.getBalance()
                .multiply(request.getRiskPercent())
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);

        BigDecimal priceDistance = request.getEntryPrice()
                .subtract(request.getStopLossPrice())
                .abs();

        BigDecimal stopLossTicks = priceDistance
                .divide(symbolInfo.getTickSize(), 10, RoundingMode.HALF_UP);

        BigDecimal tickValuePerLot;

        if (request.getSymbol().equalsIgnoreCase("USDCAD")) {

            tickValuePerLot = new BigDecimal("10")
                    .divide(request.getEntryPrice(), 10, RoundingMode.HALF_UP);

        } else if (request.getSymbol().equalsIgnoreCase("GER40")) {

            tickValuePerLot = symbolInfo.getTickValuePerLot()
                    .multiply(eurUsdRate);

        } else {

            tickValuePerLot = symbolInfo.getTickValuePerLot();
        }

        BigDecimal moneyLostPerLot = stopLossTicks.multiply(tickValuePerLot);

        BigDecimal lotSize = riskAmount
                .divide(moneyLostPerLot, 2, RoundingMode.HALF_UP);

        return new TradeResult(
                symbolInfo.getSymbol(),
                riskAmount.setScale(2, RoundingMode.HALF_UP),
                stopLossTicks.setScale(1, RoundingMode.HALF_UP),
                lotSize.setScale(2, RoundingMode.HALF_UP)
        );
    }
}