package me.frep.vulcan.spigot.bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class Metrics {
    private final ThreadFactory threadFactory = Metrics::lambda$new$0;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, this.threadFactory);
    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private boolean enabled;
    private static boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static String serverUUID;
    private final Plugin plugin;
    private final int pluginId;
    private final List charts = new ArrayList();

    public Metrics(Plugin plugin, int pluginId) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        this.pluginId = pluginId;
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);

        if (!yamlConfiguration.isSet("serverUuid")) {
            yamlConfiguration.addDefault("enabled", (Object) true);
            yamlConfiguration.addDefault("serverUuid", (Object) UUID.randomUUID().toString());
            yamlConfiguration.addDefault("logFailedRequests", (Object) false);
            yamlConfiguration.addDefault("logSentData", (Object) false);
            yamlConfiguration.addDefault("logResponseStatusText", (Object) false);
            yamlConfiguration.options().header("Plugin usage metrics (bStats).").copyDefaults(true);
            try {
                yamlConfiguration.save(configFile);
            } catch (IOException ignored) {
            }
        }

        this.enabled = yamlConfiguration.getBoolean("enabled", true);
        serverUUID = yamlConfiguration.getString("serverUuid");
        logFailedRequests = yamlConfiguration.getBoolean("logFailedRequests", false);
        logSentData = yamlConfiguration.getBoolean("logSentData", false);
        logResponseStatusText = yamlConfiguration.getBoolean("logResponseStatusText", false);
        if (!this.enabled) {
            return;
        }
        boolean alreadyRegistered = false;
        for (Class service : Bukkit.getServicesManager().getKnownServices()) {
            try {
                service.getField("B_STATS_VERSION");
                alreadyRegistered = true;
                break;
            } catch (NoSuchFieldException ignored) {
            }
        }

        Bukkit.getServicesManager().register(Metrics.class, this, plugin, ServicePriority.Normal);
        if (alreadyRegistered) {
            return;
        }
        this.startSubmitting();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void addCustomChart(CustomChart chart) {
        if (chart == null) {
            throw new IllegalArgumentException("Chart cannot be null!");
        }
        this.charts.add(chart);
    }

    private void startSubmitting() {
        Runnable runnable = this::lambda$startSubmitting$1;
        long initialDelay = (long) (60000.0 * (3.0 + Math.random() * 3.0));
        long secondDelay = (long) (60000.0 * (Math.random() * 30.0));
        this.scheduler.schedule(runnable, initialDelay, TimeUnit.MILLISECONDS);
        this.scheduler.scheduleAtFixedRate(runnable, initialDelay + secondDelay, 1800000L, TimeUnit.MILLISECONDS);
    }

    public JsonObject getPluginData() {
        JsonObject jsonObject = new JsonObject();
        String string = this.plugin.getDescription().getName();
        String string2 = this.plugin.getDescription().getVersion();
        jsonObject.addProperty("pluginName", string);
        jsonObject.addProperty("id", (Number)this.pluginId);
        jsonObject.addProperty("pluginVersion", string2);
        JsonArray jsonArray = new JsonArray();
        for (Object chartObject : this.charts) {
            JsonObject jsonObject2 = ((CustomChart) chartObject).getRequestJsonObject();
            if (jsonObject2 == null) {
                continue;
            }
            jsonArray.add((JsonElement) jsonObject2);
        }
        jsonObject.add("customCharts", (JsonElement)jsonArray);
        return jsonObject;
    }

    private JsonObject getServerData() {
        int playerCount;
        try {
            Method method = Bukkit.class.getMethod("getOnlinePlayers");
            if (method.getReturnType().equals(Collection.class)) {
                playerCount = ((Collection) method.invoke(Bukkit.getServer())).size();
            } else {
                playerCount = ((Player[]) method.invoke(Bukkit.getServer())).length;
            }
        } catch (Exception ignored) {
            playerCount = Bukkit.getOnlinePlayers().size();
        }
        int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serverUUID", serverUUID);
        jsonObject.addProperty("playerAmount", (Number) playerCount);
        jsonObject.addProperty("onlineMode", (Number) onlineMode);
        jsonObject.addProperty("bukkitVersion", Bukkit.getVersion());
        jsonObject.addProperty("bukkitName", Bukkit.getName());
        jsonObject.addProperty("javaVersion", System.getProperty("java.version"));
        jsonObject.addProperty("osName", System.getProperty("os.name"));
        jsonObject.addProperty("osArch", System.getProperty("os.arch"));
        jsonObject.addProperty("osVersion", System.getProperty("os.version"));
        jsonObject.addProperty("coreCount", (Number) Runtime.getRuntime().availableProcessors());
        return jsonObject;
    }

    private void submitData() {
        JsonObject jsonObject = this.getServerData();
        JsonArray jsonArray = new JsonArray();
        for (Class service : Bukkit.getServicesManager().getKnownServices()) {
            try {
                service.getField("B_STATS_VERSION");
                for (RegisteredServiceProvider registeredServiceProvider : (java.util.Collection<RegisteredServiceProvider>) (java.util.Collection) Bukkit.getServicesManager().getRegistrations(service)) {
                    try {
                        Object object = service.getMethod("getPluginData").invoke(registeredServiceProvider.getProvider());

                        if (object instanceof JsonObject) {
                            jsonArray.add((JsonElement)((JsonObject)object));
                            continue;
                        }

                        try {
                            Class<?> jsonObjectClass = Class.forName("org.json.simple.JSONObject");
                            if (!jsonObjectClass.isAssignableFrom(object.getClass())) {
                                continue;
                            }
                            Method method = jsonObjectClass.getDeclaredMethod("toJSONString");
                            method.setAccessible(true);
                            String json = (String) method.invoke(object);
                            jsonArray.add(new JsonParser().parse(json).getAsJsonObject());
                        }
                    catch (ClassNotFoundException classNotFoundException) {
                            if (!logFailedRequests) continue;
                            this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception", classNotFoundException);

                        }
                    }
                catch (IllegalAccessException | NoSuchMethodException | NullPointerException | InvocationTargetException exception) {
                    }
                }
            }
        catch (NoSuchFieldException noSuchFieldException) {
            }
        }
        jsonObject.add("plugins", (JsonElement)jsonArray);
        new Thread(() -> this.lambda$submitData$2(jsonObject)).start();
    }
    private static void sendData(Plugin plugin, JsonObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }
        if (logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + data);
        }
        HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();
        byte[] compressedData = compress(data.toString());
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(compressedData);
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }
        bufferedReader.close();
        if (logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + builder);
        }
    }

    static boolean isLoggingFailedRequests() {
        return logFailedRequests;
    }

    private static byte[] compress(String text) throws IOException {
        if (text == null) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(output)) {
            gzip.write(text.getBytes(StandardCharsets.UTF_8));
        }
        return output.toByteArray();
    }

    private void lambda$submitData$2(JsonObject jsonObject) {
        try {
            Metrics.sendData(this.plugin, jsonObject);
        } catch (Exception exception) {
            if (logFailedRequests) {
                this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), exception);
            }
        }
    }

    private void lambda$startSubmitting$1() {
        if (!this.plugin.isEnabled()) {
            this.scheduler.shutdown();
            return;
        }
        Bukkit.getScheduler().runTask(this.plugin, this::submitData);
    }

    private static Thread lambda$new$0(Runnable runnable) {
        return new Thread(runnable, "bStats-Metrics");
    }

    static {
        if (System.getProperty("bstats.relocatecheck") == null
            || !System.getProperty("bstats.relocatecheck").equals("false")) {
            String unrelocated = "org.bstats.bukkit";
            if (Metrics.class.getPackage().getName().equals(unrelocated)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }
}

