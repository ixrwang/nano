package name.ixr.nano.common.spi;

/**
 * Created by IXR on 13-12-23.
 */
public abstract class UrlRoutingSPI {

    public abstract void invoke() throws Exception;

    public abstract String route();
}
