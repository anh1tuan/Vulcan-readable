package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.util.concurrent.Callable;

public class SimplePie extends CustomChart {
    private final Callable callable;

    public SimplePie(String chartId, Callable callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObject getChartData() throws Exception {
        JsonObject data = new JsonObject();
        String value = (String) this.callable.call();
        if (value == null || value.isEmpty()) {
            return null;
        }
        data.addProperty("value", value);
        return data;
    }

}
