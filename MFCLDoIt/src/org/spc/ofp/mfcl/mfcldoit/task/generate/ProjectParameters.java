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
public final class ProjectParameters {

    /**
     * Create a new empty instance.
     */
    ProjectParameters() {
    }

    int phaseNumber;

    public int getPhaseNumber() {
        return phaseNumber;
    }

    String modelExecutable;

    public String getModelExecutable() {
        return modelExecutable;
    }

    String frqFile;

    public String getFRQFile() {
        return frqFile;
    }

    String iniFile;

    public String getINIFile() {
        return iniFile;
    }

    boolean useRelativePath;

    public boolean isUseRelativePath() {
        return useRelativePath;
    }

    String preActions;

    public String getPreActions() {
        return preActions;
    }

    String postActions;

    public String getPostActions() {
        return postActions;
    }

    boolean includePhaseHeaders;

    public boolean isIncludePhaseHeaders() {
        return includePhaseHeaders;
    }

    boolean includePreActionsHeader;

    public boolean isIncludePreActionsHeader() {
        return includePreActionsHeader;
    }

    boolean includePostActionsHeader;

    public boolean isIncludePostActionsHeader() {
        return includePostActionsHeader;
    }

    boolean makePar;

    public boolean isUseMakePar() {
        return makePar;
    }

    NumberFormat numberFormat = DecimalFormat.getInstance(Locale.US);

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

}
