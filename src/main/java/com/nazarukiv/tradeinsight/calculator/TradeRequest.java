package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;

public class TradeRequest {

    private BigDecimal balance;

    private BigDecimal riskPercent;

    private BigDecimal entryPrice;

    private BigDecimal stopLossPrice;

    private String symbol;

    // for Spring
    public TradeRequest() {}

    // for CLI
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

    public BigDecimal getBalance() { return balance; }
    public BigDecimal getRiskPercent() { return riskPercent; }
    public BigDecimal getEntryPrice() { return entryPrice; }
    public BigDecimal getStopLossPrice() { return stopLossPrice; }
    public String getSymbol() { return symbol; }
}
