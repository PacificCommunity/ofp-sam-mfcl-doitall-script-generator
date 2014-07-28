/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.generate;

import org.spc.ofp.mfcl.mfcldoit.task.ProgressUpdater;
import org.spc.ofp.mfcl.mfcldoit.task.MessageUpdater;
import org.spc.ofp.mfcl.mfcldoit.task.CancelledChecker;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class that generates the shell script out of the parameter object.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class CodeGenerateUtils {

    /**
     * The line return.
     */
    private static final char ENDL = '\n'; // NOI18N.

    /**
     * Computes the number of steps in the script generation.
     * @param parameters The parameters.
     * @return A {@code long}.
     */
    public static long stepNumbers(final CodeGenerateParameters parameters) {
        return parameters.phaseNumber + 3;
    }

    /**
     * Generates the script.
     * @param parameters The parameters.
     * @return The script.
     */
    public static String generate(final CodeGenerateParameters parameters) {
        return generate(parameters, null, null, null);
    }

    /**
     * Generates the script.
     * @param parameters The parameters.
     * @param messageUpdater Receives message updates, can be {@code null}.
     * @param progressUpdater Receives progress updates, can be {@code null}.
     * @param cancelledChecker Check if task has been canceled, can be {@code null}.
     * @return The script.
     */
    public static String generate(final CodeGenerateParameters parameters, final ProgressUpdater progressUpdater, final MessageUpdater messageUpdater, final CancelledChecker cancelledChecker) {
        // Generate.
        final StringWriter result = new StringWriter();
        try (final PrintWriter out = new PrintWriter(result)) {
            ////////////////////////////////////////////////////////////////////
            // File header.
            out.printf("#!/bin/sh%s", ENDL); // NOI18N.
            out.print(ENDL);
            if (progressUpdater != null) {
                progressUpdater.updateProgress();
            }
            if (cancelledChecker != null && cancelledChecker.isCancelled()) {
                return null;
            }
            ////////////////////////////////////////////////////////////////////
            // Pre actions.
            final String preActions = parameters.preActions;
            if (preActions != null && !preActions.trim().isEmpty()) {
                out.printf("# Actions to execute before the script.%s", ENDL); // NOI18N.
                out.print(preActions);
                out.print(ENDL);
                out.print(ENDL);
            }
            if (progressUpdater != null) {
                progressUpdater.updateProgress();
            }
            if (cancelledChecker != null && cancelledChecker.isCancelled()) {
                return null;
            }
            ////////////////////////////////////////////////////////////////////
            // Phases.
            final String modelExecutable = parameters.modelExecutable;
            final String frqFile = parameters.frqFile;
            final String modelPrefix = parameters.useRelativePath ? "./" : "";
            final int phaseNumber = parameters.phaseNumber;
            for (int phaseIndex = 0; phaseIndex < phaseNumber; phaseIndex++) {
                if (cancelledChecker != null && cancelledChecker.isCancelled()) {
                    return null;
                }
                final String phaseToken = String.format("PHASE%d", phaseIndex + 1);  // NOI18N.
                // Phase header.
                out.printf("#------------------------------------------------------------------------------%s", ENDL);
                out.printf("# Phase %s%s", phaseIndex + 1, ENDL); // NOI18N.
                out.printf("#------------------------------------------------------------------------------%s", ENDL);
                // Start phase command line.
                out.printf("if [ ! -f %02d.par ]; then%s", phaseIndex + 1, ENDL);
                out.printf("  %s%s %s %02d.par %02d.par -file - <<%s%s", modelPrefix, modelExecutable, frqFile, phaseIndex,  phaseIndex + 1,  phaseToken, ENDL); // NOI18N.
                // Phase parameters.
                // End phase command line.
                out.printf("%s%s", phaseToken, ENDL); // NOI18N.
                out.printf("fi%s", ENDL); // NOI18N.
                out.print(ENDL);
                //
                if (progressUpdater != null) {
                    progressUpdater.updateProgress();
                }
                if (cancelledChecker != null && cancelledChecker.isCancelled()) {
                    return null;
                }
            }
            ////////////////////////////////////////////////////////////////////
            // Pre actions.
            final String postActions = parameters.postActions;
            if (postActions != null && !postActions.trim().isEmpty()) {
                out.printf("# Actions to execute after the script.%s", ENDL); // NOI18N.
                out.print(postActions);
                out.print(ENDL);
                out.print(ENDL);
            }
            if (progressUpdater != null) {
                progressUpdater.updateProgress();
            }
            if (cancelledChecker != null && cancelledChecker.isCancelled()) {
                return null;
            }
        }
        return result.toString();

    }
}
