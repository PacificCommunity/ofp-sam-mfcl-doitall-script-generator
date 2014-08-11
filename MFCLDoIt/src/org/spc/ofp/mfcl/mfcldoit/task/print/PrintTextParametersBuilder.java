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
 * Builder class for the parameters object.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class PrintTextParametersBuilder {

    /**
     * Hidden constructor.
     */
    private PrintTextParametersBuilder() {
    }

    /**
     * Creates a new instance.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public static PrintTextParametersBuilder create() {
        return new PrintTextParametersBuilder();
    }

    /**
     * Build the parameters object.
     * @return A {@code PrintTextParameters} instance, never {@code null}.
     */
    public PrintTextParameters build() {
        final PrintTextParameters result = new PrintTextParameters();
        result.text = text;
        result.textAlignment = textAlignment;
        result.font = font;
        result.fill = fill;
        result.stroke = stroke;
        result.strokeWidth = strokeWidth;
        result.owner = owner;
        return result;
    }

    private String text;

    /**
     * Sets the text.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder text(final String value) {
        text = value;
        return this;
    }

    private TextAlignment textAlignment;

    /**
     * Sets the text alignment.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder textAlignment(final TextAlignment value) {
        textAlignment = value;
        return this;
    }

    private Font font;

    /**
     * Sets the font.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder font(final Font value) {
        font = value;
        return this;
    }

    private Paint fill;

    /**
     * Sets the fill.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder fill(final Paint value) {
        fill = value;
        return this;
    }

    private Paint stroke;

    /**
     * Sets the stroke.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder stroke(final Paint value) {
        stroke = value;
        return this;
    }

    private Double strokeWidth;

    /**
     * Sets the stroke width.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder strokeWidth(final Double value) {
        strokeWidth = value;
        return this;
    }

    private Window owner;

    /**
     * Sets the owner window.
     * @param value The new value.
     * @return A {@code PrintTextParametersBuilder} instance, never {@code null}.
     */
    public PrintTextParametersBuilder owner(final Window value) {
        owner = value;
        return this;
    }
}
