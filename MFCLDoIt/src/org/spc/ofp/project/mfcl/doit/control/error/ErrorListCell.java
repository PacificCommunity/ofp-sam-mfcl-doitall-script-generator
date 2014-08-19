/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.control.error;

import javafx.scene.control.ListCell;
import org.spc.ofp.project.mfcl.doit.control.FormError;

/**
 * List cell that renders errors.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
final class ErrorListCell extends ListCell<FormError> {

    /**
     * Creates a new instance.
     */
    public ErrorListCell() {
        super();
    }

    @Override
    protected void updateItem(final FormError item, final boolean empty) {
        super.updateItem(item, empty);
        final String text = (empty || item == null) ? null : item.getMessage();
        setText(text);
    }
}
