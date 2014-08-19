/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.error;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.util.Duration;
import org.spc.ofp.project.mfcl.doit.Disposable;
import org.spc.ofp.project.mfcl.doit.FXMLControllerBase;
import org.spc.ofp.project.mfcl.doit.control.FormError;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ErrorPaneController extends FXMLControllerBase implements Initializable, Disposable {

    @FXML
    private ListView<FormError> errorList;

    /**
     * Creates a new instance.
     */
    public ErrorPaneController() {
        super();
    }

    @Override
    public void dispose() {
        try {
            errorList.getSelectionModel().selectedItemProperty().removeListener(invalidationListener);
            errorList.setItems(null);
            errorList.setCellFactory(null);
            springAnimationMap.clear();
        } finally {
            super.dispose();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorList.setCellFactory(listView -> new ErrorListCell());
        errorList.setItems(errorsProperty());
        errorList.getSelectionModel().selectedItemProperty().addListener(invalidationListener);
    }
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Called whenever selection in the error list is invalidated.
     */
    private final InvalidationListener invalidationListener = observable -> springErrorNode();

    ////////////////////////////////////////////////////////////////////////////
    /** 
     * Hold temp references to current animations.
     */
    private final Map<Node, ScaleTransition> springAnimationMap = new HashMap<>();
    /**
     * Duration of an half animation.
     */
    private final Duration springDuration = Duration.millis(125);

    /**
     * Do a spring animation on the selected error node.
     */
    private void springErrorNode() {
        final Optional<FormError> errorOptional = Optional.ofNullable(errorList.getSelectionModel().getSelectedItem());
        errorOptional.ifPresent(error -> {
            final Node node = error.getNode();
            if (!springAnimationMap.containsKey(node)) {
                final ScaleTransition springAnimation = new ScaleTransition(springDuration, node);
                springAnimation.setInterpolator(Interpolator.EASE_OUT);
                springAnimation.setByX(0.1);
                springAnimation.setByY(0.1);
                springAnimation.setCycleCount(2);
                springAnimation.setAutoReverse(true);
                springAnimation.setOnFinished(actionEvent -> springAnimationMap.remove(node));
                springAnimationMap.put(node, springAnimation);
                springAnimation.playFromStart();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * List of errors on display.
     */
    private final ListProperty<FormError> errors = new SimpleListProperty<>(this, "errors", FXCollections.observableList(new LinkedList<>())); // NOI18N.

    public final ObservableList<FormError> getErrors() {
        return errors.get();
    }

    public final void setErrors(final ObservableList<FormError> value) {
        errors.set(value);
    }

    public final ListProperty<FormError> errorsProperty() {
        return errors;
    }
}
