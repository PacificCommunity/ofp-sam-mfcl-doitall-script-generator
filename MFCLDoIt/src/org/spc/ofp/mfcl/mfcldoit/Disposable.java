/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit;

/**
 * Interface for classes that can be disposed.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public interface Disposable {

    /**
     * Dispose this object.
     * <br/>Once called the object is not in a usable state anymore.
     */
    public void dispose();
}
