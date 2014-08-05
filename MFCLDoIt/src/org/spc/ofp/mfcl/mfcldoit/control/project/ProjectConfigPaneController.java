/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.control.project;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.spc.ofp.mfcl.mfcldoit.Disposable;
import org.spc.ofp.mfcl.mfcldoit.control.FormError;
import org.spc.ofp.mfcl.mfcldoit.control.FormValidator;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ProjectConfigPaneController extends FormValidator implements Initializable, Disposable {

    @FXML
    private ComboBox<String> modelExecutableCombo;
    @FXML
    private CheckBox modelRelativePathCheck;
    @FXML
    private TextField frqFileField;
    @FXML
    private TextField phaseNumberField;
    @FXML
    private TextArea preActionsArea;
    @FXML
    private TextArea postActionsArea;
    @FXML
    private CheckBox includePhaseHeadersCheck;
    @FXML
    private CheckBox includePreActionsHeaderCheck;
    @FXML
    private CheckBox includePostActionsHeaderCheck;

    /**
     * Creates a new instance.
     */
    public ProjectConfigPaneController() {
    }

    @Override
    public void dispose() {
        try {
            includePhaseHeaders.unbind();
            includePreActionsHeader.unbind();
            includePostActionsHeader.unbind();
            if (modelExecutableCombo != null) {
                modelExecutableCombo.valueProperty().removeListener(invalidationListener);
                modelExecutableCombo.getItems().clear();
                modelExecutableCombo = null;
            }
            if (modelRelativePathCheck != null) {
                modelRelativePathCheck.selectedProperty().removeListener(invalidationListener);
                modelRelativePathCheck = null;
            }
            if (frqFileField != null) {
                frqFileField.textProperty().removeListener(invalidationListener);
                frqFileField = null;
            }
            if (preActionsArea != null) {
                preActionsArea.textProperty().removeListener(invalidationListener);
                preActionsArea = null;
            }
            if (postActionsArea != null) {
                postActionsArea.textProperty().removeListener(invalidationListener);
                postActionsArea = null;
            }
            if (phaseNumberField != null) {
                phaseNumberField.textProperty().removeListener(invalidationListener);
                phaseNumberField = null;
            }
        } finally {
            super.dispose();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        // Populate model names list.
        final String modelNames = prefs.get("model.executables", "mfclo, mfclo32, mfclo64, gmult"); // NOI18N.
        final List<String> modelExecutables = Arrays.asList(modelNames.split(",")) // NOI18N.
                .stream()
                .map(value -> value.trim())
                .collect(Collectors.toList());
        modelExecutableCombo.getItems().setAll(modelExecutables);
        modelExecutableCombo.valueProperty().addListener(invalidationListener);
        //
        modelRelativePathCheck.selectedProperty().addListener(invalidationListener);
        //
        frqFileField.textProperty().addListener(invalidationListener);
        //
        preActionsArea.textProperty().addListener(invalidationListener);
        //
        postActionsArea.textProperty().addListener(invalidationListener);
        //
        phaseNumberField.textProperty().addListener(invalidationListener);
        //
        includePhaseHeaders.bind(includePhaseHeadersCheck.selectedProperty());
        includePreActionsHeader.bind(includePreActionsHeaderCheck.selectedProperty());
        includePostActionsHeader.bind(includePostActionsHeaderCheck.selectedProperty());
        requestValidateForm();
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Called when a value has been invalidated.
     */
    private final InvalidationListener invalidationListener = observable -> requestValidateForm();

    ////////////////////////////////////////////////////////////////////////////
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());

    @Override
    protected void impl_validateForm() {
        final List<FormError> allErrors = new LinkedList<>();
        // Model name.
        modelExecutableCombo.getStyleClass().remove(ERROR_STYLE_CLASSS);
        String modelName = modelExecutableCombo.getValue();
        modelName = (modelName != null) ? modelName.trim() : null;
        if (modelName == null || modelName.isEmpty()) {
            // @todo Localize!
            allErrors.add(new FormError("Model name cannot be empty.", modelExecutableCombo));
            modelExecutableCombo.getStyleClass().add(ERROR_STYLE_CLASSS);
        } else {
            setModelExecutable(modelName);
            // If not already in the list, add it.
            if (!modelExecutableCombo.getItems().contains(modelName)) {
                modelExecutableCombo.getItems().add(modelName);
                modelExecutableCombo.getSelectionModel().select(modelName);
                final StringBuilder toStoreBuilder = new StringBuilder();
                modelExecutableCombo.getItems().forEach(value -> toStoreBuilder.append(" ").append(value).append(",")); // NOI18N.
                final String toStore = toStoreBuilder.substring(0, toStoreBuilder.length() - 1).trim();
                prefs.put("model.executables", toStore); // NOI18N.
            }
        }
        // Relative path.
        final boolean userRelativePath = modelRelativePathCheck.isSelected();
        setUseRelativePath(userRelativePath);
        // FRQ file.
        frqFileField.getStyleClass().remove(ERROR_STYLE_CLASSS);
        final String frqFile = frqFileField.getText();
        if (frqFile == null || frqFile.trim().isEmpty()) {
            // @todo Localize!
            allErrors.add(new FormError("FRQ file cannot be empty.", frqFileField));
            frqFileField.getStyleClass().add(ERROR_STYLE_CLASSS);
        } else {
            prefs.put("frq.file", frqFile); // NOI18N.
        }
        setFRQFile(frqFile);
        // Phase number.
        phaseNumberField.getStyleClass().remove(ERROR_STYLE_CLASSS);
        int phaseNumber = 0;
        try {
            final String text = phaseNumberField.getText();
            phaseNumber = Integer.parseInt(text);
            if (phaseNumber <= 0) {
                // @todo Localize!
                allErrors.add(new FormError("Phase number must be an interger > 0.", modelExecutableCombo));
                phaseNumberField.getStyleClass().add(ERROR_STYLE_CLASSS);
            }
        } catch (Exception ex) {
            // @todo Log this.
            // @todo Localize!
            allErrors.add(new FormError("Phase number must be an interger number.", modelExecutableCombo));
            phaseNumberField.getStyleClass().add(ERROR_STYLE_CLASSS);
        }
        setPhaseNumber(phaseNumber);
        // Pre-actions.
        final String preActions = preActionsArea.getText();
        setPreActions(preActions);
        // Post-actions.
        final String postActions = postActionsArea.getText();
        setPostActions(postActions);
        //
        errors.setAll(allErrors);
    }

    ////////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleFRQBrowseButton(final ActionEvent actionEvent) {
    }

    ////////////////////////////////////////////////////////////////////////////
    private final ReadOnlyIntegerWrapper phaseNumber = new ReadOnlyIntegerWrapper(this, "phaseNumber"); // NOI18N.

    public final int getPhaseNumber() {
        return phaseNumber.get();
    }

    protected final void setPhaseNumber(final int value) {
        phaseNumber.set(Math.max(0, value));
    }

    public final ReadOnlyIntegerProperty phaseNumberProperty() {
        return phaseNumber.getReadOnlyProperty();
    }

    private final ReadOnlyBooleanWrapper useRelativePath = new ReadOnlyBooleanWrapper(this, "useRelativePath"); // NOI18N.

    public final boolean isUseRelativePath() {
        return useRelativePath.get();
    }

    protected final void setUseRelativePath(final boolean value) {
        useRelativePath.set(value);
    }

    public final ReadOnlyBooleanProperty useRelativePathProperty() {
        return useRelativePath.getReadOnlyProperty();
    }

    private final ReadOnlyStringWrapper modelExecutable = new ReadOnlyStringWrapper(this, "modelExecutable"); // NOI18N.

    public final String getModelExecutable() {
        return modelExecutable.get();
    }

    protected final void setModelExecutable(final String value) {
        modelExecutable.set(value);
    }

    public final ReadOnlyStringProperty modelExecutableProperty() {
        return modelExecutable.getReadOnlyProperty();
    }

    private final ReadOnlyStringWrapper frqFile = new ReadOnlyStringWrapper(this, "frqFile"); // NOI18N.

    public final String getFRQFile() {
        return frqFile.get();
    }

    protected final void setFRQFile(final String value) {
        frqFile.set(value);
    }

    public final ReadOnlyStringProperty frqFileProperty() {
        return frqFile.getReadOnlyProperty();
    }

    private final ReadOnlyStringWrapper preActions = new ReadOnlyStringWrapper(this, "preActions"); // NOI18N.

    public final String getPreActions() {
        return preActions.get();
    }

    protected final void setPreActions(final String value) {
        preActions.set(value);
    }

    public final ReadOnlyStringProperty preActionsProperty() {
        return preActions.getReadOnlyProperty();
    }

    private final ReadOnlyStringWrapper postActions = new ReadOnlyStringWrapper(this, "postActions"); // NOI18N.

    public final String getPostActions() {
        return postActions.get();
    }

    protected final void setPostActions(final String value) {
        postActions.set(value);
    }

    public final ReadOnlyStringProperty postActionsProperty() {
        return postActions.getReadOnlyProperty();
    }

    private final ReadOnlyBooleanWrapper includePhaseHeaders = new ReadOnlyBooleanWrapper(this, "includePhaseHeaders", true); // NOI18N.

    public final boolean isIncludePhaseHeaders() {
        return includePhaseHeaders.get();
    }

    protected final void setIncludePhaseHeaders(final boolean value) {
        includePhaseHeaders.set(value);
    }

    public final ReadOnlyBooleanProperty includePhaseHeadersProperty() {
        return includePhaseHeaders.getReadOnlyProperty();
    }

    private final ReadOnlyBooleanWrapper includePreActionsHeader = new ReadOnlyBooleanWrapper(this, "includePreActionsHeader", true); // NOI18N.

    public final boolean isIncludePreActionsHeader() {
        return includePreActionsHeader.get();
    }

    protected final void setIncludePreActionsHeader(final boolean value) {
        includePreActionsHeader.set(value);
    }

    public final ReadOnlyBooleanProperty includePreActionsHeaderProperty() {
        return includePreActionsHeader.getReadOnlyProperty();
    }

    private final ReadOnlyBooleanWrapper includePostActionsHeader = new ReadOnlyBooleanWrapper(this, "includePostActionsHeader", true); // NOI18N.

    public final boolean isIncludePostActionsHeader() {
        return includePostActionsHeader.get();
    }

    protected final void setIncludePostActionsHeader(final boolean value) {
        includePostActionsHeader.set(value);
    }

    public final ReadOnlyBooleanProperty includePostActionsHeaderProperty() {
        return includePostActionsHeader.getReadOnlyProperty();
    }
}
