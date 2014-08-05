/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit.task.generate;

/**
 * Builder for code generation parameter object.
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public final class CodeGenerateParametersBuilder {

    /**
     * Hidden constructor.
     */
    private CodeGenerateParametersBuilder() {
    }

    ////////////////////////////////////////////////////////////////////////////
    /**
     * Create a new builder instance.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public static CodeGenerateParametersBuilder create() {
        return new CodeGenerateParametersBuilder()
                .modelExecutable(null);
    }

    /**
     * Build the parameter object.
     * @return A {@code GeneratorParameters} instance, never {@code null}. 
     */
    public CodeGenerateParameters build() {
        final CodeGenerateParameters result = new CodeGenerateParameters();
        result.phaseNumber = phaseNumber;
        result.modelExecutable = modelExecutable;
        result.frqFile = frqFile;
        result.iniFile = iniFile;
        result.useRelativePath = useRelativePath;
        result.preActions = preActions;
        result.postActions = postActions;
        result.includePhaseHeaders = includePhaseHeaders;
        result.includePreActionsHeader = includePreActionsHeader;
        result.includePostActionsHeader = includePostActionsHeader;
        result.makePar = makePar;
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    private int phaseNumber = 0;

    /**
     * Sets the number of phases
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder phaseNumber(final int value) {
        phaseNumber = Math.max(0, value);
        return this;
    }

    private String modelExecutable;

    /**
     * Sets the name of the model executable.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder modelExecutable(final String value) {
        modelExecutable = (value == null || value.trim().isEmpty()) ? "mfclo" : value;
        return this;
    }

    private String frqFile;

    /**
     * Sets the name of the FRQ file.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder frqFile(final String value) {
        frqFile = value;
        return this;
    }

    private String iniFile;

    /**
     * Sets the name of the INI file (for make par).
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder iniFile(final String value) {
        iniFile = value;
        return this;
    }

    private boolean useRelativePath;

    /**
     * Sets whether we use relative path for the model.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder useRelativePath(final boolean value) {
        useRelativePath = value;
        return this;
    }

    private String preActions;

    /**
     * Sets the pre-actions for this run.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder preActions(final String value) {
        preActions = value;
        return this;
    }

    private String postActions;

    /**
     * Sets the post-actions for this run.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder postActions(final String value) {
        postActions = value;
        return this;
    }

    private boolean includePhaseHeaders;

    /**
     * Sets whether phase headers should be included.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder includePhaseHeaders(final boolean value) {
        includePhaseHeaders = value;
        return this;
    }

    private boolean includePreActionsHeader;

    /**
     * Sets whether phase headers should be included.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder includePreActionsHeader(final boolean value) {
        includePreActionsHeader = value;
        return this;
    }

    private boolean includePostActionsHeader;

    /**
     * Sets whether phase headers should be included.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder includePostActionsHeader(final boolean value) {
        includePostActionsHeader = value;
        return this;
    }

    private boolean makePar;

    /**
     * Sets whether we should run the make par phase.
     * @param value The new value.
     * @return A {@code GeneratorParametersBuilder} instance, never {@code null}.
     */
    public CodeGenerateParametersBuilder makePar(final boolean value) {
        makePar = value;
        return this;
    }
}
