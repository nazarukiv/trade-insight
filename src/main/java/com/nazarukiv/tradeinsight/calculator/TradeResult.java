package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;


//what calculator will return
public class TradeResult {

    private final String symbol;
    private final BigDecimal riskAmount;
    private final BigDecimal stopLossTicks;
    private final BigDecimal lotSize;

    public TradeResult(String symbol, BigDecimal riskAmount, BigDecimal stopLossTicks, BigDecimal lotSize) {
        this.symbol = symbol;
        this.riskAmount = riskAmount;
        this.stopLossTicks = stopLossTicks;
        this.lotSize = lotSize;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getRiskAmount() {
        return riskAmount;
    }

    public BigDecimal getStopLossTicks() {
        return stopLossTicks;
    }

    public BigDecimal getLotSize() {
        return lotSize;
    }
}