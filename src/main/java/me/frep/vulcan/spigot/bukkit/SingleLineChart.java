package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonObject;
import java.util.concurrent.Callable;

public class SingleLineChart extends CustomChart {
    private final Callable callable;

    public SingleLineChart(String chartId, Callable callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObject getChartData() throws Exception {
        JsonObject data = new JsonObject();
        int value = ((Number) this.callable.call()).intValue();
        if (value == 0) {
            return null;
        }
        data.addProperty("value", (Number) Integer.valueOf(value));
        return data;
    }

}
