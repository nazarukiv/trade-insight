package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;


//what calculator will return
public class TradeResult {

    private final String symbol;
    private final BigDecimal riskAmount;
    private final BigDecimal stopLossPips;
    private final BigDecimal lotSize;

    public TradeResult(String symbol, BigDecimal riskAmount, BigDecimal stopLossPips, BigDecimal lotSize) {
        this.symbol = symbol;
        this.riskAmount = riskAmount;
        this.stopLossPips = stopLossPips;
        this.lotSize = lotSize;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getRiskAmount() {
        return riskAmount;
    }

    public BigDecimal getStopLossPips() {
        return stopLossPips;
    }

    public BigDecimal getLotSize() {
        return lotSize;
    }
}