package name.ixr.nano.common.config;

import java.util.List;

/**
 * SegmentConfig : TODO: yuuji
 * yuuji 12:10 PM 11/25/13
 */
public class SegmentConfig {
    private String layout;
    private List<String> apps;

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }
}
