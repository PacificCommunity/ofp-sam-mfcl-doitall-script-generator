/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.control.about;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.spc.ofp.mfcl.mfcldoit.Disposable;
import org.spc.ofp.mfcl.mfcldoit.FXMLControllerBase;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class AboutPaneController extends FXMLControllerBase implements Initializable, Disposable {

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
