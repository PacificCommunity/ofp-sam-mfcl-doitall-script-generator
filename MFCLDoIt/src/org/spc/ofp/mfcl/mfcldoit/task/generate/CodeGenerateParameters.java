/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.generate;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Parameters used when generating the code.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class CodeGenerateParameters {

    int phaseNumber;
    String modelExecutable;
    String frqFile;
    boolean useRelativePath;
    String preActions;
    String postActions;
    //
    NumberFormat numberFormat = DecimalFormat.getInstance(Locale.US);

    /**
     * Create a new empty instance.
     */
    CodeGenerateParameters() {
    }
}
