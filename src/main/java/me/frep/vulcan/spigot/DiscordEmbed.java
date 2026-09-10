package me.frep.vulcan.spigot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Discord embed payload.
 */
public class DiscordEmbed {
    private String title;
    private String description;
    private String url;
    private Color color;
    private DiscordEmbedAuthor author;
    private DiscordEmbedThumbnail thumbnail;
    private DiscordEmbedFooter footer;
    private DiscordEmbedImage image;
    private final List fields = new ArrayList();

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public Color getColor() {
        return this.color;
    }

    public DiscordEmbedAuthor getAuthor() {
        return this.author;
    }

    public DiscordEmbedThumbnail getThumbnail() {
        return this.thumbnail;
    }

    public DiscordEmbedFooter getFooterText() {
        return this.footer;
    }

    public DiscordEmbedImage getImage() {
        return this.image;
    }

    public List getFields() {
        return this.fields;
    }

    public DiscordEmbed setTitle(String title) {
        this.title = title;
        return this;
    }

    public DiscordEmbed setDescription(String description) {
        this.description = description;
        return this;
    }

    public DiscordEmbed setUrl(String url) {
        this.url = url;
        return this;
    }

    public DiscordEmbed setColor(Color color) {
        this.color = color;
        return this;
    }

    public DiscordEmbed setAuthor(String name, String iconUrl) {
        this.author = new DiscordEmbedAuthor(this, name, iconUrl, null);
        return this;
    }

    public DiscordEmbed setThumbnailUrl(String thumbnailUrl) {
        this.thumbnail = new DiscordEmbedThumbnail(this, thumbnailUrl, null);
        return this;
    }

    public DiscordEmbed setFooter(String footerText) {
        this.footer = new DiscordEmbedFooter(this, footerText, null);
        return this;
    }

    public DiscordEmbed setImage(String imageUrl, String height, String width) {
        this.image = new DiscordEmbedImage(this, imageUrl, height, width, null);
        return this;
    }

    public DiscordEmbed addField(String name, String value, boolean inline) {
        this.fields.add(new DiscordEmbedField(this, name, value, inline, null));
        return this;
    }
}
