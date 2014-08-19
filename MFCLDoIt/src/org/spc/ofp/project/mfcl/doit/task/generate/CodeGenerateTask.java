/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.task.generate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.concurrent.Task;

/**
 * Task that generates the file.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class CodeGenerateTask extends Task<String> {

    //
    private final NumberFormat numberFormat = DecimalFormat.getInstance(Locale.US);

    /**
     * The parameters.
     */
    private final ProjectParameters parameters;

    /**
     * Creates a new instance.
     * @param parameters The parameters.
     * @throws IllegalArgumentException If {@code parameters} is {@code null}.
     */
    public CodeGenerateTask(final ProjectParameters parameters) throws IllegalArgumentException {
        if (parameters == null) {
            throw new IllegalArgumentException("Parameters must not be null."); // NOI18N.
        }
        this.parameters = parameters;
    }

    private long currentProgress = 0;

    @Override
    protected String call() throws Exception {
        final long totalProgress = CodeGenerateUtils.stepNumbers(parameters) + 1;
        currentProgress = 0;
        // Generate.
        final String result = CodeGenerateUtils.generate(parameters, 
                () -> updateProgress(++currentProgress, totalProgress), 
                this::updateMessage, this::isCancelled);
        updateProgress(++currentProgress, totalProgress);
        if (isCancelled()) {
            return null;
        }
        return result;
    }
}
