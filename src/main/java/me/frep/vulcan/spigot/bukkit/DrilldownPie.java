package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.Callable;

public class DrilldownPie extends CustomChart {
    private final Callable callable;

    public DrilldownPie(String chartId, Callable callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObject getChartData() throws Exception {
        JsonObject values = new JsonObject();
        Map map = (Map) this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        boolean allSkipped = true;
        for (Object entryObject : map.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObject;
            JsonObject nested = new JsonObject();
            Map inner = (Map) entry.getValue();
            boolean nestedSkipped = true;
            if (inner != null) {
                for (Object innerEntryObject : inner.entrySet()) {
                    Map.Entry innerEntry = (Map.Entry) innerEntryObject;
                    nestedSkipped = false;
                    nested.addProperty((String) innerEntry.getKey(), (Number) innerEntry.getValue());
                }
            }
            if (nestedSkipped) {
                continue;
            }
            allSkipped = false;
            values.add((String) entry.getKey(), nested);
        }
        if (allSkipped) {
            return null;
        }
        JsonObject data = new JsonObject();
        data.add("values", values);
        return data;
    }

}
