/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.control.phase;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.spc.ofp.mfcl.mfcldoit.Disposable;
import org.spc.ofp.mfcl.mfcldoit.flag.FishFlag;
import org.spc.ofp.mfcl.mfcldoit.flag.Fishery;
import org.spc.ofp.mfcl.mfcldoit.flag.FisheryGroup;

/**
 * FXML Controller class
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class FishFlagsEditorController implements Initializable, Disposable {
    
    @FXML
    private TableView<Fishery> tableView;
    @FXML
    private TableColumn<Fishery, String> fisheriesColumn;

    /**
     * Creates a new instance.
     */
    public FishFlagsEditorController() {
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }        

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle bundle) {
        tableView.setItems(fisheries);
        fisheriesColumn.setEditable(false);
        fisheriesColumn.setCellValueFactory(cellDataFeature -> {
            final Fishery fishery = cellDataFeature.getValue();
            return new SimpleStringProperty(fishery.getName());
        });
    }

    ////////////////////////////////////////////////////////////////////////////
    private void populateColumns() {
        tableView.getColumns().remove(1, tableView.getColumns().size());
        final List<TableColumn<Fishery, FisheryGroup>> flagColumns = flags.stream()
                .map(this::createColumnForFlag)
                .collect(Collectors.toList());
        tableView.getColumns().addAll(flagColumns);
        tableView.setColumnResizePolicy((tableView.getColumns().size() < 10) ? TableView.CONSTRAINED_RESIZE_POLICY : TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            final int rowIndex = tableView.getSelectionModel().getSelectedIndex();
            final Fishery fishery = fisheries.get(rowIndex);
            flags.forEach(flag -> {
                flag.getValues().keySet().forEach(fisheryGroup -> {
                    if (fisheryGroup.getFisheries().contains(fishery)) {
                        fisheryGroup.setSelected(true);
                    } else {
                        fisheryGroup.setSelected(false);
                    }
                });
            });
        });
    }

    /**
     * Create a new table column for given fish flag.
     * @param fishFlag The fish flag.
     * @return A {@code TableColumn<Fishery, FisheryGroup>} instance, never {@code null}.
     */
    private TableColumn<Fishery, FisheryGroup> createColumnForFlag(final FishFlag fishFlag) {
        final TableColumn<Fishery, FisheryGroup> column = new TableColumn<>();
        column.setPrefWidth(35);
        column.setEditable(true);
        column.setSortable(false);
        column.setText(String.valueOf(fishFlag.getId()));
        column.setCellValueFactory(cellDataFeature -> {
            final Fishery fishery = cellDataFeature.getValue();
            final FisheryGroup fisheryGroup = fishFlag.getValues().entrySet().stream()
                    .map(entry -> entry.getKey())
                    .filter(group -> group.getFisheries().contains(fishery))
                    .reduce(null, (a, b) -> b);
            final ObjectProperty<FisheryGroup> property = new SimpleObjectProperty<>(fisheryGroup);
            property.addListener((ObservableValue<? extends FisheryGroup> observableValue, FisheryGroup oldValue, FisheryGroup newValue) -> {
                // Transfer fishery to new group.
                oldValue.getFisheries().remove(fishery);
                newValue.getFisheries().add(fishery);
                // Remove group if empty.
                if (oldValue.getFisheries().isEmpty()) {
                    fishFlag.getValues().remove(oldValue);
                }
                // Add new group if needed.
                if (!fishFlag.getValues().containsKey(newValue)) {
                    fishFlag.getValues().put(newValue, -1);
                }
            });
            return property;
        });
        column.setCellFactory(tableColumn -> new FishGroupTableCell(fishFlag, fisheries));
        return column;
    }

    ////////////////////////////////////////////////////////////////////////////
    /** 
     * List of flags in this editor.
     */
    private final ObservableList<FishFlag> flags = FXCollections.observableList(new LinkedList<>());
    private final ObservableList<FishFlag> flagsReadOnly = FXCollections.unmodifiableObservableList(flags);
    
    public final ObservableList<FishFlag> getFlags() {
        return flagsReadOnly;
    }

    /** 
     * List of flags in this editor.
     */
    private final ObservableList<Fishery> fisheries = FXCollections.observableList(new LinkedList<>());
    private final ObservableList<Fishery> fisheriesReadOnly = FXCollections.unmodifiableObservableList(fisheries);
    
    public final ObservableList<Fishery> getFisheries() {
        return fisheriesReadOnly;
    }
    
    public void loadContent(final List<Fishery> fisheries, final List<FishFlag> flags) {
        this.fisheries.setAll(fisheries);
        this.flags.setAll(flags);
        populateColumns();
    }
}
