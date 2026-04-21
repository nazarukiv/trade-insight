package com.nazarukiv.tradeinsight.news;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ForexFactorySeleniumService {

    public List<NewsItem> getHighImpactNews() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--log-level=3");

        WebDriver driver = new ChromeDriver(options);
        List<NewsItem> result = new ArrayList<>();

        try {
            driver.get("https://www.forexfactory.com/calendar?day=today");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("td.calendar__cell.calendar__event")
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
                    if (time.isBlank()) {
                        time = "All Day";
                    }

                    String currency = row.findElement(By.cssSelector("td.calendar__currency")).getText();

                    String event = row.findElement(
                            By.cssSelector("td.calendar__cell.calendar__event")
                    ).getText();

                    result.add(new NewsItem("tomorrow", time, currency, "HIGH", event));

                } catch (Exception ignored) {
                }
            }

        } catch (Exception e) {
            System.out.println("Error parsing news: " + e.getMessage());
        } finally {
            driver.quit();
        }

        return result;
    }
}