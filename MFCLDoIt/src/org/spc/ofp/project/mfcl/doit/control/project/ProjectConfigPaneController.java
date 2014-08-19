/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.project;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.spc.ofp.project.mfcl.doit.Disposable;
import org.spc.ofp.project.mfcl.doit.control.FormError;
import org.spc.ofp.project.mfcl.doit.control.FormValidator;
import org.spc.ofp.project.mfcl.doit.control.action.ActionsEditorController;
import org.spc.ofp.project.mfcl.doit.control.action.Flavor;
import org.spc.ofp.project.mfcl.doit.task.generate.ProjectParameters;
import org.spc.ofp.project.mfcl.doit.task.generate.ProjectParametersBuilder;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ProjectConfigPaneController extends FormValidator implements Initializable, Disposable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ComboBox<String> modelExecutableCombo;
    @FXML
    private CheckBox modelRelativePathCheck;
    @FXML
    private TextField frqFileField;
    @FXML
    private Button frqFileBrowseButton;
    @FXML
    private TextField iniFileField;
    @FXML
    private Button iniFileBrowseButton;
    @FXML
    private TextField phaseNumberField;
    @FXML
    private CheckBox makeParCheck;
    @FXML
    private CheckBox includePhaseHeadersCheck;
    @FXML
    private ActionsEditorController preActionsEditorController;
    @FXML
    private ActionsEditorController postActionsEditorController;

    /**
     * Creates a new instance.
     */
    public ProjectConfigPaneController() {
    }

    @Override
    public void dispose() {
        try {
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
            if (iniFileField != null) {
                iniFileField.disableProperty().unbind();
                iniFileField = null;
            }
            if (iniFileBrowseButton != null) {
                iniFileBrowseButton.disableProperty().unbind();
                iniFileBrowseButton = null;
            }
            if (preActionsEditorController != null) {
                preActionsEditorController.actionsProperty().removeListener(invalidationListener);
                preActionsEditorController.includeHeaderProperty().removeListener(invalidationListener);
                preActionsEditorController.dispose();
                preActionsEditorController = null;
            }
            if (postActionsEditorController != null) {
                postActionsEditorController.actionsProperty().removeListener(invalidationListener);
                postActionsEditorController.includeHeaderProperty().removeListener(invalidationListener);
                postActionsEditorController.dispose();
                postActionsEditorController = null;
            }
            if (phaseNumberField != null) {
                phaseNumberField.textProperty().removeListener(invalidationListener);
                phaseNumberField = null;
            }
            if (includePhaseHeadersCheck != null) {
                includePhaseHeadersCheck.selectedProperty().removeListener(invalidationListener);
                includePhaseHeadersCheck = null;
            }
            if (makeParCheck != null) {
                makeParCheck.selectedProperty().removeListener(invalidationListener);
                makeParCheck = null;
            }
        } finally {
            super.dispose();
        }
    }

    private ResourceBundle bundle;

    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        this.bundle = bundle;
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
        iniFileField.textProperty().addListener(invalidationListener);
        iniFileField.disableProperty().bind(makeParCheck.selectedProperty().not());
        //
        iniFileBrowseButton.disableProperty().bind(makeParCheck.selectedProperty().not());
        // Pre-actions.
        preActionsEditorController.setFlavor(Flavor.PRE_SCRIPT_ACTIONS);
        preActionsEditorController.setIncludeHeader(prefs.getBoolean("include.pre.script.actions.header", true)); // NOI18N.
        preActionsEditorController.actionsProperty().addListener(invalidationListener);
        preActionsEditorController.includeHeaderProperty().addListener(invalidationListener);
        // Post-actions.
        postActionsEditorController.setFlavor(Flavor.POST_SCRIPT_ACTIONS);
        postActionsEditorController.actionsProperty().addListener(invalidationListener);
        postActionsEditorController.setIncludeHeader(prefs.getBoolean("include.post.script.actions.header", true)); // NOI18N.
        postActionsEditorController.includeHeaderProperty().addListener(invalidationListener);
        //
        phaseNumberField.textProperty().addListener(invalidationListener);
        //
        includePhaseHeadersCheck.setSelected(prefs.getBoolean("include.phase.header", true)); // NOI18N.
        includePhaseHeadersCheck.selectedProperty().addListener(invalidationListener);
        //
        makeParCheck.selectedProperty().addListener(invalidationListener);
        // This will initialize the very first parameter object. 
        // After this call, the parameter object will never be null.
        validateForm();
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Called when a value has been invalidated.
     */
    private final InvalidationListener invalidationListener = observable -> requestValidateForm();

    ////////////////////////////////////////////////////////////////////////////
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());
    /**
     * The builder used to generate the parameters.
     */
    final ProjectParametersBuilder parametersBuilder = ProjectParametersBuilder.create();

    @Override
    protected void impl_validateForm() {
        final List<FormError> allErrors = new LinkedList<>();
        // Model name.
        modelExecutableCombo.getStyleClass().remove(ERROR_STYLE_CLASSS);
        String modelName = modelExecutableCombo.getValue();
        modelName = (modelName != null) ? modelName.trim() : null;
        if (modelName == null || modelName.isEmpty()) {
            allErrors.add(new FormError(bundle.getString("ERROR_MODEL_NAME_EMPTY_MESSAGE"), modelExecutableCombo)); // NOI18N.
            modelExecutableCombo.getStyleClass().add(ERROR_STYLE_CLASSS);
        } else {
            parametersBuilder.modelExecutable(modelName);
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
        parametersBuilder.useRelativePath(userRelativePath);
        // FRQ file.
        frqFileField.getStyleClass().remove(ERROR_STYLE_CLASSS);
        final String frqFile = frqFileField.getText();
        if (frqFile == null || frqFile.trim().isEmpty()) {
            allErrors.add(new FormError(bundle.getString("ERROR_FRQ_FILE_EMPTY_MESSAGE"), frqFileField)); // NOI18N.
            frqFileField.getStyleClass().add(ERROR_STYLE_CLASSS);
        } else {
            prefs.put("frq.file", frqFile); // NOI18N.
        }
        parametersBuilder.frqFile(frqFile);
        // INI file.
        final boolean makePar = makeParCheck.isSelected();
        parametersBuilder.makePar(makePar);
        iniFileField.getStyleClass().remove(ERROR_STYLE_CLASSS);
        final String iniFile = iniFileField.getText();
        if (makePar && (iniFile == null || iniFile.trim().isEmpty())) {
            allErrors.add(new FormError(bundle.getString("ERROR_INI_FILE_EMPTY_MESSAGE"), iniFileField)); // NOI18N.
            iniFileField.getStyleClass().add(ERROR_STYLE_CLASSS);
        } else {
            prefs.put("ini.file", frqFile); // NOI18N.
        }
        parametersBuilder.iniFile(iniFile);
        // Phase number.
        phaseNumberField.getStyleClass().remove(ERROR_STYLE_CLASSS);
        int phaseNumber = 0;
        try {
            final String text = phaseNumberField.getText();
            phaseNumber = Integer.parseInt(text);
            if (phaseNumber <= 0) {
                allErrors.add(new FormError(bundle.getString("ERROR_PHASE_NUMBER_INVALID_MESSAGE"), phaseNumberField)); // NOI18N.
                phaseNumberField.getStyleClass().add(ERROR_STYLE_CLASSS);
            }
        } catch (Exception ex) {
            // @todo Log this.
            allErrors.add(new FormError(bundle.getString("ERROR_PHASE_NUMBER_NOT_AN_INT_MESSAGE"), phaseNumberField)); // NOI18N.
            phaseNumberField.getStyleClass().add(ERROR_STYLE_CLASSS);
        }
        parametersBuilder.phaseNumber(phaseNumber);
        // Phase header.
        final boolean includePhaseHeaders = includePhaseHeadersCheck.isSelected();
        prefs.putBoolean("include.phase.header", includePhaseHeaders); // NOI18N.
        parametersBuilder.includePhaseHeaders(includePhaseHeaders);
        // Pre-actions.
        final String preActions = preActionsEditorController.getActions();
        parametersBuilder.preActions(preActions);
        final boolean includePreActionsHeader = preActionsEditorController.isIncludeHeader();
        prefs.putBoolean("include.pre.script.actions.header", includePreActionsHeader); // NOI18N.
        parametersBuilder.includePreActionsHeader(includePreActionsHeader);
        // Post-actions.
        final String postActions = postActionsEditorController.getActions();
        parametersBuilder.postActions(postActions);
        final boolean includePostActionsHeader = postActionsEditorController.isIncludeHeader();
        prefs.putBoolean("include.post.script.actions.header", includePostActionsHeader); // NOI18N.
        parametersBuilder.includePostActionsHeader(includePostActionsHeader);
        //
        parameters.set(parametersBuilder.build());
        //
        errors.setAll(allErrors);
    }

    ////////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleFRQBrowseButton(final ActionEvent actionEvent) {
        final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(bundle.getString("GENERAL_FRQ_FILE_DESCRIPTION"), "*.frq"); // NOI18N.       
        final FileChooser dialog = new FileChooser();
        dialog.getExtensionFilters().add(filter);
        dialog.setSelectedExtensionFilter(filter);
        final Optional<File> fileOptional = Optional.ofNullable(dialog.showOpenDialog(rootPane.getScene().getWindow()));
        fileOptional.ifPresent(file -> frqFileField.setText(file.getName()));
    }

    @FXML
    private void handleINIBrowseButton(final ActionEvent actionEvent) {
        final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(bundle.getString("GENERAL_INI_FILE_DESCRIPTION"), "*.ini"); // NOI18N.        
        final FileChooser dialog = new FileChooser();
        dialog.getExtensionFilters().add(filter);
        dialog.setSelectedExtensionFilter(filter);
        final Optional<File> fileOptional = Optional.ofNullable(dialog.showOpenDialog(rootPane.getScene().getWindow()));
        fileOptional.ifPresent(file -> iniFileField.setText(file.getName()));
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * The parameter object for this config pane.
     */
    private final ReadOnlyObjectWrapper<ProjectParameters> parameters = new ReadOnlyObjectWrapper(this, "parameters"); // NOI18N.

    public final ProjectParameters getParameters() {
        return parameters.get();
    }

    public final ReadOnlyObjectProperty<ProjectParameters> parametersProperty() {
        return parameters.getReadOnlyProperty();
    }
}
