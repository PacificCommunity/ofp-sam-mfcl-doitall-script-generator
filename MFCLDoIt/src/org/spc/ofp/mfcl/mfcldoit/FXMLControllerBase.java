/***********************************************************************
 *  Copyright - Secretariat of the Pacific Community                   *
 *  Droit de copie - Secrétariat Général de la Communauté du Pacifique *
 *  http://www.spc.int/                                                *
 ***********************************************************************/
package org.spc.ofp.mfcl.mfcldoit;

import java.net.URL;
import java.util.Optional;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;

/**
 * Base class for controllers
 * @author Fabrice Bouyé (fabriceb@spc.int)
 */
public abstract class FXMLControllerBase implements Initializable, Disposable {

    /**
     * Creates a new instance.
     */
    public FXMLControllerBase() {
    }

    @Override
    public void dispose() {
        application.unbind();
        setApplication(null);
    }

    ////////////////////////////////////////////////////////////////////////////
    public void browse(final URL url) {
        browse(url.toExternalForm());
    }

    public void browse(final String url) {
        final Optional<MFCLDoIt> applicationOptional = Optional.ofNullable(getApplication());
        applicationOptional.ifPresent(application -> application.getHostServices().showDocument(url));
    }

    ////////////////////////////////////////////////////////////////////////////
    private final ObjectProperty<MFCLDoIt> application = new SimpleObjectProperty<>(this, "application"); // NOI18N.

    public final MFCLDoIt getApplication() {
        return application.get();
    }

    public final void setApplication(final MFCLDoIt value) {
        application.set(value);
    }

    public final ObjectProperty<MFCLDoIt> applicationProperty() {
        return application;
    }
}
