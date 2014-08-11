/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.print;

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

/**
 * Parameters used when printing text.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class PrintTextParameters {

    String text;
    Font font;
    Paint fill;
    Paint stroke;
    Double strokeWidth;
    TextAlignment textAlignment;
    Window owner;
}
