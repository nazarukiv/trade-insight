package com.nazarukiv.tradeinsight.news;

public class NewsItem {

    private final String date;
    private final String time;
    private final String currency;
    private final String impact;
    private final String event;

    public NewsItem(String date, String time, String currency, String impact, String event) {
        this.date = date;
        this.time = time;
        this.currency = currency;
        this.impact = impact;
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCurrency() {
        return currency;
    }

    public String getImpact() {
        return impact;
    }

    public String getEvent() {
        return event;
    }
}