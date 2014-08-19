/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.phase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import org.spc.ofp.project.mfcl.doit.flag.FishFlag;
import org.spc.ofp.project.mfcl.doit.flag.Fishery;
import org.spc.ofp.project.mfcl.doit.flag.FisheryGroup;

/**
 * Table cell used to render and select fish groups.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
final class FishGroupTableCell extends TableCell<Fishery, FisheryGroup> {

    private final FishFlag fishFlag;
    private final List<Fishery> fisheries;
    private final ObjectProperty<FisheryGroup> fisheryGroupProperty = new SimpleObjectProperty<>();

    public FishGroupTableCell(final FishFlag fishFlag, final List<Fishery> fisheries) {
        this.fishFlag = fishFlag;
        this.fisheries = fisheries;
//        setEditable(true);
        final BooleanBinding groupSelected = Bindings.selectBoolean(fisheryGroupProperty, "selected");
        final StringBinding style = new StringBinding() {
            {
                bind(groupSelected);
            }

            @Override
            public void dispose() {
                unbind(groupSelected);
                super.dispose();
            }

            @Override
            protected String computeValue() {
                final boolean isSelected = groupSelected.get();
                return isSelected ? "-fx-font-weight: bold; -fx-border-color: red;" : "";
            }
        };
        styleProperty().bind(style);
        setOnMousePressed(this::handleMousePressed);
    }

    @Override
    protected void updateItem(FisheryGroup group, boolean empty) {
        super.updateItem(group, empty);
        String text = null;
        sourceProperty = null;
        fisheryGroupProperty.unbind();
        if (!empty && group != null) {
            final Number value = fishFlag.getValues().get(group);
            text = String.valueOf(value);
            sourceProperty = (ObjectProperty<FisheryGroup>) getTableColumn().getCellObservableValue(getIndex());
            fisheryGroupProperty.bind(sourceProperty);
        }
        setText(text);
    }

    private ObjectProperty<FisheryGroup> sourceProperty;

    private void handleMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isSecondaryButtonDown()) {
            final Optional<FisheryGroup> fisheryGroupOptional = Optional.ofNullable(fisheryGroupProperty.get());
            fisheryGroupOptional.ifPresent(fisheryGroup -> {
                final int rowIndex = getIndex();
                final Fishery fishery = fisheries.get(rowIndex);
                final ContextMenu contextMenu = new ContextMenu();
                final boolean canUngroup = (fisheryGroup.getFisheries().size() > 1);
                final List<Fishery> otherFisheries = fisheries.stream()
                        .filter(otherFishery -> (otherFishery != fishery) && (!fisheryGroup.getFisheries().contains(otherFishery)))
                        .collect(Collectors.toList());
                boolean canGroup = !otherFisheries.isEmpty();
                if (canUngroup) {
                    final MenuItem ungroupItem = new MenuItem("Ungroup");
                    ungroupItem.setOnAction(actionEvent -> ungroupFishery(fishery));
                    contextMenu.getItems().add(ungroupItem);
                }
                if (canUngroup && canGroup) {
                    final SeparatorMenuItem separator = new SeparatorMenuItem();
                    contextMenu.getItems().add(separator);
                }
                if (canGroup) {
                    final Menu groupWithMenu = new Menu("Group with...");
                    otherFisheries.forEach(otherFishery -> {
                        final MenuItem groupWithItem = new MenuItem(otherFishery.getName());
                        groupWithItem.setOnAction(actionEvent -> groupFisheryWith(fishery, otherFishery));
                        groupWithMenu.getItems().add(groupWithItem);
                    });
                    contextMenu.getItems().add(groupWithMenu);
                }
                contextMenu.show(this, Side.RIGHT, 0, 0);
            });
        }
    }

    private void ungroupFishery(final Fishery sourceFishery) {
        final FisheryGroup sourceGroup = fishFlag.getValues().entrySet().stream()
                .map(entry -> entry.getKey())
                .filter(group -> group.getFisheries().contains(sourceFishery))
                .reduce(null, (a, b) -> b);
        sourceGroup.setSelected(false);
        final FisheryGroup targetGroup = new FisheryGroup();
        targetGroup.setSelected(true);
        sourceProperty.set(targetGroup);
    }

    private void groupFisheryWith(final Fishery sourceFishery, final Fishery targetFishery) {
        final FisheryGroup sourceGroup = fishFlag.getValues().entrySet().stream()
                .map(entry -> entry.getKey())
                .filter(group -> group.getFisheries().contains(sourceFishery))
                .reduce(null, (a, b) -> b);
        sourceGroup.setSelected(false);
        final FisheryGroup targetGroup = fishFlag.getValues().entrySet().stream()
                .map(entry -> entry.getKey())
                .filter(group -> group.getFisheries().contains(targetFishery))
                .reduce(null, (a, b) -> b);
        targetGroup.setSelected(true);
        sourceProperty.set(targetGroup);
    }
}
