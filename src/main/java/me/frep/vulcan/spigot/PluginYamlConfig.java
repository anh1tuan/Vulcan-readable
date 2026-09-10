package me.frep.vulcan.spigot;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class PluginYamlConfig {
    private final File file;
    private final FileConfiguration config;

    public PluginYamlConfig(String name) {
        this.file = new File(VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder(), name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
