/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.project.mfcl.doit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Access the program's constants values.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class MFCLDoItConstants {

    private static final MFCLDoItConstants INSTANCE = new MFCLDoItConstants();

    private final Properties versionProperties = new Properties();

    /**
     * Creates a new instance.
     */
    private MFCLDoItConstants() {
        final URL versionURL = MFCLDoItConstants.class.getResource("version.properties"); // NOI18N.
        try (final InputStream input = versionURL.openStream()) {
            versionProperties.load(input);
        } catch (IOException ex) {
            Logger.getLogger(MFCLDoItConstants.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(-1);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the unique instance of this class.
     * @return A {@code MFCLDoItConstants} instance, never {@code null}.
     */
    public static final MFCLDoItConstants getInstance() {
        return INSTANCE;
    }
    ////////////////////////////////////////////////////////////////////////////

    public String getVersion() {
        return String.format("%s.%s.%s-%s (%s)", // NOI18N.
                versionProperties.getProperty("version.major"), // NOI18N.
                versionProperties.getProperty("version.minor"), // NOI18N.
                versionProperties.getProperty("version.release"), // NOI18N.
                versionProperties.getProperty("build.number"), // NOI18N.
                versionProperties.getProperty("version.codename")).trim(); // NOI18N.
    }
}
