/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task;

/**
 * A functional interface that defines a method to update progress.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public interface ProgressUpdater {

    /**
     * Update progress.
     */
    public void updateProgress();
}
