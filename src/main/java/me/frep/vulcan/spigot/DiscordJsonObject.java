package me.frep.vulcan.spigot;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal JSON object builder used by Discord webhook payloads.
 */
class DiscordJsonObject {
    private final Map jsonFields = new LinkedHashMap();

    DiscordJsonObject() {
    }

    DiscordJsonObject(DiscordWebhookClient ignoredWebhook) {
        this();
    }

    DiscordJsonObject(DiscordWebhookClient ignoredWebhook, SchedulerTask ignored) {
        this();
    }

    void put(String key, Object value) {
        if (value == null) {
            return;
        }
        this.jsonFields.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        Iterator iterator = this.jsonFields.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            builder.append(quote((String) entry.getKey())).append(':');
            appendValue(builder, entry.getValue());
            if (iterator.hasNext()) {
                builder.append(',');
            }
        }
        builder.append('}');
        return builder.toString();
    }

    private static void appendValue(StringBuilder builder, Object value) {
        if (value instanceof String) {
            builder.append(quote((String) value));
            return;
        }
        if (value instanceof Number || value instanceof Boolean || value instanceof DiscordJsonObject) {
            builder.append(value);
            return;
        }
        if (value.getClass().isArray()) {
            builder.append('[');
            int length = Array.getLength(value);
            for (int i = 0; i < length; ++i) {
                if (i > 0) {
                    builder.append(',');
                }
                appendValue(builder, Array.get(value, i));
            }
            builder.append(']');
            return;
        }
        builder.append(quote(String.valueOf(value)));
    }

    private static String quote(String value) {
        return '"' + value.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }
}
