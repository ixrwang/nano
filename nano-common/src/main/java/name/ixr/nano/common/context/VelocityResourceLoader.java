package name.ixr.nano.common.context;

import name.ixr.nano.common.utils.ResourceUtils;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import java.io.InputStream;

/**
 * VelocityResourceLoader : TODO: yuuji
 * yuuji 9:45 AM 12/5/13
 */
public class VelocityResourceLoader extends ResourceLoader {


    @Override
    public void init(ExtendedProperties configuration) {

    }

    @Override
    public InputStream getResourceStream(String name) {
        InputStream stream = ResourceUtils.getInputStream(name);
        return stream;
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return true;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }

}
