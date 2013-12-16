package name.ixr.nano.common.context;

/**
 * ContentType : TODO: yuuji
 * yuuji 4:04 PM 11/20/13
 */
public enum ContentType {

    CSS("css", "text/css"),
    JS("js", "text/javascript"),
    HTML("htm", "text/html"),
    TEXT("txt", "text/plain");


    private ContentType(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public static ContentType findContentType(String key) {
        for(ContentType contentType : values()) {
            if(contentType.key().equals(key)) {
                return contentType;
            }
        }
        return null;
    }

    private String key;

    public String key() {
        return key;
    }

    private String value;

    public String value() {
        return value;
    }
}
