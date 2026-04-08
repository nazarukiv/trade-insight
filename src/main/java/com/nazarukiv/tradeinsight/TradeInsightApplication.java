package com.nazarukiv.tradeinsight;

import com.nazarukiv.tradeinsight.calculator.PositionSizeCalculator;
import com.nazarukiv.tradeinsight.calculator.TradeRequest;
import com.nazarukiv.tradeinsight.calculator.TradeResult;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class TradeInsightApplication {

    public static void main(String[] args) {

        PositionSizeCalculator calculator = new PositionSizeCalculator();

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.println("=== Position Size Calculator ===");

        System.out.print("Enter balance: ");
        BigDecimal balance = new BigDecimal(scanner.nextLine());

        System.out.print("Enter risk %: ");
        BigDecimal riskPercent = new BigDecimal(scanner.nextLine());

        System.out.print("Enter entry price: ");
        BigDecimal entryPrice = new BigDecimal(scanner.nextLine());

        System.out.print("Enter stop loss price: ");
        BigDecimal stopLoss = new BigDecimal(scanner.nextLine());

        System.out.print("Enter symbol (EURUSD / GBPUSD / USDCAD): ");
        String symbol = scanner.nextLine();

        TradeRequest request = new TradeRequest(
                balance,
                riskPercent,
                entryPrice,
                stopLoss,
                symbol
        );

        TradeResult result = calculator.calculate(request);

        System.out.println("\n=== RESULT ===");
        System.out.println("Symbol: " + result.getSymbol());
        System.out.println("Risk Amount: $" + result.getRiskAmount());
        System.out.println("Stop Loss (pips): " + result.getStopLossPips());
        System.out.println("Lot Size: " + result.getLotSize());
    }

    //tested with real 'EURUSD' and 'GBPUSD' trades.Results match broker calculations.
    //USDCAD initially showed error.Fixed by introducing dynamic pip value calculation.

}