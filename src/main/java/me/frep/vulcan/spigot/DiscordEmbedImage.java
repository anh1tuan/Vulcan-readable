package me.frep.vulcan.spigot;

class DiscordEmbedImage {
    private final String url;
    private final String height;
    private final String width;

    DiscordEmbedImage(DiscordEmbed ignoredEmbed, String url, String height, String width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    DiscordEmbedImage(DiscordEmbed embed, String url, String height, String width, SchedulerTask ignored) {
        this(embed, url, height, width);
    }

    String getUrl() {
        return this.url;
    }

    String getHeight() {
        return this.height;
    }

    String getWidth() {
        return this.width;
    }

    static String getUrl(DiscordEmbedImage image) {
        return image.getUrl();
    }

    static String getHeight(DiscordEmbedImage image) {
        return image.getHeight();
    }

    static String getWidth(DiscordEmbedImage image) {
        return image.getWidth();
    }
}
