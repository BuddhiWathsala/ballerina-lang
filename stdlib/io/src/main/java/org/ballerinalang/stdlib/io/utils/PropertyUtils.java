package org.ballerinalang.stdlib.io.utils;

import org.ballerinalang.jvm.values.api.BMap;
import org.ballerinalang.jvm.values.api.BString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This class read properties.
 *
 */
public class PropertyUtils {
    private static final Logger log = LoggerFactory.getLogger(PropertyUtils.class);
    public static BString readProperty(Reader reader, BString key) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
            return org.ballerinalang.jvm.StringUtils.fromString(properties.getProperty(key.getValue()));
        } catch (IOException e) {
            log.error("error");
            return null;
        }
    }

    public static String getWritablePropertyContent(BMap<BString, BString> propertiesMap) {
        Set<Map.Entry<BString, BString>> propertiesSet = propertiesMap.entrySet();
        Iterator<Map.Entry<BString, BString>> iterator = propertiesSet.iterator();
        String content = "";
        while (iterator.hasNext()) {
            Map.Entry<BString, BString> entry = iterator.next();
            content = content +
                   entry.getKey().getValue() + "=" + entry.getValue().getValue() + "\n";
        }
        return content;
    }
}
