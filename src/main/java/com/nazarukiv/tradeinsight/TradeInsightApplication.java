package com.nazarukiv.tradeinsight;

import com.nazarukiv.tradeinsight.calculator.PositionSizeCalculator;
import com.nazarukiv.tradeinsight.calculator.TradeRequest;
import com.nazarukiv.tradeinsight.calculator.TradeResult;
import com.nazarukiv.tradeinsight.news.ForexFactorySeleniumService;
import com.nazarukiv.tradeinsight.session.SessionService;
import com.nazarukiv.tradeinsight.session.SessionTime;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.nazarukiv.tradeinsight.news.NewsItem;


import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class TradeInsightApplication {

    public static void main(String[] args) {

        ForexFactorySeleniumService newsService = new ForexFactorySeleniumService();
        List<NewsItem> news = newsService.getHighImpactNews();

        System.out.println("=== HIGH IMPACT NEWS ===");
        if (news.isEmpty()) {
            System.out.println("No high impact news found.");
        } else {
            for (NewsItem item : news) {
                System.out.println(item.getTime() + " | " + item.getCurrency() + " | " + item.getEvent());
            }
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter timezone (London / Kyiv / New York): ");
        String input = scanner.nextLine();

        SessionService sessionService = new SessionService();
        String zoneId = sessionService.mapToZoneId(input);

        List<SessionTime> sessions = sessionService.getSessions(zoneId);

        System.out.println("\n=== SESSIONS ===");
        for (SessionTime session : sessions) {
            System.out.println(
                    session.getName() + ": " +
                            session.getStart().toLocalTime() + " - " +
                            session.getEnd().toLocalTime()
            );
        }

        PositionSizeCalculator calculator = new PositionSizeCalculator();

        System.out.println("\n=== POSITION SIZE CALCULATOR ===");

        System.out.print("Enter balance: ");
        BigDecimal balance = new BigDecimal(scanner.nextLine());

        System.out.print("Enter risk %: ");
        BigDecimal riskPercent = new BigDecimal(scanner.nextLine());

        System.out.print("Enter entry price: ");
        BigDecimal entryPrice = new BigDecimal(scanner.nextLine());

        System.out.print("Enter stop loss price: ");
        BigDecimal stopLoss = new BigDecimal(scanner.nextLine());

        System.out.print("Enter symbol (EURUSD / GBPUSD / USDCAD / GER40 / NDX100 / XAUUSD / XAGUSD): ");
        String symbol = scanner.nextLine().trim().toUpperCase();

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
        System.out.println("Stop Loss (ticks): " + result.getStopLossTicks());
        System.out.println("Lot Size: " + result.getLotSize());
    }

    //tested with real 'EURUSD' and 'GBPUSD' trades.Results match broker calculations.
    //USDCAD initially showed error.Fixed by introducing dynamic pip value calculation.


    // tested with real trades: XAUUSD, GER40, NDX100.
    // results match broker calculations within acceptable rounding range.
    // XAGUSD still needs real trade validation.

}