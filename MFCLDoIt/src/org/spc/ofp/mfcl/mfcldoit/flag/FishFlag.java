/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.flag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * Defines a fish(ery) flag.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class FishFlag extends FlagBase {

    /**
     * Creates a new instance.
     * @param id The id of this flag.
     * @throws IllegalArgumentException If {@code id} &le; 0.
     */
    public FishFlag(final int id) throws IllegalArgumentException {
        super(FlagType.FISH, id);
    }

    /**
    * Values for each groups in this flag.
    */
    private final ObservableMap<FisheryGroup, Number> values = FXCollections.observableHashMap();

    public final ObservableMap<FisheryGroup, Number> getValues() {
        return values;
    }

}
