/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit;

import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Simple Preloader Using the ProgressBar Control
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class MFCLDoIt_Preloader extends Preloader {

    private ProgressBar progressBar;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        progressBar = new ProgressBar();
        final BorderPane root = new BorderPane();
        root.setCenter(progressBar);
        final Scene scene = new Scene(root, 300, 150);
        this.stage = stage;
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(final StateChangeNotification stateChangeNotification) {
        switch (stateChangeNotification.getType()) {
            case BEFORE_START:
                stage.hide();
                break;
            default:
        }
    }

    @Override
    public void handleProgressNotification(final ProgressNotification progressNotification) {
        progressBar.setProgress(progressNotification.getProgress());
    }
}
