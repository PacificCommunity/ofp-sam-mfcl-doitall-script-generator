/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.flag;

/**
 * Base class for a flag.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public abstract class FlagBase {

    /**
     * The type of this flag.
     */
    private final FlagType type;
    /**
     * The id of this flag.
     */
    private final int id;

    /**
     * Creates a new instance.
     * @param type The type of this flag.
     * @param id The id of this flag.
     * @throws IllegalArgumentException  If {@code type} is {@code null} or if {@code id} &le; 0.
     */
    public FlagBase(final FlagType type, final int id) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("id must be > 1.");
        }
        this.type = type;
        this.id = id;
    }

    /**
     * Gets the id of this flag.
     * @return An {@code int}.
     */
    public final int getId() {
        return id;
    }

    /**
     * Gets the type of this flag.
     * @return A {@code FlagType} instance, never {@code null}.
     */
    public final FlagType getType() {
        return type;
    }
}
