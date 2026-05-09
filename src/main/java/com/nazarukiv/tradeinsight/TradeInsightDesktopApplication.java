package com.nazarukiv.tradeinsight;

import com.nazarukiv.tradeinsight.ui.TradeInsightController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class TradeInsightDesktopApplication extends Application {

    @Override
    public void start(Stage stage) {
        TradeInsightController controller = new TradeInsightController();
        ScrollPane rootView = controller.getView();

        Scene scene = new Scene(rootView, 1160, 700);
        URL stylesheet = Objects.requireNonNull(
                TradeInsightDesktopApplication.class.getResource("/desktop.css"),
                "desktop.css must be packaged with the desktop application"
        );
        scene.getStylesheets().add(stylesheet.toExternalForm());

        stage.setTitle("Trade Insight");
        stage.setMinWidth(980);
        stage.setMinHeight(640);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

        controller.loadDefaultData();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
