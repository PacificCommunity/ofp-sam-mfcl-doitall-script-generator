/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.spc.ofp.mfcl.mfcldoit.control.FormError;
import org.spc.ofp.mfcl.mfcldoit.control.FormValidator;
import org.spc.ofp.mfcl.mfcldoit.control.about.AboutPaneController;
import org.spc.ofp.mfcl.mfcldoit.control.codeeditor.CodeEditor;
import org.spc.ofp.mfcl.mfcldoit.control.error.ErrorPaneController;
import org.spc.ofp.mfcl.mfcldoit.control.phase.PhaseEditorController;
import org.spc.ofp.mfcl.mfcldoit.control.project.ProjectConfigPaneController;
import org.spc.ofp.mfcl.mfcldoit.task.export.ExportFileParameters;
import org.spc.ofp.mfcl.mfcldoit.task.export.ExportFileParametersBuilder;
import org.spc.ofp.mfcl.mfcldoit.task.export.ExportFileTask;
import org.spc.ofp.mfcl.mfcldoit.task.generate.ProjectParameters;
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
    @FXML
    private Node errorPane;
    @FXML
    private ErrorPaneController errorPaneController;

    /**
     * Display the code on screen.
     */
    private final CodeEditor codeEditor = new CodeEditor();

    private ResourceBundle bundle;

    /**
     * Creates a new instance.
     */
    public MainUIController() {
        super();
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
                projectConfigPaneController.applicationProperty().unbind();
                projectConfigPaneController.formValidProperty().removeListener(invalidationListener);
                projectConfigPaneController.parametersProperty().removeListener(invalidationListener);
                projectConfigPaneController.dispose();
                projectConfigPaneController = null;
            }
            if (pathField != null) {
                pathField.textProperty().removeListener(invalidationListener);
                pathField = null;
            }
            if (errorPane != null) {
                errorPane.visibleProperty().unbind();
                errorPane.managedProperty().unbind();
                errorPane = null;
            }
            if (errorPaneController != null) {
                errorPaneController.setErrors(null);
                errorPaneController.dispose();
                errorPaneController = null;
            }
            previewVBox.getChildren().remove(codeEditor);
            VBox.clearConstraints(codeEditor);
            codeEditor.dispose();
        } finally {
            super.dispose();
        }
    }

    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        this.bundle = bundle;
        //
        errorPane.visibleProperty().bind(Bindings.isNotEmpty(errors));
        errorPane.managedProperty().bind(errorPane.visibleProperty());
        //
        errorPaneController.setErrors(errors);
        //
        VBox.setVgrow(codeEditor, Priority.ALWAYS);
        previewVBox.getChildren().add(1, codeEditor);
        //
        projectConfigPaneController.applicationProperty().bind(applicationProperty());
        projectConfigPaneController.formValidProperty().addListener(invalidationListener);
        projectConfigPaneController.parametersProperty().addListener(invalidationListener);
        //
        final String lastPath = prefs.get("last.file", ""); // NOI18N.
        pathField.setText(lastPath);
        pathField.textProperty().addListener(invalidationListener);
        //
        exportButton.disableProperty().bind(formValidProperty().not());
        //
        requestValidateForm();
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Called whenever a value is invalidated or sub-forms.
     */
    private final InvalidationListener invalidationListener = observable -> requestValidateForm();

    ////////////////////////////////////////////////////////////////////////////
    
    /**
    * Called whenever the browse button is clicked.
    */
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
    
    /**
    * Called whenever the create button is clicked.
    */
    @FXML
    private void handleCreateButton(final ActionEvent actionEvent) {
        exportToFile();
    }

    /**
    * Called whenever the help button is clicked.
    */
    @FXML
    private void handleHelpButton(final ActionEvent actionEvent) {
        try {
            final URL fxmlURL = getClass().getResource("control/about/AboutPane.fxml"); // NOI18N.
            final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
            final Node node = fxmlLoader.load();
            final AboutPaneController controller = fxmlLoader.getController();
            controller.applicationProperty().bind(applicationProperty());
            controller.setOnClose(event -> {
                rootPane.getChildren().remove(node);
                AnchorPane.clearConstraints(node);
                controller.applicationProperty().unbind();
                controller.dispose();
            });
            AnchorPane.setTopAnchor(node, 0d);
            AnchorPane.setLeftAnchor(node, 0d);
            AnchorPane.setBottomAnchor(node, 0d);
            AnchorPane.setRightAnchor(node, 0d);
            rootPane.getChildren().add(node);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Allow to store user preferences.
     */
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());
    /**
     * Controllers for each phase.
     */
    private final Map<Number, PhaseEditorController> phaseControllers = new HashMap<>();

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
            allErrors.add(new FormError(bundle.getString("ERROR_EXPORT_PATH_EMPTY_MESSAGE"), pathField)); // NOI18N.
            pathField.getStyleClass().add(ERROR_STYLE_CLASSS);
        }
        // Phase tabs.
        final ProjectParameters projectParameters = projectConfigPaneController.getParameters();
        final int phaseTabOffet = 1;
        final int oldPhaseNumber = tabPane.getTabs().size() - phaseTabOffet;
        final int newPhaseNumber = projectParameters.getPhaseNumber();
        // Add if needed.
        for (int phaseIndex = oldPhaseNumber; phaseIndex < newPhaseNumber; phaseIndex++) {
            try {
                final URL fxmlURL = getClass().getResource("control/phase/PhaseEditor.fxml"); // NOI18N.
                final FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL, bundle);
                final Node phaseNode = fxmlLoader.load();
                final PhaseEditorController phaseController = (PhaseEditorController) fxmlLoader.getController();
                phaseController.applicationProperty().bind(applicationProperty());
                final String tabTitle = String.format(bundle.getString("PHASE_PATTERN"), phaseIndex + 1); // NOI18N.
                final Tab tab = new Tab(tabTitle);
                tab.setContent(phaseNode);
                tabPane.getTabs().add(tab);
                phaseControllers.put(phaseIndex, phaseController);
            } catch (IOException ex) {
                Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        // Remove if needed.
        for (int phaseIndex = newPhaseNumber; phaseIndex < oldPhaseNumber; phaseIndex++) {
            final Tab tab = tabPane.getTabs().remove(newPhaseNumber + phaseTabOffet);
            final PhaseEditorController phaseController = phaseControllers.remove(phaseIndex);
            phaseController.applicationProperty().unbind();
            phaseController.dispose();
        }
        // Configure style for special phases.
        tabPane.getTabs().forEach(tab -> tab.getStyleClass().removeAll("phase1", "phase2", "final-phase")); // NOI18N.
        if (newPhaseNumber >= 1) {
            tabPane.getTabs().get(phaseTabOffet + 0).getStyleClass().add("phase1"); // NOI18N.
        }
        if (newPhaseNumber >= 2) {
            tabPane.getTabs().get(phaseTabOffet + 1).getStyleClass().add("phase2"); // NOI18N.
        }
        if (newPhaseNumber > 0) {
            tabPane.getTabs().get(phaseTabOffet + newPhaseNumber - 1).getStyleClass().add("final-phase"); // NOI18N.
        }
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
                    final ProjectParameters projectParameters = projectConfigPaneController.getParameters();
                    final CodeGenerateTask task = new CodeGenerateTask(projectParameters);
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
                    final ProjectParameters projectParameters = projectConfigPaneController.getParameters();
                    final ExportFileParametersBuilder exportFileBuilder = ExportFileParametersBuilder.create();
                    exportFileBuilder
                            .folder(file.getParentFile())
                            .filename(file.getName())
                            .projectParameters(projectParameters);
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
