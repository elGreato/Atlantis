package client.splashscreen;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.concurrent.Task;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Splash_Model  {
 

    public Splash_Model() {
        super();
    }

    // A task is a JavaFX class that implements Runnable. Tasks are designed to
    // have attached listeners, which we can use to monitor their progress.
    final Task<Void> initializer = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            // First, take some time, update progress
            Integer i = 0;
            for (; i < 1000000000; i++) {
                if ((i % 1000000) == 0)
                    this.updateProgress(i, 1000000000);
            }

      

            return null;
        }
    };

    public void initialize() {
        new Thread(initializer).start();
    }

 
}
