package com.nazarukiv.tradeinsight;

import com.nazarukiv.tradeinsight.calculator.PositionSizeCalculator;
import com.nazarukiv.tradeinsight.calculator.TradeRequest;
import com.nazarukiv.tradeinsight.calculator.TradeResult;
import com.nazarukiv.tradeinsight.news.ForexFactorySeleniumService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.nazarukiv.tradeinsight.news.NewsItem;


import java.math.BigDecimal;

@SpringBootApplication
public class TradeInsightApplication {

    public static void main(String[] args) {

        ForexFactorySeleniumService service = new ForexFactorySeleniumService();
        var news = service.getHighImpactNews();

        System.out.println("=== HIGH IMPACT NEWS ===");

        for (NewsItem item : news) {
            System.out.println(item.getTime() + " | " + item.getCurrency() + " | " + item.getEvent());
        }

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

        System.out.print("Enter symbol \n(EURUSD / GBPUSD / USDCAD /\n GER40 / NDX100 /\n XAUUSD(gold)/ XAGUSD(silver)): ");
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
        System.out.println("Stop Loss (pips): " + result.getStopLossTicks());
        System.out.println("Lot Size: " + result.getLotSize());
    }

    //tested with real 'EURUSD' and 'GBPUSD' trades.Results match broker calculations.
    //USDCAD initially showed error.Fixed by introducing dynamic pip value calculation.


    // tested with real trades: XAUUSD, GER40, NDX100.
    // results match broker calculations within acceptable rounding range.
    // XAGUSD still needs real trade validation.

}