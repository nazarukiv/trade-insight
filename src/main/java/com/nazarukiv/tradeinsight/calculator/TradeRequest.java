package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;


//input data for the trade
public class TradeRequest {

    //big decimal is immutable, that's why it's used for these variables.
    private final BigDecimal balance;
    private final BigDecimal riskPercent;
    private final BigDecimal entryPrice;
    private final BigDecimal stopLossPrice;
    private final String symbol;


    //constructor for the trade
    public TradeRequest(BigDecimal balance,
                        BigDecimal riskPercent,
                        BigDecimal entryPrice,
                        BigDecimal stopLossPrice,
                        String symbol) {

        this.balance = balance;
        this.riskPercent = riskPercent;
        this.entryPrice = entryPrice;
        this.stopLossPrice = stopLossPrice;
        this.symbol = symbol;
    }


    //getters
    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getRiskPercent() {
        return riskPercent;
    }

    public BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public BigDecimal getStopLossPrice() {
        return stopLossPrice;
    }

    public String getSymbol() {
        return symbol;
    }
}