/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit.task;

/**
 * A functional interface that defines a method to update a message.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public interface MessageUpdater {
    /**
    * Update the message.
    * @param message The message.
    */
    public void updateMessage(final String message);
}
