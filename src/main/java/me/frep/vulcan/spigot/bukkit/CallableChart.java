package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import java.util.concurrent.Callable;

public class CallableChart extends CustomChart {
    private final Callable callable;

    public CallableChart(String chartId, Callable callable) {
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
        for (Object entryObject : map.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObject;
            JsonArray array = new JsonArray();
            array.add(new JsonPrimitive((Number) entry.getValue()));
            values.add((String) entry.getKey(), array);
        }
        JsonObject data = new JsonObject();
        data.add("values", values);
        return data;
    }

}
