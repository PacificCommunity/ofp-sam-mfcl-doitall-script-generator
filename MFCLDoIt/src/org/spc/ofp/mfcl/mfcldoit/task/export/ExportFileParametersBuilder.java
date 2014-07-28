/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.export;

import java.io.File;
import java.nio.charset.Charset;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateParameters;
import org.spc.ofp.mfcl.mfcldoit.task.generate.CodeGenerateParametersBuilder;

/**
 * Builder for parameter object.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class ExportFileParametersBuilder {

    /**
     * Hidden constructor.
     */
    private ExportFileParametersBuilder() {
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new builder instance.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public static ExportFileParametersBuilder create() {
        return new ExportFileParametersBuilder()
                .folder(null)
                .filename(null)
                .codeGenerateParameters(null);
    }

    /**
     * Build the parameter object.
     * @return A {@code GeneratorParameters} instance, never {@code null}. 
     */
    public ExportFileParameters build() {
        final ExportFileParameters result = new ExportFileParameters();
        result.folder = folder;
        result.filename = filename;
        result.codeGenerateParameters = codeGenerateParameters;
        result.encoding = encoding;
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    private File folder;

    /**
     * Sets the output folder.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public ExportFileParametersBuilder folder(final File value) {
        folder = (value == null) ? new File(System.getProperty("user.dir")) : value; // NOI18N.
        return this;
    }

    private String filename;

    /**
     * Sets the name of the output file.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public ExportFileParametersBuilder filename(final String value) {
        filename = (value == null || value.trim().isEmpty()) ? "test.sh" : value; // NOI18N.
        return this;
    }

    private CodeGenerateParameters codeGenerateParameters;

    /**
     * Sets the code generator
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public ExportFileParametersBuilder codeGenerateParameters(final CodeGenerateParameters value) {
        codeGenerateParameters = (value == null) ? CodeGenerateParametersBuilder.create().build() : value;
        return this;
    }

    private Charset encoding = ExportFileParameters.DEFAULT_ENCODING;

    /**
     * Sets the encoding to be used when writing the file.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public ExportFileParametersBuilder encoding(final Charset value) {
        encoding = (value == null) ? ExportFileParameters.DEFAULT_ENCODING : value;
        return this;
    }

}
