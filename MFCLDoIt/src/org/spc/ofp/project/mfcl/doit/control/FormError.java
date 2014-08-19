/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control;

import javafx.scene.Node;

/**
 * Defines an error in a form.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class FormError {

    /**
     * The error message.
     */
    private final String message;
    /**
     * The node at the source of the error.
     */
    private final Node node;

    /**
     * Creates a new instance.
     * @param message The error message.
     * @throws IllegalArgumentException If {@code message} is {@code null} or {@code empty}.
     */
    public FormError(final String message) {
        this(message, null);
    }

    /**
     * Creates a new instance.
     * @param message The error message.
     * @param node The node at the source of the error, may be {@code null}.
     * @throws IllegalArgumentException If {@code message} is {@code null} or {@code empty}.
     */
    public FormError(final String message, final Node node) throws IllegalArgumentException {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("message cannot be empty."); // NOI18N.
        }
        this.message = message.trim();
        this.node = node;
    }

    /**
     * Gets the error message.
     * @return A {@code String}, never {@code null}.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the node at the source of the error
     * @return A {@code Node}, may be {@code null}.
     */
    public Node getNode() {
        return node;
    }
}
