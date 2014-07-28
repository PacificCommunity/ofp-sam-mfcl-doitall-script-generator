/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task;

/**
 * A functional interface that defines a method that tests if a task has been cancelld.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public interface CancelledChecker {

    /**
     * Test if the task has been canceled.
     * @return {@code True} if the task has been canceled, {@code false} otherwise.
     */
    public boolean isCancelled();
}
