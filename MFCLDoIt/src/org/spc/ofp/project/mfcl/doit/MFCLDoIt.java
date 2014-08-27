/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * App class.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class MFCLDoIt extends Application {

    @Override
    public void start(Stage primaryStage) {
        MFCLDoItConstants.getInstance();
        try {
            final URL fxmlURL = getClass().getResource("MainUI.fxml"); // NOI18N.
            final ResourceBundle bundle = ResourceBundle.getBundle("org.spc.ofp.project.mfcl.doit.strings"); // NOI18N.
            final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
            final Parent root = fxmlLoader.<Parent>load();
            final MainUIController controller = fxmlLoader.getController();
            controller.setApplication(this);
            final Scene scene = new Scene(root);
            final URL cssURL = getClass().getResource("MFCLDoIt.css"); // NOI18N.
            scene.getStylesheets().add(cssURL.toExternalForm());
            primaryStage.setTitle(bundle.getString("APP_TITLE")); // NOI18N.
            primaryStage.setScene(scene);
            primaryStage.getIcons().setAll(
                    new Image(getClass().getResource("MFCLDoIt_16.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_24.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_32.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_48.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_64.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_128.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_256.png").toExternalForm()), // NOI18N.
                    new Image(getClass().getResource("MFCLDoIt_512.png").toExternalForm())); // NOI18N.
            primaryStage.show();            
        } catch (IOException ex) {
            Logger.getLogger(MFCLDoIt.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(-2);
        }
    }

    /**
     * Main.
     * @param args The command line arguments.
     */
    public static void main(String... args) {
        launch(args);
    }
}
