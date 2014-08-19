/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.about;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.spc.ofp.project.mfcl.doit.Disposable;
import org.spc.ofp.project.mfcl.doit.FXMLControllerBase;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class AboutPaneController extends FXMLControllerBase implements Initializable, Disposable {

    @FXML
    private Label titleLabel;
    @FXML
    private Label versionLabel;
    @FXML
    private Label copyrightLabel;
    @FXML
    private TableView<String> systemPropertiesTable;
    @FXML
    private TableColumn<String, String> systemKeyColumn;
    @FXML
    private TableColumn<String, String> systemValueColumn;
    @FXML
    private AddonPaneController addonsController;

    /**
     * Creates a new instance.
     */
    public AboutPaneController() {
    }

    @Override
    public void dispose() {
        try {
            if (addonsController != null) {
                addonsController.applicationProperty().unbind();
                addonsController.dispose();
                addonsController = null;
            }
        } finally {
            super.dispose();
        }
    }

    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        addonsController.applicationProperty().bind(applicationProperty());
        //
        final Properties properties = System.getProperties();
        final List<String> keys = new ArrayList<>(properties.stringPropertyNames());
        Collections.sort(keys);
        systemKeyColumn.setCellValueFactory(cellDataFeature -> {
            final String key = cellDataFeature.getValue();
            return new SimpleStringProperty(key);
        });
        systemValueColumn.setCellValueFactory(cellDataFeature -> {
            final String key = cellDataFeature.getValue();
            return new SimpleStringProperty(System.getProperty(key));
        });
        systemPropertiesTable.getItems().setAll(keys);
    }

    ////////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleCloseButton(final ActionEvent actionEvent) {
        final Optional<EventHandler<ActionEvent>> onCloseOptional = Optional.ofNullable(getOnClose());
        onCloseOptional.ifPresent(onClose -> onClose.handle(new ActionEvent(this, null)));
    }

    ////////////////////////////////////////////////////////////////////////////
    private final ObjectProperty<EventHandler<ActionEvent>> onClose = new SimpleObjectProperty<>(this, "onClose"); // NOI18N.

    public final EventHandler<ActionEvent> getOnClose() {
        return onClose.get();
    }

    public final void setOnClose(final EventHandler<ActionEvent> value) {
        onClose.set(value);
    }

    public ObjectProperty<EventHandler<ActionEvent>> onCloseProperty() {
        return onClose;
    }
}
