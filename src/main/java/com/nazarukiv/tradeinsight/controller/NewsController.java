package com.nazarukiv.tradeinsight.controller;

import com.nazarukiv.tradeinsight.news.ForexFactorySeleniumService;
import com.nazarukiv.tradeinsight.news.NewsItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final ForexFactorySeleniumService newsService = new ForexFactorySeleniumService();

    @GetMapping
    public List<NewsItem> getNews() {
        return newsService.getHighImpactNews();
    }
}