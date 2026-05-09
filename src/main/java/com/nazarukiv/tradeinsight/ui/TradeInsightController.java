package com.nazarukiv.tradeinsight.ui;

import com.nazarukiv.tradeinsight.calculator.PositionSizeCalculator;
import com.nazarukiv.tradeinsight.calculator.TradeRequest;
import com.nazarukiv.tradeinsight.calculator.TradeResult;
import com.nazarukiv.tradeinsight.news.ForexFactorySeleniumService;
import com.nazarukiv.tradeinsight.news.NewsItem;
import com.nazarukiv.tradeinsight.session.SessionService;
import com.nazarukiv.tradeinsight.session.SessionTime;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TradeInsightController {

    private static final DateTimeFormatter SESSION_FORMAT = DateTimeFormatter.ofPattern("EEE HH:mm");
    private static final double NEWS_PANEL_HEIGHT = 236;

    private final PositionSizeCalculator calculator = new PositionSizeCalculator();
    private final SessionService sessionService = new SessionService();
    private final ForexFactorySeleniumService newsService = new ForexFactorySeleniumService();

    private final ScrollPane view = new ScrollPane();
    private final VBox page = new VBox(14);

    private final Label errorLabel = new Label();
    private final Label resultLabel = new Label("Enter trade values and calculate.");
    private final TextField balanceField = createField("Balance");
    private final TextField riskField = createField("Risk %");
    private final TextField entryField = createField("Entry Price");
    private final TextField stopLossField = createField("Stop Loss");
    private final ComboBox<String> symbolBox = new ComboBox<>(
            FXCollections.observableArrayList("EURUSD", "GBPUSD", "USDCAD", "GER40", "NDX100", "XAUUSD", "XAGUSD")
    );
    private final ComboBox<String> timezoneBox = new ComboBox<>(
            FXCollections.observableArrayList("London", "Kyiv", "New York")
    );
    private final VBox sessionsBox = new VBox(10);
    private final VBox newsBox = new VBox(10);

    public TradeInsightController() {
        symbolBox.setValue("EURUSD");
        timezoneBox.setValue("London");

        page.getStyleClass().add("app-page");
        page.setMaxWidth(1120);
        page.setFillWidth(true);
        page.getChildren().addAll(buildHeader(), buildContent());

        StackPane contentShell = new StackPane(page);
        contentShell.getStyleClass().add("app-shell");
        contentShell.setAlignment(Pos.TOP_CENTER);
        contentShell.setPadding(new Insets(18));

        view.setContent(contentShell);
        view.getStyleClass().add("app-root");
        view.setFitToWidth(true);
        view.setFitToHeight(false);
        view.setPannable(true);
        view.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        view.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public ScrollPane getView() {
        return view;
    }

    public void loadDefaultData() {
        sessionsBox.getChildren().setAll(createMutedLabel("Loading sessions..."));
        newsBox.getChildren().setAll(createMutedLabel("Loading news..."));

        String timezone = timezoneBox.getValue();

        Thread thread = new Thread(() -> {
            List<SessionTime> sessions;
            List<NewsItem> newsItems;

            try {
                String zone = sessionService.mapToZoneId(timezone);
                sessions = sessionService.getSessions(zone);
            } catch (Exception ex) {
                sessions = null;
            }

            try {
                newsItems = newsService.getHighImpactNews();
            } catch (Exception ex) {
                newsItems = null;
            }

            List<SessionTime> finalSessions = sessions;
            List<NewsItem> finalNewsItems = newsItems;
            Platform.runLater(() -> {
                renderSessions(finalSessions);
                renderNews(finalNewsItems);
            });
        }, "trade-insight-default-data");
        thread.setDaemon(true);
        thread.start();
    }

    private VBox buildHeader() {
        Label title = new Label("Trade Insight");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Local trading utility with instant startup and no browser.");
        subtitle.getStyleClass().add("subtitle");

        VBox header = new VBox(4, title, subtitle);
        header.getStyleClass().add("header-block");
        return header;
    }

    private GridPane buildContent() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("content-grid");
        grid.setHgap(14);
        grid.setVgap(14);

        ColumnConstraints calculatorColumn = new ColumnConstraints();
        calculatorColumn.setPercentWidth(56);
        calculatorColumn.setHgrow(Priority.ALWAYS);

        ColumnConstraints sessionsColumn = new ColumnConstraints();
        sessionsColumn.setPercentWidth(44);
        sessionsColumn.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(calculatorColumn, sessionsColumn);

        VBox calculatorCard = buildCalculatorCard();
        VBox sessionsCard = buildSessionsCard();
        VBox newsCard = buildNewsCard();

        GridPane.setHgrow(calculatorCard, Priority.ALWAYS);
        GridPane.setHgrow(sessionsCard, Priority.ALWAYS);
        GridPane.setHgrow(newsCard, Priority.ALWAYS);
        calculatorCard.setMaxWidth(Double.MAX_VALUE);
        sessionsCard.setMaxWidth(Double.MAX_VALUE);
        newsCard.setMaxWidth(Double.MAX_VALUE);

        grid.add(calculatorCard, 0, 0);
        grid.add(sessionsCard, 1, 0);
        grid.add(newsCard, 0, 1, 2, 1);

        return grid;
    }

    private VBox buildCalculatorCard() {
        Label heading = createCardTitle("Position Size");

        errorLabel.getStyleClass().add("error-label");
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);
        resultLabel.getStyleClass().add("result-label");

        Button calculateButton = new Button("Calculate");
        calculateButton.getStyleClass().add("primary-button");
        calculateButton.setMaxWidth(Double.MAX_VALUE);
        calculateButton.setOnAction(event -> calculate());

        VBox card = createCard(
                heading,
                errorLabel,
                balanceField,
                riskField,
                entryField,
                stopLossField,
                symbolBox,
                calculateButton,
                resultLabel
        );
        return card;
    }

    private VBox buildSessionsCard() {
        Label heading = createCardTitle("Sessions");

        Button loadSessionsButton = new Button("Refresh Sessions");
        loadSessionsButton.getStyleClass().add("secondary-button");
        loadSessionsButton.setMaxWidth(Double.MAX_VALUE);
        loadSessionsButton.setOnAction(event -> loadSessions());

        sessionsBox.getStyleClass().add("content-list");
        sessionsBox.getChildren().setAll(createMutedLabel("Loading sessions..."));

        return createCard(heading, timezoneBox, loadSessionsButton, sessionsBox);
    }

    private VBox buildNewsCard() {
        Label heading = createCardTitle("High Impact News");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button loadNewsButton = new Button("Refresh News");
        loadNewsButton.getStyleClass().add("secondary-button");
        loadNewsButton.setOnAction(event -> loadNews());

        HBox toolbar = new HBox(10, heading, spacer, loadNewsButton);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        newsBox.getStyleClass().add("content-list");
        newsBox.getChildren().setAll(createMutedLabel("Loading news..."));
        newsBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(newsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefViewportHeight(NEWS_PANEL_HEIGHT);
        scrollPane.setPrefHeight(NEWS_PANEL_HEIGHT);
        scrollPane.setMinHeight(190);
        scrollPane.setMaxHeight(252);
        scrollPane.getStyleClass().add("news-scroll");

        VBox card = createCard(toolbar, scrollPane);
        card.getStyleClass().add("news-card");
        return card;
    }

    private VBox createCard(javafx.scene.Node... nodes) {
        VBox card = new VBox(10, nodes);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(16));
        card.setFillWidth(true);
        return card;
    }

    private Label createCardTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("card-title");
        return label;
    }

    private TextField createField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        return field;
    }

    private Label createMutedLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("muted-label");
        return label;
    }

    private void calculate() {
        hideError();

        try {
            TradeRequest request = new TradeRequest(
                    parseDecimal(balanceField.getText(), "Balance"),
                    parseDecimal(riskField.getText(), "Risk %"),
                    parseDecimal(entryField.getText(), "Entry Price"),
                    parseDecimal(stopLossField.getText(), "Stop Loss"),
                    symbolBox.getValue()
            );

            TradeResult result = calculator.calculate(request);
            resultLabel.setText(
                    "Risk: $" + result.getRiskAmount()
                            + "\nSL: " + result.getStopLossTicks() + " ticks"
                            + "\nLot: " + result.getLotSize()
            );
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    public void loadSessions() {
        sessionsBox.getChildren().setAll(createMutedLabel("Loading sessions..."));

        String timezone = timezoneBox.getValue();
        Thread thread = new Thread(() -> {
            try {
                String zone = sessionService.mapToZoneId(timezone);
                List<SessionTime> sessions = sessionService.getSessions(zone);
                Platform.runLater(() -> renderSessions(sessions));
            } catch (Exception ex) {
                Platform.runLater(() -> sessionsBox.getChildren().setAll(
                        createMutedLabel("Failed to load sessions: " + ex.getMessage())
                ));
            }
        }, "trade-insight-sessions");
        thread.setDaemon(true);
        thread.start();
    }

    public void loadNews() {
        newsBox.getChildren().setAll(createMutedLabel("Loading news..."));

        Thread thread = new Thread(() -> {
            try {
                List<NewsItem> items = newsService.getHighImpactNews();
                Platform.runLater(() -> renderNews(items));
            } catch (Exception ex) {
                Platform.runLater(() -> newsBox.getChildren().setAll(
                        createMutedLabel("Failed to load news: " + ex.getMessage())
                ));
            }
        }, "trade-insight-news");
        thread.setDaemon(true);
        thread.start();
    }

    private void renderSessions(List<SessionTime> sessions) {
        sessionsBox.getChildren().clear();

        if (sessions == null || sessions.isEmpty()) {
            sessionsBox.getChildren().add(createMutedLabel("No session data available."));
            return;
        }

        for (SessionTime session : sessions) {
            VBox item = new VBox(
                    sessionRow(session.getName(), SESSION_FORMAT.format(session.getStart())),
                    sessionRow("Ends", SESSION_FORMAT.format(session.getEnd()))
            );
            item.getStyleClass().add("list-item");
            sessionsBox.getChildren().add(item);
        }
    }

    private void renderNews(List<NewsItem> items) {
        newsBox.getChildren().clear();

        if (items == null || items.isEmpty()) {
            newsBox.getChildren().add(createMutedLabel("No news available"));
            return;
        }

        for (NewsItem item : items) {
            Label currency = new Label(item.getCurrency());
            currency.getStyleClass().add("currency-pill");

            Label title = new Label(item.getEvent());
            title.getStyleClass().add("news-title");
            title.setWrapText(true);

            Label meta = new Label(item.getDate() + "  /  " + item.getTime());
            meta.getStyleClass().add("news-meta");
            meta.setWrapText(true);

            VBox copy = new VBox(2, title, meta);
            copy.setMinWidth(0);
            HBox.setHgrow(copy, Priority.ALWAYS);

            Label impact = new Label(item.getImpact());
            impact.getStyleClass().add("impact-badge");

            HBox newsItem = new HBox(10, currency, copy, impact);
            newsItem.getStyleClass().add("list-item");
            newsItem.getStyleClass().add("news-item");
            newsItem.setAlignment(Pos.TOP_LEFT);
            newsBox.getChildren().add(newsItem);
        }
    }

    private HBox sessionRow(String label, String value) {
        Label left = new Label(label);
        left.getStyleClass().add("session-name");

        Label right = new Label(value);
        right.getStyleClass().add("session-time");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(12, left, spacer, right);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private BigDecimal parseDecimal(String rawValue, String fieldName) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }

        try {
            BigDecimal value = new BigDecimal(rawValue.trim());
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(fieldName + " must be greater than 0");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setManaged(true);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setManaged(false);
        errorLabel.setVisible(false);
    }
}
