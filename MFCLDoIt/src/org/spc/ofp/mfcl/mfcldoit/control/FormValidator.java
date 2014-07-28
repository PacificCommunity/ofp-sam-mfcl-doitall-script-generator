/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.control;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import org.spc.ofp.mfcl.mfcldoit.Disposable;

/**
 * The base class for a form validator.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public abstract class FormValidator implements Disposable {

    /**
     * The style class to use whenever a control is in an error state.
     */
    public static final String ERROR_STYLE_CLASSS = "error";

    /**
     * Creates a new instance.
     */
    public FormValidator() {
        // Form is valid if last validation did not generate any error.
        formValid.bind(Bindings.isEmpty(errors));
    }

    @Override
    public void dispose() {
        if (validateFormTimer != null) {
            validateFormTimer.stop();
            validateFormTimer = null;
        }
        errors.clear();
        formValid.unbind();
    }

    ////////////////////////////////////////////////////////////////////////////
    private final Duration validateFormWaitTime = Duration.millis(350);
    private PauseTransition validateFormTimer = null;

    /**
     * Request form validation.
     */
    public final void requestValidateForm() {
        if (validateFormTimer == null) {
            validateFormTimer = new PauseTransition(validateFormWaitTime);
            validateFormTimer.setOnFinished(actionEvent -> validateForm());
        }
        validateFormTimer.playFromStart();
    }

    /**
     * Validate the form immediately.
     */
    public final void validateForm() {
        try {
            impl_validateForm();
        } catch (Throwable ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Proceed with form validation.
     */
    protected abstract void impl_validateForm();

    ////////////////////////////////////////////////////////////////////////////
    /**
     * List of errors from last form validation.
     */
    protected final ObservableList<FormError> errors = FXCollections.observableList(new LinkedList<>());
    /**
     * The read-only list of errors.
     */
    private final ObservableList<FormError> errorsReadOnly = FXCollections.unmodifiableObservableList(errors);

    /**
     * Gets the errors from the last form validation.
     * @return A non-modifiable {@code ObservableList<FormError>}, never {@code null}.
     */
    public ObservableList<FormError> getErrors() {
        return errorsReadOnly;
    }

    /**
     * Indicates whether the form has been validated.
     */
    private final ReadOnlyBooleanWrapper formValid = new ReadOnlyBooleanWrapper(this, "formValid"); // NOI18N.

    public final boolean isFormValid() {
        return formValid.get();
    }

    public final ReadOnlyBooleanProperty formValidProperty() {
        return formValid.getReadOnlyProperty();
    }
}
