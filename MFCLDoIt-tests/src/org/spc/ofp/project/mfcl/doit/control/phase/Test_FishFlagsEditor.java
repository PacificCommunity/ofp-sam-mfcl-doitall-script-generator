/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.phase;

import org.spc.ofp.project.mfcl.doit.control.phase.FishFlagsEditorController;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.spc.ofp.project.mfcl.doit.flag.FishFlag;
import org.spc.ofp.project.mfcl.doit.flag.Fishery;
import org.spc.ofp.project.mfcl.doit.flag.FisheryGroup;

/**
 * Test class for the fish flags editor.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public class Test_FishFlagsEditor extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        final URL fxmlURL = getClass().getResource("FishFlagsEditor.fxml");
        final ResourceBundle bundle = ResourceBundle.getBundle("org.spc.ofp.mfcl.doit.strings");
        final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
        final Node editor = fxmlLoader.load();
        final FishFlagsEditorController editorController = fxmlLoader.getController();
        //
        final int fisheryNumber = 5;
        final List<Fishery> fisheries = new LinkedList<>();
        for (int fisheryIndex = 0; fisheryIndex < fisheryNumber; fisheryIndex++) {
            final Fishery fishery = new Fishery(fisheryIndex, null);
            fisheries.add(fishery);
        }
        final List<FishFlag> fishFlags = new LinkedList<>();
        final int fishFlagNumber = 5;
        for (int fishFlagIndex = 0; fishFlagIndex < fishFlagNumber; fishFlagIndex++) {
            final FishFlag fishFlag = new FishFlag(fishFlagIndex + 1);
            fishFlags.add(fishFlag);
        }
        fishFlags.forEach(fishFlag -> fisheries.forEach(fishery -> {
            final FisheryGroup fisheryGroup = new FisheryGroup();
            fisheryGroup.getFisheries().add(fishery);
            fishFlag.getValues().put(fisheryGroup, -1);
        }));
        editorController.loadContent(fisheries, fishFlags);
        //
        final StackPane root = new StackPane();
        root.getChildren().add(editor);
        final Scene scene = new Scene(root, 500, 800);
        primaryStage.setTitle("Test_FishFlagsEditor");
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
