package com.nazarukiv.tradeinsight.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ForexFactorySeleniumService {

    private static final String CALENDAR_URL = "https://www.forexfactory.com/calendar?day=today";
    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    public List<NewsItem> getHighImpactNews() {
        List<NewsItem> result = new ArrayList<>();
        String currentDate = "Today";

        try {
            Document document = Jsoup.connect(CALENDAR_URL)
                    .userAgent(DESKTOP_USER_AGENT)
                    .referrer("https://www.forexfactory.com/")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Cache-Control", "no-cache")
                    .timeout(15000)
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .get();

            Elements rows = document.select("tr.calendar__row, tr.calendar_row");

            if (rows.isEmpty()) {
                return result;
            }

            for (Element row : rows) {
                String impact = extractImpact(row);
                if (!"HIGH".equals(impact)) {
                    continue;
                }

                Element dateCell = firstMatching(row, "td.calendar__date", "td.calendar_date");
                if (dateCell != null && !dateCell.text().isBlank()) {
                    currentDate = dateCell.text();
                }

                String time = textOrDefault(firstMatching(row, "td.calendar__time", "td.calendar_time"), "All Day");
                String currency = textOrDefault(firstMatching(row, "td.calendar__currency", "td.calendar_currency"), "N/A");
                String event = textOrDefault(
                        firstMatching(
                                row,
                                "td.calendar__event",
                                "td.calendar__cell.calendar__event",
                                "td.calendar_event"
                        ),
                        "Unknown event"
                );

                if (!"Unknown event".equals(event)) {
                    result.add(new NewsItem(currentDate, time, currency, impact, event));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Forex Factory news", e);
        }

        return result;
    }

    private String extractImpact(Element row) {
        Element impactEl = firstMatching(
                row,
                "td.calendar__impact span[title]",
                "td.calendar__impact span",
                "td.calendar_impact span[title]",
                "td.calendar_impact span"
        );
        if (impactEl == null) {
            return "LOW";
        }

        String title = impactEl.attr("title");
        String classes = impactEl.className().toLowerCase(Locale.ROOT);
        String text = impactEl.text().toLowerCase(Locale.ROOT);

        if (title.toLowerCase(Locale.ROOT).contains("high")
                || classes.contains("ff-impact-red")
                || classes.contains("impact--high")
                || text.contains("high")) {
            return "HIGH";
        }

        if (title.toLowerCase(Locale.ROOT).contains("medium")
                || classes.contains("ff-impact-ora")
                || classes.contains("impact--medium")
                || text.contains("medium")) {
            return "MEDIUM";
        }

        return "LOW";
    }

    private Element firstMatching(Element root, String... selectors) {
        for (String selector : selectors) {
            Element match = root.selectFirst(selector);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private String textOrDefault(Element element, String fallback) {
        if (element == null) {
            return fallback;
        }

        String text = element.text().trim();
        return text.isEmpty() ? fallback : text;
    }
}
