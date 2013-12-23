package name.ixr.nano.common.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IXR on 13-12-23.
 */
public class SpringContext {
    private static final ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    static {
        context = new ClassPathXmlApplicationContext(new String[]{"classpath:spring-context.xml"});
    }
}
