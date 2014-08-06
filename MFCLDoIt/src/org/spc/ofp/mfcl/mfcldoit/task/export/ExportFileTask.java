/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.export;

import java.io.BufferedWriter;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.concurrent.Task;

/**
 * Task that generates the file.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ExportFileTask extends Task<File> {

    /**
     * The parameters.
     */
    private final ExportFileParameters parameters;

    /**
     * Creates a new instance.
     * @param parameters The parameters.
     * @throws IllegalArgumentException If {@code parameters} is {@code null}.
     */
    public ExportFileTask(final ExportFileParameters parameters) throws IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters must not be null."); // NOI18N.
        }
        this.parameters = parameters;
    }

    private long currentProgress = 0;

    @Override
    protected File call() throws Exception {
        final long totalProgress = CodeGenerateUtils.stepNumbers(parameters.projectParameters) + 3;
        currentProgress = 0;
        // Check folder.
        final File folder = parameters.folder;
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create target folder.");
        } else if (!folder.isDirectory()) {
            throw new IOException("Target folder is not a directory.");
        }
        updateProgress(++currentProgress, totalProgress);
        if (isCancelled()) {
            return null;
        }
        // File.
        final File file = new File(folder, parameters.filename);
        if (file.exists() && !file.canWrite()) {
            throw new IOException("Cannot overwrite target file.");
        }
        updateProgress(++currentProgress, totalProgress);
        if (isCancelled()) {
            return null;
        }
        // Generate.
        final String text = CodeGenerateUtils.generate(parameters.projectParameters,
                () -> updateProgress(++currentProgress, totalProgress),
                this::updateMessage, this::isCancelled);
        updateProgress(++currentProgress, totalProgress);
        if (isCancelled()) {
            return null;
        }
        // Export to text.
        if (text != null) {
            final Charset encoding = parameters.encoding;
            try (final Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding))) {
                out.append(text);
            }
        }
        return file;
    }
}
