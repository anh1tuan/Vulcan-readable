package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.util.concurrent.Callable;

public class CustomChartData extends CustomChart {
    private final Callable callable;

    public CustomChartData(String chartId, Callable callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObject getChartData() throws Exception {
        JsonObject data = new JsonObject();
        String value = String.valueOf(this.callable.call());
        if (value == null || value.isEmpty() || "null".equals(value)) {
            return null;
        }
        data.addProperty("value", value);
        return data;
    }

}
