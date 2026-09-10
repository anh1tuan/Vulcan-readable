package me.frep.vulcan.spigot;

class DiscordEmbedField {
    private final String name;
    private final String value;
    private final boolean inline;

    DiscordEmbedField(DiscordEmbed ignoredEmbed, String name, String value, boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }

    DiscordEmbedField(DiscordEmbed embed, String name, String value, boolean inline, SchedulerTask ignored) {
        this(embed, name, value, inline);
    }

    String getName() {
        return this.name;
    }

    String getValue() {
        return this.value;
    }

    boolean isInline() {
        return this.inline;
    }

    static String getName(DiscordEmbedField field) {
        return field.getName();
    }

    static String getValue(DiscordEmbedField field) {
        return field.getValue();
    }

    static boolean isInline(DiscordEmbedField field) {
        return field.isInline();
    }
}
