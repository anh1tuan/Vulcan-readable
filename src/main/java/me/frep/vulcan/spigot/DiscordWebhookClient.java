package me.frep.vulcan.spigot;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 * Discord webhook HTTP client.
 */
public class DiscordWebhookClient {
    private final String webhookUrl;
    private String content;
    private String username;
    private String avatarUrl;
    private boolean tts;
    private final List embeds = new ArrayList();

    public DiscordWebhookClient(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void addEmbed(DiscordEmbed embed) {
        if (embed != null) {
            this.embeds.add(embed);
        }
    }

    public void execute() throws IOException {
        if (this.content == null && this.embeds.isEmpty()) {
            throw new IllegalArgumentException("Must set content or add at least one embed");
        }
        DiscordJsonObject root = new DiscordJsonObject();
        root.put("content", this.content);
        root.put("username", this.username);
        root.put("avatar_url", this.avatarUrl);
        root.put("tts", Boolean.valueOf(this.tts));
        if (!this.embeds.isEmpty()) {
            List embedObjects = new ArrayList();
            for (int i = 0; i < this.embeds.size(); ++i) {
                DiscordEmbed embed = (DiscordEmbed) this.embeds.get(i);
                embedObjects.add(toEmbedJson(embed));
            }
            root.put("embeds", embedObjects.toArray());
        }
        URL url = new URL(this.webhookUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0");
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        OutputStream output = connection.getOutputStream();
        output.write(root.toString().getBytes("UTF-8"));
        output.flush();
        output.close();
        connection.getInputStream().close();
        connection.disconnect();
    }

    private static DiscordJsonObject toEmbedJson(DiscordEmbed embed) {
        DiscordJsonObject json = new DiscordJsonObject();
        json.put("title", embed.getTitle());
        json.put("description", embed.getDescription());
        json.put("url", embed.getUrl());
        if (embed.getColor() != null) {
            Color color = embed.getColor();
            int packed = (color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue();
            json.put("color", Integer.valueOf(packed));
        }
        if (embed.getAuthor() != null) {
            DiscordJsonObject author = new DiscordJsonObject();
            author.put("name", DiscordEmbedAuthor.getName(embed.getAuthor()));
            author.put("icon_url", DiscordEmbedAuthor.getIconUrl(embed.getAuthor()));
            json.put("author", author);
        }
        if (embed.getFooterText() != null) {
            DiscordJsonObject footer = new DiscordJsonObject();
            footer.put("text", DiscordEmbedFooter.getText(embed.getFooterText()));
            json.put("footer", footer);
        }
        if (embed.getThumbnail() != null) {
            DiscordJsonObject thumbnail = new DiscordJsonObject();
            thumbnail.put("url", DiscordEmbedThumbnail.getUrl(embed.getThumbnail()));
            json.put("thumbnail", thumbnail);
        }
        if (embed.getImage() != null) {
            DiscordJsonObject image = new DiscordJsonObject();
            image.put("url", DiscordEmbedImage.getUrl(embed.getImage()));
            image.put("height", DiscordEmbedImage.getHeight(embed.getImage()));
            image.put("width", DiscordEmbedImage.getWidth(embed.getImage()));
            json.put("image", image);
        }
        List fields = embed.getFields();
        if (fields != null && !fields.isEmpty()) {
            List fieldObjects = new ArrayList();
            for (int i = 0; i < fields.size(); ++i) {
                DiscordEmbedField field = (DiscordEmbedField) fields.get(i);
                DiscordJsonObject fieldJson = new DiscordJsonObject();
                fieldJson.put("name", DiscordEmbedField.getName(field));
                fieldJson.put("value", DiscordEmbedField.getValue(field));
                fieldJson.put("inline", Boolean.valueOf(DiscordEmbedField.isInline(field)));
                fieldObjects.add(fieldJson);
            }
            json.put("fields", fieldObjects.toArray());
        }
        return json;
    }
}
