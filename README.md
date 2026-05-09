# Trade Insight

Trade Insight now runs as a local JavaFX desktop application.

You can open it as a normal macOS app instead of starting Spring Boot and opening a browser.

## Desktop Features

- Position size calculator
- Trading sessions by timezone
- High impact news panel
- Local JavaFX UI with dark theme
- No browser required

## Project Structure

```text
src/main/java/com/nazarukiv/tradeinsight/
├── TradeInsightDesktopApplication.java
├── calculator/
│   ├── PositionSizeCalculator.java
│   ├── SymbolInfo.java
│   ├── TradeRequest.java
│   └── TradeResult.java
├── news/
│   ├── ForexFactorySeleniumService.java
│   └── NewsItem.java
├── session/
│   ├── SessionService.java
│   └── SessionTime.java
└── ui/
    └── TradeInsightController.java

src/main/resources/
└── desktop.css
```

## Main Class

Desktop entrypoint:

```java
public class TradeInsightDesktopApplication extends Application {
    @Override
    public void start(Stage stage) {
        TradeInsightController controller = new TradeInsightController();
        Scene scene = new Scene(controller.getView(), 1180, 760);
        scene.getStylesheets().add(
                TradeInsightDesktopApplication.class.getResource("/desktop.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.show();
        controller.loadDefaultData();
    }
}
```

## Run Locally

Use Java 17.

```bash
./mvnw javafx:run
```

## Build The Desktop Jar

```bash
./mvnw clean package
```

This produces:

- `target/trade-insight-0.0.1-SNAPSHOT.jar`
- `target/libs/` with runtime dependencies

## Package As A macOS .app

1. Build the project:

```bash
./mvnw clean package
```

2. Create the app bundle with `jpackage`:

```bash
jpackage \
  --type app-image \
  --name "Trade Insight" \
  --input target \
  --main-jar trade-insight-0.0.1-SNAPSHOT.jar \
  --main-class com.nazarukiv.tradeinsight.TradeInsightDesktopApplication \
  --dest dist
```

3. Open the generated app:

```bash
open "dist/Trade Insight.app"
```

## Optional DMG Installer

```bash
jpackage \
  --type dmg \
  --name "Trade Insight" \
  --input target \
  --main-jar trade-insight-0.0.1-SNAPSHOT.jar \
  --main-class com.nazarukiv.tradeinsight.TradeInsightDesktopApplication \
  --dest dist
```

## Notes

- News still depends on internet access because the app fetches live data.
- Position size and session calculations are fully local.
- Startup is fast because the UI opens immediately and news loads in the background.

## Disclaimer

This project is for educational and demonstration purposes only.

All calculations, news data, and session times are provided as-is and may not be fully accurate.

Trading involves risk. You are fully responsible for your own trading decisions and should always double-check all data before executing any trades.
