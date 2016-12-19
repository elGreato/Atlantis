package client.splashscreen;


import client.lobby.LobbyModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards, used by Ali Habbabeh
 */
public class Splash_Controller {
    protected Splash_Model model;
    protected Splash_View view;
	// I need to start main.startGame()

    public Splash_Controller(final LobbyModel main, Splash_Model model, Splash_View view) {
          this.model=model;
          this.view=view;
  
        view.progress.progressProperty().bind(model.initializer.progressProperty());
      
        // Using a lambda expression
        model.initializer.stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED)
                        main.startGame(null);
                });
    }
}
