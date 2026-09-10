package me.frep.vulcan.spigot;

class DiscordEmbedThumbnail {
    private final String url;

    DiscordEmbedThumbnail(DiscordEmbed ignoredEmbed, String url) {
        this.url = url;
    }

    DiscordEmbedThumbnail(DiscordEmbed embed, String url, SchedulerTask ignored) {
        this(embed, url);
    }

    String getUrl() {
        return this.url;
    }

    static String getUrl(DiscordEmbedThumbnail thumbnail) {
        return thumbnail.getUrl();
    }
}
