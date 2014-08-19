/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.task.generate;

import org.spc.ofp.project.mfcl.doit.task.ProgressUpdater;
import org.spc.ofp.project.mfcl.doit.task.MessageUpdater;
import org.spc.ofp.project.mfcl.doit.task.CancelledChecker;
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
    public static long stepNumbers(final ProjectParameters parameters) {
        return parameters.phaseNumber + 4;
    }

    /**
     * Generates the script.
     * @param parameters The parameters.
     * @return The script.
     */
    public static String generate(final ProjectParameters parameters) {
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
    public static String generate(final ProjectParameters parameters, final ProgressUpdater progressUpdater, final MessageUpdater messageUpdater, final CancelledChecker cancelledChecker) {
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
            if (!isEmpty(preActions)) {
                if (parameters.includePreActionsHeader) {
                    out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                    out.printf("# Actions to execute before the script.%s", ENDL); // NOI18N.
                    out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                }
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
            final String modelExecutable = parameters.modelExecutable;
            final String frqFile = parameters.frqFile;
            final String iniFile = parameters.iniFile;
            final String modelPrefix = parameters.useRelativePath ? "./" : "";
            ////////////////////////////////////////////////////////////////////
            // Pre-phase.
            if (!isEmpty(modelExecutable) && !isEmpty(frqFile) && !isEmpty(iniFile) && parameters.makePar) {
                if (parameters.includePhaseHeaders) {
                    out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                    out.printf("# Phase %s%s", 0, ENDL); // NOI18N.
                    out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                }
                out.printf("%s%s %s %s 00.par -makepar%s", modelPrefix, modelExecutable, frqFile, iniFile, ENDL);
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
            final int phaseNumber = parameters.phaseNumber;
            for (int phaseIndex = 0; phaseIndex < phaseNumber; phaseIndex++) {
                if (cancelledChecker != null && cancelledChecker.isCancelled()) {
                    return null;
                }
                if (!isEmpty(modelExecutable) && !isEmpty(frqFile)) {
                    final String phaseToken = String.format("PHASE%d", phaseIndex + 1);  // NOI18N.
                    // Phase header.
                    if (parameters.includePhaseHeaders) {
                        out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                        out.printf("# Phase %s%s", phaseIndex + 1, ENDL); // NOI18N.
                        out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                    }
                    // Start phase command line.
                    out.printf("if [ ! -f %02d.par ]; then%s", phaseIndex + 1, ENDL);
                    out.printf("  %s%s %s %02d.par %02d.par -file - <<%s%s", modelPrefix, modelExecutable, frqFile, phaseIndex, phaseIndex + 1, phaseToken, ENDL); // NOI18N.
                    // Phase parameters.
                    // End phase command line.
                    out.printf("%s%s", phaseToken, ENDL); // NOI18N.
                    out.printf("fi%s", ENDL); // NOI18N.
                    out.print(ENDL);
                }
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
            if (!isEmpty(postActions)) {
                if (parameters.includePostActionsHeader) {
                    out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                    out.printf("# Actions to execute after the script.%s", ENDL); // NOI18N.
                    out.printf("#------------------------------------------------------------------------------%s", ENDL); // NOI18N.
                }
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

    private static boolean isEmpty(final String value) {
        return value == null || value.trim().isEmpty();
    }
}
