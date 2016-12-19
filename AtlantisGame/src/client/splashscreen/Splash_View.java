package client.splashscreen;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Splash_View  {
    ProgressBar progress;
    private Label lblStatus;
    Stage splashStage;
    Splash_Model splashModel;

    public Splash_View(Stage splashStage, Splash_Model splashModel) {
    	 this.splashStage = splashStage;
         this.splashModel = splashModel;
         
         Scene scene = create_GUI(); // Create all controls within "root"
         splashStage.setScene(scene);
        
        splashStage.initStyle(StageStyle.TRANSPARENT); // also undecorated
    }

    protected Scene create_GUI() {
        BorderPane root = new BorderPane();
        root.setId("splash");

        lblStatus = new Label("Woof");
        root.setCenter(lblStatus);
        
        progress = new ProgressBar();
        HBox bottomBox = new HBox();
        bottomBox.setId("progressbox");
        bottomBox.getChildren().add(progress);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 300, 300, Color.TRANSPARENT);
        scene.getStylesheets().addAll(
                this.getClass().getResource("splash.css").toExternalForm());

        return scene;
    }
}
