package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.Callable;

public class AdvancedPie extends CustomChart {
    private final Callable callable;

    public AdvancedPie(String chartId, Callable callable) {
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
            int value = ((Number) entry.getValue()).intValue();
            if (value == 0) {
                continue;
            }
            allSkipped = false;
            values.addProperty((String) entry.getKey(), (Number) Integer.valueOf(value));
        }
        if (allSkipped) {
            return null;
        }
        JsonObject data = new JsonObject();
        data.add("values", values);
        return data;
    }

}
