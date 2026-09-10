package me.frep.vulcan.spigot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public final class YamlFileHelper extends YamlConfiguration {
    private final Map commentsByPath = new HashMap();
    private boolean mergeComplete;

    public void saveSkippingRootKeys(File file, String... skippedRootKeys) {
        if (!this.mergeConfiguration(this.getConfigurationSection(""), Arrays.asList(skippedRootKeys)) || file == null) {
            return;
        }
        try { this.save(file); } catch (Exception ignored) {}
    }

    public void setComment(String path, String comment) {
        if (comment == null) {
            this.commentsByPath.remove(path);
            return;
        }
        this.commentsByPath.put(path, comment);
    }

    public String getComment(String path) {
        return this.getComment(path, null);
    }

    public String getComment(String path, String defaultValue) {
        Object comment = this.commentsByPath.get(path);
        return comment == null ? defaultValue : (String) comment;
    }

    public boolean hasComment(String path) {
        return this.getComment(path) != null;
    }

    public boolean isMergeComplete() {
        return this.mergeComplete;
    }

    @Override
    public void loadFromString(String contents) {
        try { super.loadFromString(contents); } catch (Exception ignored) {}
        this.commentsByPath.clear();
        String pendingComment = "";
        String[] lines = contents.split("\n", -1);
        for (int i = 0; i < lines.length; ++i) {
            String line = lines[i];
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                pendingComment = pendingComment + line + "\n";
                continue;
            }
            if (!trimmed.contains(":")) {
                continue;
            }
            String key = trimmed.split(":", 2)[0].trim().replace("'", "").replace("\"", "");
            if (!pendingComment.isEmpty()) {
                this.commentsByPath.put(key, pendingComment.substring(0, pendingComment.length() - 1));
                pendingComment = "";
            }
        }
    }

    @Override
    public String saveToString() {
        this.options().header(null);
        return super.saveToString();
    }

    private boolean mergeConfiguration(ConfigurationSection section, List skippedRootKeys) {
        if (section == null) {
            return false;
        }
        boolean changed = false;
        for (String key : section.getKeys(false)) {
            if (skippedRootKeys != null && skippedRootKeys.contains(key)) {
                continue;
            }
            String path = section.getCurrentPath();
            String fullPath = path == null || path.isEmpty() ? key : path + "." + key;
            if (!this.contains(fullPath)) {
                this.set(fullPath, section.get(key));
                changed = true;
            }
        }
        return changed;
    }

    private YamlFileHelper markMergeComplete() {
        this.mergeComplete = true;
        return this;
    }

    public static YamlFileHelper loadFromFile(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return YamlFileHelper.loadFromReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        } catch (FileNotFoundException fileNotFoundException) {
            Bukkit.getLogger().warning("File " + file.getName() + " does not exist.");
            return new YamlFileHelper().markMergeComplete();
        } catch (IOException exception) {
            exception.printStackTrace();
            return new YamlFileHelper().markMergeComplete();
        }
    }

    public static YamlFileHelper loadFromStream(InputStream inputStream) {
        try {
            return YamlFileHelper.loadFromReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            exception.printStackTrace();
            return new YamlFileHelper().markMergeComplete();
        }
    }

    public static YamlFileHelper loadFromReader(Reader reader) throws IOException {
        YamlFileHelper yaml = new YamlFileHelper();
        BufferedReader buffered = reader instanceof BufferedReader
            ? (BufferedReader) reader
            : new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = buffered.readLine()) != null) {
            builder.append(line).append('\n');
        }
        yaml.loadFromString(builder.toString());
        buffered.close();
        return yaml;
    }

    private static void copySectionValues(ConfigurationSection source, ConfigurationSection target) {
        for (String key : source.getKeys(false)) {
            target.set(key, source.get(key));
        }
    }

    private static void clearSection(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            section.set(key, null);
        }
    }
}
