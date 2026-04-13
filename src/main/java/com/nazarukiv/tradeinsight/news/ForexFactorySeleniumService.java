package com.nazarukiv.tradeinsight.news;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import java.util.ArrayList;
import java.util.List;

public class ForexFactorySeleniumService {

    public List<NewsItem> getHighImpactNews() {

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.forexfactory.com/calendar?day=tomorrow");

        List<NewsItem> result = new ArrayList<>();

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("tr.calendar__row")
            ));

            List<WebElement> rows = driver.findElements(By.cssSelector("tr.calendar__row"));

            for (WebElement row : rows) {
                try {
                    WebElement impactEl = row.findElement(By.cssSelector("td.calendar__impact span"));
                    String classes = impactEl.getAttribute("class");

                    if (!classes.contains("ff-impact-red")) {
                        continue;
                    }

                    String time = row.findElement(By.cssSelector("td.calendar__time")).getText();
                    if (time.isEmpty()) time = "All Day";

                    String currency = row.findElement(By.cssSelector("td.calendar__currency")).getText();

                    String event = row.findElement(
                            By.cssSelector("td.calendar__cell.calendar__event")
                    ).getText();

                    result.add(new NewsItem("today", time, currency, "HIGH", event));

                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            System.out.println("Error parsing news: " + e.getMessage());
        }

        driver.quit();
        return result;
    }
}