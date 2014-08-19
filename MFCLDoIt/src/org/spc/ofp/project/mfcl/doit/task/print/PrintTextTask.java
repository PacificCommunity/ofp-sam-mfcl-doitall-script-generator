/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.task.print;

import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.VPos;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Tasks used to print simple text.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class PrintTextTask extends Task<Void> {

    /**
     * The parameters object.
     */
    private final PrintTextParameters parameters;

    /**
     * Creates a new instance.
     * @param parameters The parameters object.
     * @throws IllegalArgumentException If {@code parameters} is {@code null} or the text to print is {@code null} or empty.
     */
    public PrintTextTask(final PrintTextParameters parameters) throws IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters cannot be null.");
        }
        if (parameters.text == null || parameters.text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty.");
        }
        this.parameters = parameters;
    }

    @Override
    protected synchronized Void call() throws Exception {
        Platform.runLater(this::setupAndPrint);
        wait();
        return null;
    }

    private synchronized void setupAndPrint() {
        final Optional<PrinterJob> jobOptional = Optional.ofNullable(PrinterJob.createPrinterJob());
        jobOptional.ifPresent(job -> {
            if (job.showPrintDialog(parameters.owner)) {
                final JobSettings jobSettings = job.getJobSettings();
                final PageLayout pageLayout = jobSettings.getPageLayout();
                final double pageWidth = pageLayout.getPrintableWidth();
                final double pageHeight = pageLayout.getPrintableHeight();
                final Text text = new Text(parameters.text);
                text.setTextOrigin(VPos.TOP);
                text.setWrappingWidth(pageWidth);
                // Font.
                final Optional<Font> fontOptional = Optional.ofNullable(parameters.font);
                fontOptional.ifPresent(font -> text.setFont(font));
                // Fill.
                final Optional<Paint> fillOptional = Optional.ofNullable(parameters.fill);
                fillOptional.ifPresent(fill -> text.setFill(fill));
                // Stroke.
                final Optional<Paint> strokeOptional = Optional.ofNullable(parameters.stroke);
                strokeOptional.ifPresent(stroke -> text.setStroke(stroke));
                // Stroke width.
                final Optional<Double> strokeWidthOptional = Optional.ofNullable(parameters.strokeWidth);
                strokeWidthOptional.ifPresent(strokeWidth -> text.setStrokeWidth(strokeWidth));
                // Text alignment.
                final Optional<TextAlignment> textAlignmentOptional = Optional.ofNullable(parameters.textAlignment);
                textAlignmentOptional.ifPresent(textAlignment -> text.setTextAlignment(textAlignment));
                // Computer number of pages.
                final double height = text.getBoundsInLocal().getHeight();
                final int pageNumber = (int) Math.ceil(height / pageHeight);
                for (int pageIndex = 0; pageIndex < pageNumber; pageIndex++) {
                    text.setTranslateY(-pageIndex * pageHeight);
                    job.printPage(text);
                }
                job.endJob();
            }
        });
        notify();
    }
}
