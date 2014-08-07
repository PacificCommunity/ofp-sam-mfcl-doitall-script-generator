/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.control.about;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.spc.ofp.mfcl.mfcldoit.MFCLDoIt;

/**
 * Test class for the addon pane.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public class Test_AddonPane extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        final URL fxmlURL = getClass().getResource("AddonPane.fxml"); // NOI18N.
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.spc.ofp.mfcl.mfcldoit.strings"); // NOI18N.
        final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, resourceBundle);
        final Node node = (Node) fxmlLoader.load();
        final StackPane root = new StackPane();
        root.getChildren().add(node);
        final Scene scene = new Scene(root, 300, 300);
        final URL cssURL = MFCLDoIt.class.getResource("MFCLDoIt.css");
        scene.getStylesheets().add(cssURL.toExternalForm());
        primaryStage.setTitle("Test_AddonPane"); // NOI18N.
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
