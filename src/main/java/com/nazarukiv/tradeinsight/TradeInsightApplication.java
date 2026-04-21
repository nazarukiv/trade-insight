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
        org.springframework.boot.SpringApplication.run(TradeInsightApplication.class, args);

    }

    //tested with real 'EURUSD' and 'GBPUSD' trades.Results match broker calculations.
    //USDCAD initially showed error.Fixed by introducing dynamic pip value calculation.


    // tested with real trades: XAUUSD, GER40, NDX100.
    // results match broker calculations within acceptable rounding range.
    // XAGUSD still needs real trade validation.

}