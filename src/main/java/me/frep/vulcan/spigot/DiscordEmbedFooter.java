package me.frep.vulcan.spigot;

class DiscordEmbedFooter {
    private final String text;

    DiscordEmbedFooter(DiscordEmbed ignoredEmbed, String text) {
        this.text = text;
    }

    DiscordEmbedFooter(DiscordEmbed embed, String text, SchedulerTask ignored) {
        this(embed, text);
    }

    String getText() {
        return this.text;
    }

    static String getText(DiscordEmbedFooter footer) {
        return footer.getText();
    }
}
