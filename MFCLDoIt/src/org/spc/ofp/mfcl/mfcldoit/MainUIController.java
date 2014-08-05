/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.InvalidationListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.spc.ofp.mfcl.mfcldoit.control.FormError;
import org.spc.ofp.mfcl.mfcldoit.control.FormValidator;
import org.spc.ofp.mfcl.mfcldoit.control.codeeditor.CodeEditor;
import org.spc.ofp.mfcl.mfcldoit.control.project.ProjectConfigPaneController;
import org.spc.ofp.mfcl.mfcldoit.task.export.ExportFileParameters;
import org.spc.ofp.mfcl.mfcldoit.task.export.ExportFileParametersBuilder;
import org.spc.ofp.mfcl.mfcldoit.task.export.ExportFileTask;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateParameters;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateParametersBuilder;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateTask;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class MainUIController extends FormValidator implements Initializable, Disposable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab projectTab;
    @FXML
    private VBox previewVBox;
    @FXML
    private ProjectConfigPaneController projectConfigPaneController;
    @FXML
    private TextField pathField;
    @FXML
    private Button pathButton;
    @FXML
    private Button exportButton;
    /**
     * Display the code on screen.
     */
    private final CodeEditor codeEditor = new CodeEditor();
    /**
     * The builder used to generate the parameters.
     */
    final CodeGenerateParametersBuilder codeGenerateBuilder = CodeGenerateParametersBuilder.create();

    private ResourceBundle bundle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        this.bundle = bundle;
        //
        VBox.setVgrow(codeEditor, Priority.ALWAYS);
        previewVBox.getChildren().add(codeEditor);
        //
        projectConfigPaneController.formValidProperty().addListener(invalidationListener);
        projectConfigPaneController.modelExecutableProperty().addListener(invalidationListener);
        projectConfigPaneController.useRelativePathProperty().addListener(invalidationListener);
        projectConfigPaneController.frqFileProperty().addListener(invalidationListener);
        projectConfigPaneController.preActionsProperty().addListener(invalidationListener);
        projectConfigPaneController.phaseNumberProperty().addListener(invalidationListener);
        projectConfigPaneController.postActionsProperty().addListener(invalidationListener);
        projectConfigPaneController.includePhaseHeadersProperty().addListener(invalidationListener);
        projectConfigPaneController.includePreActionsHeaderProperty().addListener(invalidationListener);
        projectConfigPaneController.includePostActionsHeaderProperty().addListener(invalidationListener);
        //
        final String lastPath = prefs.get("last.file", ""); // NOI18N.
        pathField.setText(lastPath);
        pathField.textProperty().addListener(invalidationListener);
        //
        exportButton.disableProperty().bind(formValidProperty().not());
        //
        requestValidateForm();
    }

    @Override
    public void dispose() {
        try {
            if (exportService != null) {
                exportService.cancel();
                exportService = null;
            }
            if (codeGenerateService != null) {
                codeGenerateService.cancel();
                codeGenerateService = null;
            }
            if (projectConfigPaneController != null) {
                projectConfigPaneController.formValidProperty().removeListener(invalidationListener);
                projectConfigPaneController.modelExecutableProperty().removeListener(invalidationListener);
                projectConfigPaneController.useRelativePathProperty().removeListener(invalidationListener);
                projectConfigPaneController.frqFileProperty().removeListener(invalidationListener);
                projectConfigPaneController.preActionsProperty().removeListener(invalidationListener);
                projectConfigPaneController.phaseNumberProperty().removeListener(invalidationListener);
                projectConfigPaneController.postActionsProperty().removeListener(invalidationListener);
                projectConfigPaneController.includePhaseHeadersProperty().removeListener(invalidationListener);
                projectConfigPaneController.includePreActionsHeaderProperty().removeListener(invalidationListener);
                projectConfigPaneController.includePostActionsHeaderProperty().removeListener(invalidationListener);
                projectConfigPaneController.dispose();
                projectConfigPaneController = null;
            }
            if (pathField != null) {
                pathField.textProperty().removeListener(invalidationListener);
                pathField = null;
            }
            previewVBox.getChildren().remove(codeEditor);
            VBox.clearConstraints(codeEditor);
            codeEditor.dispose();
        } finally {
            super.dispose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Called whenever a value is invalidated or sub-forms.
     */
    private final InvalidationListener invalidationListener = observable -> requestValidateForm();

    ////////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleBrowseButton(final ActionEvent actionEvent) {
        final String lastPath = prefs.get("last.file", ""); // NOI18N.
        final File lastFile = new File(lastPath);
        File folder = lastFile.getParentFile();
        folder = (folder == null || !folder.exists()) ? new File(System.getProperty("user.dir")) : folder; // NOI18N.
        final FileChooser dialog = new FileChooser();
        dialog.setInitialDirectory(folder);
        dialog.setInitialFileName(lastFile.getName());
        final File file = dialog.showSaveDialog(tabPane.getScene().getWindow());
        if (file != null) {
            final String filePath = file.getAbsolutePath();
            pathField.setText(filePath);
            prefs.put("last.file", filePath); // NOI18N.            
        }
    }

    @FXML
    private void handleCreateButton(final ActionEvent actionEvent) {
        exportToFile();
    }
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Allow to store user preferences.
     */
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());

    @Override
    protected void impl_validateForm() {
        final List<FormError> allErrors = new LinkedList<>();
        // Get errors from sub-forms.
        allErrors.addAll(projectConfigPaneController.getErrors());
        // Output path.
        pathField.getStyleClass().remove(ERROR_STYLE_CLASSS);
        String filePath = pathField.getText();
        filePath = (filePath != null) ? filePath.trim() : null;
        if (filePath == null || filePath.isEmpty()) {
            // @todo Localize!
            allErrors.add(new FormError("Export path cannot be empty.", pathField));
            pathField.getStyleClass().add(ERROR_STYLE_CLASSS);
        }
        // Model executable.
        final String modelExecutable = projectConfigPaneController.getModelExecutable();
        codeGenerateBuilder.modelExecutable(modelExecutable);
        // Use relative path.
        final boolean useRelativePath = projectConfigPaneController.isUseRelativePath();
        codeGenerateBuilder.useRelativePath(useRelativePath);
        // FRQ file.
        final String frqFile = projectConfigPaneController.getFRQFile();
        codeGenerateBuilder.frqFile(frqFile);
        // Pre-action.
        final String preActions = projectConfigPaneController.getPreActions();
        codeGenerateBuilder.preActions(preActions);
        // Post-action.
        final String postActions = projectConfigPaneController.getPostActions();
        codeGenerateBuilder.postActions(postActions);
        // Phase tabs.
        final int phaseTabOffet = 1;
        final int oldPhaseNumber = tabPane.getTabs().size() - phaseTabOffet;
        final int newPhaseNumber = projectConfigPaneController.getPhaseNumber();
        // Add if needed.
        for (int phaseIndex = oldPhaseNumber; phaseIndex < newPhaseNumber; phaseIndex++) {
            try {
                // @todo Localize!
                final URL fxmlURL = getClass().getResource("control/phase/PhaseEditor.fxml"); // NOI18N.
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
                final Node phaseNode = fxmlLoader.load();
                final Tab tabNode = new Tab(String.format("Phase %d", phaseIndex + 1));
                tabNode.setContent(phaseNode);
                tabPane.getTabs().add(tabNode);
            } catch (IOException ex) {
                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        // Remove if needed.
        for (int phaseIndex = newPhaseNumber; phaseIndex < oldPhaseNumber; phaseIndex++) {
            final Tab tab = tabPane.getTabs().remove(newPhaseNumber + phaseTabOffet);
        }
        codeGenerateBuilder.phaseNumber(newPhaseNumber);
        //
        codeGenerateBuilder.includePhaseHeaders(projectConfigPaneController.isIncludePhaseHeaders());
        codeGenerateBuilder.includePreActionsHeader(projectConfigPaneController.isIncludePreActionsHeader());
        codeGenerateBuilder.includePostActionsHeader(projectConfigPaneController.isIncludePostActionsHeader());
        //
        updateCodeInPreview();
        //
        errors.setAll(allErrors);
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Service used to generate code in the preview.
     */
    private Service<String> codeGenerateService;

    /**
     * Update the code in the preview using an async service.
     */
    private void updateCodeInPreview() {
        if (codeGenerateService == null) {
            codeGenerateService = new Service<String>() {

                @Override
                protected Task<String> createTask() {
                    final CodeGenerateParameters codeGenerateParameters = codeGenerateBuilder.build();
                    final CodeGenerateTask task = new CodeGenerateTask(codeGenerateParameters);
                    return task;
                }
            };
            codeGenerateService.setOnSucceeded(workerStateEvent -> {
                final String result = codeGenerateService.getValue();
                codeEditor.setCursor(Cursor.TEXT);
                codeEditor.setText(result);
                Logger.getLogger(getClass().getName()).log(Level.FINEST, "codeGenerateService succeeded."); // NOI18N.
            });
            codeGenerateService.setOnCancelled(workerStateEvent -> {
                codeEditor.setCursor(Cursor.DEFAULT);
                Logger.getLogger(getClass().getName()).log(Level.FINEST, "codeGenerateService cancelled."); // NOI18N.
            });
            codeGenerateService.setOnFailed(workerStateEvent -> {
                codeEditor.setCursor(Cursor.DEFAULT);
                final Throwable ex = codeGenerateService.getException();
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            });
        }
        codeEditor.setText(null);
        codeEditor.setCursor(Cursor.WAIT);
        codeGenerateService.restart();
    }

    /**
     * Service used to export code to a path.
     */
    private Service<File> exportService;

    /**
     * Export the code using an async service.
     */
    private void exportToFile() {
        if (exportService == null) {
            exportService = new Service<File>() {

                @Override
                protected Task<File> createTask() {
                    final String filePath = pathField.getText().trim();
                    prefs.put("last.file", filePath); // NOI18N.            
                    final File file = new File(filePath);
                    final CodeGenerateParameters codeGenerateParameters = codeGenerateBuilder.build();
                    final ExportFileParametersBuilder exportFileBuilder = ExportFileParametersBuilder.create();
                    exportFileBuilder
                            .folder(file.getParentFile())
                            .filename(file.getName())
                            .codeGenerateParameters(codeGenerateParameters);
                    final ExportFileParameters exportFileParameters = exportFileBuilder.build();
                    final ExportFileTask task = new ExportFileTask(exportFileParameters);
                    return task;
                }
            };
            exportService.setOnSucceeded(workerStateEvent -> {
                rootPane.setCursor(Cursor.DEFAULT);
                Logger.getLogger(getClass().getName()).log(Level.FINEST, "exportService succeeded."); // NOI18N.
            });
            exportService.setOnCancelled(workerStateEvent -> {
                rootPane.setCursor(Cursor.DEFAULT);
                Logger.getLogger(getClass().getName()).log(Level.FINEST, "exportService cancelled."); // NOI18N.
            });
            exportService.setOnFailed(workerStateEvent -> {
                rootPane.setCursor(Cursor.DEFAULT);
                final Throwable ex = codeGenerateService.getException();
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            });
        }
        rootPane.setCursor(Cursor.WAIT);
        exportService.restart();
    }
}
