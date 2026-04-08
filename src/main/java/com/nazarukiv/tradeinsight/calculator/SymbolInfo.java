package com.nazarukiv.tradeinsight.calculator;

import java.math.BigDecimal;

public class SymbolInfo {

    private final String symbol;
    private final BigDecimal tickSize;
    private final BigDecimal tickValuePerLot;

    public SymbolInfo(String symbol, BigDecimal tickSize, BigDecimal tickValuePerLot) {
        this.symbol = symbol;
        this.tickSize = tickSize;
        this.tickValuePerLot = tickValuePerLot;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getTickSize() {
        return tickSize;
    }

    public BigDecimal getTickValuePerLot() {
        return tickValuePerLot;
    }

    // later will be expanded for other instruments
    public static SymbolInfo fromSymbol(String symbol) {
        switch (symbol.toUpperCase()) {
            case "EURUSD":
                return new SymbolInfo("EURUSD", new BigDecimal("0.0001"), new BigDecimal("10"));

            case "GBPUSD":
                return new SymbolInfo("GBPUSD", new BigDecimal("0.0001"), new BigDecimal("10"));

            case "USDCAD":
                return new SymbolInfo("USDCAD", new BigDecimal("0.0001"), null);

            case "XAUUSD":
                return new SymbolInfo("XAUUSD", new BigDecimal("0.01"), new BigDecimal("1"));

            case "XAGUSD":
                return new SymbolInfo("XAGUSD", new BigDecimal("0.001"), new BigDecimal("0.5"));

            case "GER40":
                return new SymbolInfo("GER40", new BigDecimal("1"), new BigDecimal("25"));

            case "NDX100":
                return new SymbolInfo("NDX100", new BigDecimal("1"), new BigDecimal("20"));

            default:
                throw new IllegalArgumentException("Unsupported symbol: " + symbol);
        }
    }
}