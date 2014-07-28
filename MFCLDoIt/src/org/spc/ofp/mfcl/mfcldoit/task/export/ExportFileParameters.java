/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.export;

import java.io.File;
import java.nio.charset.Charset;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateParameters;

/**
 * Parameters used when exporting to file.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ExportFileParameters {
    public static final Charset DEFAULT_ENCODING = Charset.forName("utf-8"); // NOI18N.

    File folder;
    String filename;
    CodeGenerateParameters codeGenerateParameters;
    Charset encoding = DEFAULT_ENCODING;

    /**
     * Create a new empty instance.
     */
    ExportFileParameters() {
    }
}
