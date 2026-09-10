package me.frep.vulcan.spigot;

class DiscordEmbedAuthor {
    private final String name;
    private final String iconUrl;

    DiscordEmbedAuthor(DiscordEmbed ignoredEmbed, String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    DiscordEmbedAuthor(DiscordEmbed embed, String name, String iconUrl, SchedulerTask ignored) {
        this(embed, name, iconUrl);
    }

    String getName() {
        return this.name;
    }

    String getIconUrl() {
        return this.iconUrl;
    }

    static String getName(DiscordEmbedAuthor author) {
        return author.getName();
    }

    static String getIconUrl(DiscordEmbedAuthor author) {
        return author.getIconUrl();
    }
}
