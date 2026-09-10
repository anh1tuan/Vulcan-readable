package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public abstract class CustomChart {
    final String chartId;

    CustomChart(String chartId) {
        if (chartId == null || chartId.isEmpty()) {
            throw new IllegalArgumentException("Chart id cannot be null or empty!");
        }
        this.chartId = chartId;
    }

    JsonObject getRequestJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("chartId", this.chartId);
        try {
            JsonObject data = this.getChartData();
            if (data == null) {
                return null;
            }
            jsonObject.add("data", (JsonElement) data);
            return jsonObject;
        } catch (Throwable throwable) {
            if (Metrics.isLoggingFailedRequests()) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to get data for custom chart with id " + this.chartId, throwable);
            }
            return null;
        }
    }

    static JsonObject accessGetRequestJsonObject(CustomChart chart) {
        return chart.getRequestJsonObject();
    }


    protected abstract JsonObject getChartData() throws Exception;

}
