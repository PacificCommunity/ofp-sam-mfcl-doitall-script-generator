/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.flag;

import java.util.LinkedList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A fishery group for a fish flag.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class FisheryGroup {

    /**
     * Fisheries in this group.
     */
    private final ObservableList<Fishery> fisheries = FXCollections.observableList(new LinkedList<>());

    public ObservableList<Fishery> getFisheries() {
        return fisheries;
    }

    @Override
    public String toString() {
        return fisheries.toString();
    }

    private final BooleanProperty selected = new SimpleBooleanProperty(this, "selected", false);

    public final boolean isSelected() {
        return selected.get();
    }

    public final void setSelected(final boolean value) {
        selected.set(value);
    }

    public final BooleanProperty selectedProperty() {
        return selected;
    }
}
