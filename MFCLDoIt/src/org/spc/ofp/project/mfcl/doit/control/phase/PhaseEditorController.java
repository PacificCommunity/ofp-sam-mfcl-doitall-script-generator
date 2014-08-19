/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.phase;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import org.spc.ofp.project.mfcl.doit.control.FormValidator;
import org.spc.ofp.project.mfcl.doit.control.action.ActionsEditorController;
import org.spc.ofp.project.mfcl.doit.control.action.Flavor;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class PhaseEditorController extends FormValidator {

    @FXML
    private FishFlagsEditorController fishFlagsEditorController;
    @FXML
    private ActionsEditorController preActionsEditorController;
    @FXML
    private ActionsEditorController postActionsEditorController;

    /**
     * Creates a new instance.
     */
    public PhaseEditorController() {
    }

    @Override
    public void dispose() {
        try {
            if (preActionsEditorController != null) {
                preActionsEditorController.actionsProperty().removeListener(valueInvalidationListener);
                preActionsEditorController.includeHeaderProperty().removeListener(valueInvalidationListener);
                preActionsEditorController.dispose();
                preActionsEditorController = null;
            }
            if (postActionsEditorController != null) {
                postActionsEditorController.actionsProperty().removeListener(valueInvalidationListener);
                postActionsEditorController.includeHeaderProperty().removeListener(valueInvalidationListener);
                postActionsEditorController.dispose();
                postActionsEditorController = null;
            }
        } finally {
            super.dispose();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        preActionsEditorController.setFlavor(Flavor.PRE_PHASE_ACTIONS);
        preActionsEditorController.actionsProperty().addListener(valueInvalidationListener);
        preActionsEditorController.includeHeaderProperty().addListener(valueInvalidationListener);
        //
        postActionsEditorController.setFlavor(Flavor.POST_PHASE_ACTIONS);
        postActionsEditorController.actionsProperty().addListener(valueInvalidationListener);
        postActionsEditorController.includeHeaderProperty().addListener(valueInvalidationListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    private final InvalidationListener valueInvalidationListener = observable -> requestValidateForm();

    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void impl_validateForm() {
    }
}
