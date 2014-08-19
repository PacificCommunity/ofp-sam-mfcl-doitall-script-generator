/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.flag;

/**
 * Defines a fishery.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class Fishery {

    /**
     * The id of the fishery.
     */
    private final int id;
    /**
     * The name of the fishery.
     */
    private final String name;

    /**
     * Creates a new instance.
     * @param id The id of the fishery.
     * @param name The name of the fishery, if {@code null} a default name will be generated.
     * @throws IllegalArgumentException if {@code id} is &lt; 0.
     */
    public Fishery(final int id, final String name) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("id must be >= 0.");
        }
        this.id = id;
        this.name = (name == null || name.trim().isEmpty()) ? String.format("Fishery %d", id + 1) : name;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", name);
    }

    /**
     * Gets the id of this fishery.
     * @return An {@code int}.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this fishery.
     * @return An {@code String}, never {@code null}.
     */
    public String getName() {
        return name;
    }
}
