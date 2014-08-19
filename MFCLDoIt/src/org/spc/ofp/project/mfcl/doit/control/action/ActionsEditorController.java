/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.action;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import org.spc.ofp.project.mfcl.doit.Disposable;
import org.spc.ofp.project.mfcl.doit.FXMLControllerBase;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ActionsEditorController extends FXMLControllerBase implements Initializable, Disposable {

    @FXML
    private TextArea textArea;
    @FXML
    private Tooltip textTip;
    @FXML
    private CheckBox includeHeaderCheck;

    @Override
    public void dispose() {
        try {
            if (textArea != null) {
                actionsProperty().unbindBidirectional(textArea.textProperty());
                textArea.promptTextProperty().unbind();
                textArea.setTooltip(null);
                textArea = null;
            }
            if (textTip != null) {
                textTip.textProperty().unbindBidirectional(actionsProperty());
                textTip = null;
            }
            if (includeHeaderCheck != null) {
                includeHeaderCheck.selectedProperty().unbindBidirectional(includeHeaderProperty());
                includeHeaderCheck = null;
            }
        } finally {
            super.dispose();
        }
    }

    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        final StringBinding promptTextBinding = new StringBinding() {
            {
                bind(flavorProperty());
            }

            @Override
            public void dispose() {
                unbind(flavorProperty());
                super.dispose();
            }

            @Override
            protected String computeValue() {
                final Flavor flavor = getFlavor();
                String result = null;
                switch (flavor) {
                    case PRE_PHASE_ACTIONS:
                        result = "ACTION_PRE_PHASE_ACTIONS_PROMPT"; // NOI18N.
                        break;
                    case POST_PHASE_ACTIONS:
                        result = "ACTION_POST_PHASE_ACTIONS_PROMPT"; // NOI18N.
                        break;
                    case POST_SCRIPT_ACTIONS:
                        result = "ACTION_POST_SCRIPT_ACTIONS_PROMPT"; // NOI18N.
                        break;
                    case PRE_SCRIPT_ACTIONS:
                    default:
                        result = "ACTION_PRE_SCRIPT_ACTIONS_PROMPT"; // NOI18N.
                }
                return bundle.getString(result);
            }
        };
        //
        actionsProperty().bindBidirectional(textArea.textProperty());
        textArea.promptTextProperty().bind(promptTextBinding);
        //
        textTip.textProperty().bind(promptTextBinding);
        //
        includeHeaderCheck.selectedProperty().bindBidirectional(includeHeaderProperty());
    }

    ////////////////////////////////////////////////////////////////////////////
    private final ReadOnlyObjectWrapper<Flavor> flavor = new ReadOnlyObjectWrapper<>(this, "flavor", Flavor.PRE_SCRIPT_ACTIONS); // NOI18N.

    public final Flavor getFlavor() {
        return flavor.get();
    }

    public final void setFlavor(final Flavor value) {
        flavor.set(value == null ? Flavor.PRE_SCRIPT_ACTIONS : value);
    }

    public final ReadOnlyObjectProperty<Flavor> flavorProperty() {
        return flavor.getReadOnlyProperty();
    }

    private final StringProperty actions = new SimpleStringProperty(this, "actions"); // NOI18N.

    public final String getActions() {
        return actions.get();
    }

    public void setActions(final String value) {
        actions.set(value);
    }

    public final StringProperty actionsProperty() {
        return actions;
    }

    private final BooleanProperty includeHeader = new SimpleBooleanProperty(this, "includeHeader"); // NOI18N.

    public final boolean isIncludeHeader() {
        return includeHeader.get();
    }

    public final void setIncludeHeader(final boolean value) {
        includeHeader.set(value);
    }

    public final BooleanProperty includeHeaderProperty() {
        return includeHeader;
    }

}
