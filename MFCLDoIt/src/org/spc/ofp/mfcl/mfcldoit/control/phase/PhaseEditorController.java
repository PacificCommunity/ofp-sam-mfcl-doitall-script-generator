/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.control.phase;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.spc.ofp.mfcl.mfcldoit.Disposable;
import org.spc.ofp.mfcl.mfcldoit.control.FormValidator;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class PhaseEditorController extends FormValidator {

    @FXML
    private FishFlagsEditorController fishFlagsEditorController;
    @FXML
    private TextArea preActionsArea;
    @FXML
    private TextArea postActionsArea;

    /**
     * Creates a new instance.
     */
    public PhaseEditorController() {
    }

    @Override
    public void dispose() {
        if (preActionsArea != null) {
            preActionsArea.textProperty().addListener(valueInvalidationListener);
            preActionsArea = null;
        }
        if (postActionsArea != null) {
            postActionsArea.textProperty().addListener(valueInvalidationListener);
            postActionsArea = null;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        preActionsArea.textProperty().addListener(valueInvalidationListener);
        postActionsArea.textProperty().addListener(valueInvalidationListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    private final InvalidationListener valueInvalidationListener = observable -> {
    };

    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void impl_validateForm() {
    }
}
