package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;

public class SymbolInfo {

    private final String symbol;
    private final BigDecimal pipSize;
    private final BigDecimal pipValuePerLot;

    public SymbolInfo(String symbol, BigDecimal pipSize, BigDecimal pipValuePerLot) {
        this.symbol = symbol;
        this.pipSize = pipSize;
        this.pipValuePerLot = pipValuePerLot;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPipSize() {
        return pipSize;
    }

    public BigDecimal getPipValuePerLot() {
        return pipValuePerLot;
    }

    //later will be expand for other things like GER40(other indices) and metals
    public static SymbolInfo fromSymbol(String symbol) {
        switch (symbol.toUpperCase()) {
            case "EURUSD":
                return new SymbolInfo("EURUSD", new BigDecimal("0.0001"), new BigDecimal("10"));
            case "GBPUSD":
                return new SymbolInfo("GBPUSD", new BigDecimal("0.0001"), new BigDecimal("10"));
            case "USDCAD":
                return new SymbolInfo("USDCAD", new BigDecimal("0.0001"), null);
            default:
                throw new IllegalArgumentException("Unsupported symbol: " + symbol);
        }
    }
}