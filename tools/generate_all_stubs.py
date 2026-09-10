#!/usr/bin/env python3
import subprocess
import shutil
from pathlib import Path

ROOT = Path(r"D:\Dev\AcV2")
STUB_ROOT = ROOT / "tools" / "stubs" / "src"
OUT_JAR = ROOT / "tools" / "stubs.jar"
CLASSES_DIR = ROOT / "tools" / "stubs" / "classes"

if STUB_ROOT.exists():
    shutil.rmtree(STUB_ROOT)
STUB_ROOT.mkdir(parents=True)

if CLASSES_DIR.exists():
    shutil.rmtree(CLASSES_DIR)
CLASSES_DIR.mkdir(parents=True)

def write_file(rel_path, content):
    p = STUB_ROOT / rel_path
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content, encoding="utf-8")

# 1. JetBrains annotations
write_file("org/jetbrains/annotations/NotNull.java", """package org.jetbrains.annotations;
import java.lang.annotation.*;
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.CLASS)
public @interface NotNull {}
""")

write_file("org/jetbrains/annotations/Nullable.java", """package org.jetbrains.annotations;
import java.lang.annotation.*;
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.CLASS)
public @interface Nullable {}
""")

# 2. Javax annotation
write_file("javax/annotation/Nullable.java", """package javax.annotation;
import java.lang.annotation.*;
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.CLASS)
public @interface Nullable {}
""")

# 3. PlaceholderAPI
write_file("me/clip/placeholderapi/PlaceholderAPI.java", """package me.clip.placeholderapi;
import org.bukkit.entity.Player;
public class PlaceholderAPI {
    public static String setPlaceholders(Player p, String s) { return s; }
}
""")
write_file("me/clip/placeholderapi/expansion/PlaceholderExpansion.java", """package me.clip.placeholderapi.expansion;
import org.bukkit.entity.Player;
public abstract class PlaceholderExpansion {
    public abstract String getIdentifier();
    public abstract String getAuthor();
    public abstract String getVersion();
    public boolean register() { return true; }
    public String onPlaceholderRequest(Player p, String s) { return null; }
}
""")

# 4. Floodgate
write_file("org/geysermc/floodgate/api/FloodgateApi.java", """package org.geysermc.floodgate.api;
import java.util.UUID;
public class FloodgateApi {
    public static FloodgateApi getInstance() { return new FloodgateApi(); }
    public boolean isFloodgatePlayer(UUID uuid) { return false; }
}
""")
write_file("org/geysermc/floodgate/FloodgateAPI.java", """package org.geysermc.floodgate;
import java.util.UUID;
public class FloodgateAPI {
    public static boolean isBedrockPlayer(UUID uuid) { return false; }
    public static boolean isBedrockPlayer(org.bukkit.entity.Player player) { return false; }
}
""")

# 5. McMMO
write_file("com/gmail/nossr50/datatypes/skills/SuperAbilityType.java", """package com.gmail.nossr50.datatypes.skills;
public enum SuperAbilityType { TREE_FELLER, SUPER_BREAKER, GIGA_DRILL_BREAKER, BERSERK, BLAST_MINING }
""")
write_file("com/gmail/nossr50/events/skills/abilities/McMMOPlayerAbilityActivateEvent.java", """package com.gmail.nossr50.events.skills.abilities;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import com.gmail.nossr50.datatypes.skills.SuperAbilityType;
public class McMMOPlayerAbilityActivateEvent extends Event {
    public Player getPlayer() { return null; }
    public SuperAbilityType getAbility() { return null; }
}
""")
write_file("com/gmail/nossr50/events/skills/abilities/McMMOPlayerAbilityDeactivateEvent.java", """package com.gmail.nossr50.events.skills.abilities;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import com.gmail.nossr50.datatypes.skills.SuperAbilityType;
public class McMMOPlayerAbilityDeactivateEvent extends Event {
    public Player getPlayer() { return null; }
    public SuperAbilityType getAbility() { return null; }
}
""")

# 6. Bungee chat
write_file("net/md_5/bungee/api/ChatColor.java", """package net.md_5.bungee.api;
public enum ChatColor {
    BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, MAGIC, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET;
    public static String translateAlternateColorCodes(char c, String s) { return s; }
    public static ChatColor of(String s) { return WHITE; }
}
""")
write_file("net/md_5/bungee/api/chat/BaseComponent.java", """package net.md_5.bungee.api.chat;
public abstract class BaseComponent {
    public void setHoverEvent(HoverEvent e) {}
    public void setClickEvent(ClickEvent e) {}
}
""")
write_file("net/md_5/bungee/api/chat/TextComponent.java", """package net.md_5.bungee.api.chat;
public class TextComponent extends BaseComponent {
    private String text;
    public TextComponent() { this(""); }
    public TextComponent(String text) { this.text = text; }
    public TextComponent(BaseComponent... extras) {}
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public static BaseComponent[] fromLegacyText(String message) { return new BaseComponent[] { new TextComponent(message) }; }
}""")
write_file("net/md_5/bungee/api/chat/ClickEvent.java", """package net.md_5.bungee.api.chat;
public class ClickEvent {
    public enum Action { OPEN_URL, OPEN_FILE, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE }
    public ClickEvent(Action action, String value) {}
}
""")
write_file("net/md_5/bungee/api/chat/HoverEvent.java", """package net.md_5.bungee.api.chat;
public class HoverEvent {
    public enum Action { SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM, SHOW_ENTITY }
    public HoverEvent(Action action, BaseComponent[] value) {}
}""")

# 7. Gson
write_file("com/google/gson/JsonElement.java", """package com.google.gson;
public abstract class JsonElement {
    public JsonObject getAsJsonObject() { return (JsonObject)this; }
    public JsonArray getAsJsonArray() { return (JsonArray)this; }
    public String getAsString() { return ""; }
    public int getAsInt() { return 0; }
    public boolean getAsBoolean() { return false; }
}
""")
write_file("com/google/gson/JsonObject.java", """package com.google.gson;
import java.util.Set;
import java.util.Map;
public class JsonObject extends JsonElement {
    public void addProperty(String k, String v) {}
    public void addProperty(String k, Number v) {}
    public void addProperty(String k, Boolean v) {}
    public void add(String k, JsonElement v) {}
    public JsonElement get(String k) { return null; }
    public boolean has(String k) { return false; }
    public Set<Map.Entry<String, JsonElement>> entrySet() { return null; }
}
""")
write_file("com/google/gson/JsonArray.java", """package com.google.gson;
import java.util.Iterator;
public class JsonArray extends JsonElement implements Iterable<JsonElement> {
    public void add(JsonElement e) {}
    public void add(String s) {}
    public int size() { return 0; }
    public JsonElement get(int i) { return null; }
    public Iterator<JsonElement> iterator() { return null; }
}
""")
write_file("com/google/gson/JsonPrimitive.java", """package com.google.gson;
public class JsonPrimitive extends JsonElement {
    public JsonPrimitive(String s) {}
    public JsonPrimitive(Number n) {}
    public JsonPrimitive(Boolean b) {}
}
""")
write_file("com/google/gson/JsonParser.java", """package com.google.gson;
public class JsonParser {
    public static JsonElement parseString(String s) { return null; }
    public JsonElement parse(String s) { return null; }
}
""")

# 8. Guava
write_file("com/google/common/collect/AbstractIterator.java", """package com.google.common.collect;
import java.util.Iterator;
public abstract class AbstractIterator<T> implements Iterator<T> {
    protected abstract T computeNext();
    protected T endOfData() { return null; }
    public boolean hasNext() { return false; }
    public T next() { return null; }
}
""")
write_file("com/google/common/collect/Iterators.java", """package com.google.common.collect;
import java.util.Iterator;
public class Iterators {
    public static <T> Iterator<T> emptyIterator() { return null; }
}
""")
write_file("com/google/common/collect/Lists.java", """package com.google.common.collect;
import java.util.ArrayList;
public class Lists {
    public static <E> ArrayList<E> newArrayList() { return new ArrayList<E>(); }
    public static <E> java.util.LinkedList<E> newLinkedList() { return new java.util.LinkedList<E>(); }
}
""")
write_file("com/google/common/collect/Maps.java", """package com.google.common.collect;
import java.util.HashMap;
public class Maps {
    public static <K, V> HashMap<K, V> newHashMap() { return new HashMap<K, V>(); }
}
""")
write_file("com/google/common/base/Predicate.java", """package com.google.common.base;
public interface Predicate<T> {
    boolean apply(T input);
}
""")
write_file("com/google/common/io/ByteArrayDataOutput.java", """package com.google.common.io;
public interface ByteArrayDataOutput {
    void write(int b);
    void write(byte[] b);
    void write(byte[] b, int off, int len);
    void writeBoolean(boolean v);
    void writeByte(int v);
    void writeShort(int v);
    void writeChar(int v);
    void writeInt(int v);
    void writeLong(long v);
    void writeFloat(float v);
    void writeDouble(double v);
    void writeBytes(String s);
    void writeChars(String s);
    void writeUTF(String s);
    byte[] toByteArray();
}""")
write_file("com/google/common/io/ByteStreams.java", """package com.google.common.io;
public class ByteStreams {
    public static ByteArrayDataOutput newDataOutput() { return null; }
}
""")

# 9. PacketEvents
write_file("com/github/retrooper/packetevents/PacketEvents.java", """package com.github.retrooper.packetevents;
import com.github.retrooper.packetevents.event.PacketListener;
public class PacketEvents {
    public static PacketEventsAPI<?> getAPI() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/PacketEventsAPI.java", """package com.github.retrooper.packetevents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.event.EventManager;
public interface PacketEventsAPI<T> {
    com.github.retrooper.packetevents.manager.protocol.ProtocolManager getProtocolManager();
    void init();
    void terminate();
    EventManager getEventManager();
    ServerManager getServerManager();
    PlayerManager getPlayerManager();
    Settings getSettings();
}
""")
write_file("com/github/retrooper/packetevents/Settings.java", """package com.github.retrooper.packetevents;
public class Settings {
    public Settings checkForUpdates(boolean b) { return this; }
    public Settings bStats(boolean b) { return this; }
}
""")
write_file("com/github/retrooper/packetevents/event/EventManager.java", """package com.github.retrooper.packetevents.event;
public interface EventManager {
    void registerListener(PacketListener l);
    void registerListener(PacketListener l, PacketListenerPriority p);
    void unregisterListener(PacketListener l);
}
""")
write_file("com/github/retrooper/packetevents/manager/server/ServerManager.java", """package com.github.retrooper.packetevents.manager.server;
public interface ServerManager {
    ServerVersion getVersion();
}
""")
write_file("com/github/retrooper/packetevents/manager/server/ServerVersion.java", """package com.github.retrooper.packetevents.manager.server;
public enum ServerVersion {
    V_1_7_10, V_1_8, V_1_8_3, V_1_8_8, V_1_9, V_1_11, V_1_12, V_1_12_2, V_1_13, V_1_14, V_1_16, V_1_16_5, V_1_17, V_1_18, V_1_19, V_1_20, V_1_20_2, V_1_21, V_1_21_2;
    public boolean isNewerThanOrEquals(ServerVersion v) { return true; }
    public boolean isNewerThan(ServerVersion v) { return true; }
    public boolean isOlderThan(ServerVersion v) { return false; }
    public String getReleaseName() { return name(); }
}
""")

write_file("com/github/retrooper/packetevents/manager/protocol/ProtocolManager.java", """package com.github.retrooper.packetevents.manager.protocol;
public interface ProtocolManager {
    void sendPacket(Object channel, Object packet);
}
""")

write_file("com/github/retrooper/packetevents/manager/player/PlayerManager.java", """package com.github.retrooper.packetevents.manager.player;
import com.github.retrooper.packetevents.protocol.player.User;
public interface PlayerManager {
    User getUser(Object player);
    int getPing(Object player);
}
""")

write_file("com/github/retrooper/packetevents/event/PacketReceiveEvent.java", """package com.github.retrooper.packetevents.event;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
public class PacketReceiveEvent extends PacketEvent {
    public com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client getPacketType() { return null; }
    public Object getByteBuf() { return null; }
    public long getTimestamp() { return System.currentTimeMillis(); }
}
""")

write_file("com/github/retrooper/packetevents/event/PacketSendEvent.java", """package com.github.retrooper.packetevents.event;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import java.util.List;
import java.util.ArrayList;
public class PacketSendEvent extends PacketEvent {
    private final List<Runnable> tasks = new ArrayList<>();
    public PacketTypeCommon getPacketType() { return null; }
    public List<Runnable> getTasksAfterSend() { return tasks; }
    public long getTimestamp() { return System.currentTimeMillis(); }
}
""")

write_file("com/github/retrooper/packetevents/event/UserConnectEvent.java", """package com.github.retrooper.packetevents.event;
public class UserConnectEvent extends PacketEvent {}
""")

write_file("com/github/retrooper/packetevents/event/UserDisconnectEvent.java", """package com.github.retrooper.packetevents.event;
public class UserDisconnectEvent extends PacketEvent {}
""")

write_file("com/github/retrooper/packetevents/event/PacketListener.java", """package com.github.retrooper.packetevents.event;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
public interface PacketListener {
    default void onPacketPlayReceive(PacketPlayReceiveEvent e) {}
    default void onPacketPlaySend(PacketPlaySendEvent e) {}
    default void onPacketReceive(PacketReceiveEvent e) {}
    default void onPacketSend(PacketSendEvent e) {}
    default void onUserConnect(UserConnectEvent e) {}
    default void onUserDisconnect(UserDisconnectEvent e) {}
}
""")
write_file("com/github/retrooper/packetevents/event/PacketListenerPriority.java", """package com.github.retrooper.packetevents.event;
public enum PacketListenerPriority { LOWEST, LOW, NORMAL, HIGH, HIGHEST, MONITOR }
""")
write_file("com/github/retrooper/packetevents/event/PacketEvent.java", """package com.github.retrooper.packetevents.event;
import com.github.retrooper.packetevents.protocol.player.User;
public class PacketEvent {
    public User getUser() { return null; }
    public Object getPlayer() { return null; }
    public boolean isCancelled() { return false; }
    public void setCancelled(boolean b) {}
}
""")
write_file("com/github/retrooper/packetevents/event/simple/PacketPlayReceiveEvent.java", """package com.github.retrooper.packetevents.event.simple;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
public class PacketPlayReceiveEvent extends PacketEvent {
    public PacketTypeCommon getPacketType() { return null; }
    public Object getByteBuf() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/event/simple/PacketPlaySendEvent.java", """package com.github.retrooper.packetevents.event.simple;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
public class PacketPlaySendEvent extends PacketEvent {
    public PacketTypeCommon getPacketType() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/protocol/player/User.java", """package com.github.retrooper.packetevents.protocol.player;
import java.util.UUID;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.protocol.ConnectionState;

public interface User {
    UUID getUUID();
    String getName();
    ClientVersion getClientVersion();
    ConnectionState getEncoderState();
    ConnectionState getDecoderState();
    ConnectionState getConnectionState();
    Object getChannel();
    int getPing();
    void sendPacket(PacketWrapper<?> packet);
    void sendPacket(Object packet);
    void sendPacketSilently(PacketWrapper<?> packet);
    void closeConnection();
}""")
write_file("com/github/retrooper/packetevents/protocol/ConnectionState.java", """package com.github.retrooper.packetevents.protocol;
public enum ConnectionState { HANDSHAKING, STATUS, LOGIN, CONFIGURATION, PLAY }
""")
write_file("com/github/retrooper/packetevents/protocol/player/ClientVersion.java", """package com.github.retrooper.packetevents.protocol.player;
public enum ClientVersion {
    UNKNOWN, LOWER_THAN_SUPPORTED,
    V_1_7, V_1_7_10,
    V_1_8, V_1_8_8,
    V_1_9, V_1_9_2, V_1_9_4,
    V_1_10,
    V_1_11,
    V_1_12, V_1_12_2,
    V_1_13, V_1_13_2,
    V_1_14, V_1_14_4,
    V_1_15, V_1_15_2,
    V_1_16, V_1_16_1, V_1_16_2, V_1_16_3, V_1_16_4, V_1_16_5,
    V_1_17, V_1_17_1,
    V_1_18, V_1_18_1, V_1_18_2,
    V_1_19, V_1_19_1, V_1_19_2, V_1_19_3, V_1_19_4,
    V_1_20, V_1_20_1, V_1_20_2, V_1_20_3, V_1_20_4, V_1_20_5, V_1_20_6,
    V_1_21, V_1_21_1, V_1_21_2, V_1_21_3, V_1_21_4,
    HIGHER_THAN_SUPPORTED;

    public String getReleaseName() { return name(); }
    public boolean isNewerThanOrEquals(ClientVersion v) { return this.ordinal() >= v.ordinal(); }
    public boolean isOlderThanOrEquals(ClientVersion v) { return this.ordinal() <= v.ordinal(); }
    public boolean isNewerThan(ClientVersion v) { return this.ordinal() > v.ordinal(); }
    public boolean isOlderThan(ClientVersion v) { return this.ordinal() < v.ordinal(); }
}
""")
write_file("com/github/retrooper/packetevents/protocol/player/DiggingAction.java", """package com.github.retrooper.packetevents.protocol.player;
public enum DiggingAction { START_DIGGING, CANCELLED_DIGGING, FINISHED_DIGGING, DROP_ITEM_STACK, DROP_ITEM, RELEASE_USE_ITEM, SWAP_ITEM_WITH_OFFHAND }
""")
write_file("com/github/retrooper/packetevents/protocol/packettype/PacketTypeCommon.java", """package com.github.retrooper.packetevents.protocol.packettype;
public interface PacketTypeCommon {
    default String getName() { return ""; }
    default int getId(Object v) { return 0; }
}""")
write_file("com/github/retrooper/packetevents/protocol/packettype/PacketType.java", """package com.github.retrooper.packetevents.protocol.packettype;
public class PacketType {
    public static class Login {
        public enum Server implements PacketTypeCommon {
            LOGIN_SUCCESS
        }
    }
    public static class Configuration {
        public enum Client implements PacketTypeCommon {
            PLUGIN_MESSAGE
        }
    }
    public static class Play {
        public enum Client implements PacketTypeCommon {
            PLAYER_FLYING, PLAYER_POSITION, PLAYER_ROTATION, PLAYER_POSITION_AND_ROTATION,
            PLAYER_DIGGING, PLAYER_BLOCK_PLACEMENT, USE_ITEM, INTERACT_ENTITY, ATTACK,
            HELD_ITEM_CHANGE, ENTITY_ACTION, ANIMATION, KEEP_ALIVE, CLIENT_STATUS,
            CLICK_WINDOW, CLOSE_WINDOW, PLUGIN_MESSAGE, STEER_VEHICLE, VEHICLE_MOVE,
            WINDOW_CONFIRMATION, PONG, CLIENT_TICK_END, SPECTATE, CUSTOM_PAYLOAD,
            SET_HELD_ITEM, ARM_ANIMATION, PLAYER_ABILITIES
        }
        public enum Server implements PacketTypeCommon {
            ENTITY_VELOCITY, PLAYER_POSITION_AND_LOOK, EXPLOSION, KEEP_ALIVE,
            HELD_ITEM_CHANGE, ENTITY_EFFECT, REMOVE_ENTITY_EFFECT, UPDATE_ATTRIBUTES,
            ENTITY_METADATA, ENTITY_RELATIVE_MOVE, ENTITY_RELATIVE_MOVE_AND_ROTATION,
            ENTITY_ROTATION, CHANGE_GAME_STATE, PING, WINDOW_CONFIRMATION,
            PLAYER_ABILITIES, JOIN_GAME, SYSTEM_CHAT_MESSAGE
        }
    }
}""")

# Attributes & Potions
write_file("com/github/retrooper/packetevents/protocol/attribute/Attribute.java", """package com.github.retrooper.packetevents.protocol.attribute;
public class Attribute {
    public String getName() { return ""; }
}
""")
write_file("com/github/retrooper/packetevents/protocol/attribute/Attributes.java", """package com.github.retrooper.packetevents.protocol.attribute;
public class Attributes {
    public static final Attribute GENERIC_MOVEMENT_SPEED = new Attribute();
    public static final Attribute GENERIC_ATTACK_SPEED = new Attribute();
    public static final Attribute GENERIC_MAX_HEALTH = new Attribute();
    public static final Attribute GENERIC_KNOCKBACK_RESISTANCE = new Attribute();
    public static final Attribute STEP_HEIGHT = new Attribute();
    public static final Attribute JUMP_STRENGTH = new Attribute();
    public static final Attribute GRAVITY = new Attribute();
    public static final Attribute MOVEMENT_SPEED = new Attribute();
    public static final Attribute ENTITY_INTERACTION_RANGE = new Attribute();
}""")
write_file("com/github/retrooper/packetevents/protocol/potion/PotionType.java", """package com.github.retrooper.packetevents.protocol.potion;
public class PotionType {
    public int getId() { return 0; }
    public String getName() { return ""; }
}
""")
write_file("com/github/retrooper/packetevents/protocol/potion/PotionTypes.java", """package com.github.retrooper.packetevents.protocol.potion;
public class PotionTypes {
    public static final PotionType SPEED = new PotionType();
    public static final PotionType SLOWNESS = new PotionType();
    public static final PotionType HASTE = new PotionType();
    public static final PotionType MINING_FATIGUE = new PotionType();
    public static final PotionType JUMP_BOOST = new PotionType();
    public static final PotionType LEVITATION = new PotionType();
    public static final PotionType SLOW_FALLING = new PotionType();
    public static final PotionType DOLPHINS_GRACE = new PotionType();
    public static final PotionType CONDUIT_POWER = new PotionType();
}
""")
write_file("com/github/retrooper/packetevents/protocol/entity/data/EntityData.java", """package com.github.retrooper.packetevents.protocol.entity.data;
public class EntityData {
    public int getIndex() { return 0; }
    public Object getValue() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/util/Vector3d.java", """package com.github.retrooper.packetevents.util;
public class Vector3d {
    public double x, y, z;
    public Vector3d(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
}""")
write_file("io/github/retrooper/packetevents/util/Vector3d.java", """package io.github.retrooper.packetevents.util;
public class Vector3d {
    public double x, y, z;
    public Vector3d(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
}
""")
write_file("io/github/retrooper/packetevents/util/SpigotReflectionUtil.java", """package io.github.retrooper.packetevents.util;
import org.bukkit.World;
import org.bukkit.entity.Entity;
public class SpigotReflectionUtil {
    public static Entity getEntityById(World world, int entityId) { return null; }
    public static Entity getEntityById(int entityId) { return null; }
    public static double getTPS() { return 20.0; }
}""")
write_file("com/github/retrooper/packetevents/netty/channel/ChannelHelper.java", """package com.github.retrooper.packetevents.netty.channel;
public class ChannelHelper {
    public static void runInEventLoop(Object channel, Runnable task) {
        if (task != null) task.run();
    }
}""")

# Packet Wrappers
write_file("com/github/retrooper/packetevents/wrapper/PacketWrapper.java", """package com.github.retrooper.packetevents.wrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public abstract class PacketWrapper<T> {
    public PacketWrapper(PacketEvent e) {}
    public PacketWrapper() {}
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientPlayerFlying.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.util.Vector3d;
public class WrapperPlayClientPlayerFlying extends PacketWrapper<WrapperPlayClientPlayerFlying> {
    public WrapperPlayClientPlayerFlying(PacketEvent e) { super(e); }
    public boolean isOnGround() { return false; }
    public boolean hasPositionChanged() { return false; }
    public boolean hasRotationChanged() { return false; }
    public Vector3d getLocation() { return new Vector3d(0, 0, 0); }
    public float getYaw() { return 0f; }
    public float getPitch() { return 0f; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientPlayerDigging.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.util.Vector3d;
public class WrapperPlayClientPlayerDigging extends PacketWrapper<WrapperPlayClientPlayerDigging> {
    public WrapperPlayClientPlayerDigging(PacketEvent e) { super(e); }
    public DiggingAction getAction() { return DiggingAction.START_DIGGING; }
    public Vector3d getBlockPosition() { return new Vector3d(0, 0, 0); }
    public int getBlockFace() { return 0; }
}
""")

write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientPong.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientPong extends PacketWrapper<WrapperPlayClientPong> {
    public WrapperPlayClientPong(PacketEvent e) { super(e); }
    public int getId() { return 0; }
}
""")

write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientWindowConfirmation.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientWindowConfirmation extends PacketWrapper<WrapperPlayClientWindowConfirmation> {
    public WrapperPlayClientWindowConfirmation(PacketEvent e) { super(e); }
    public short getActionId() { return 0; }
}
""")

write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientPlayerBlockPlacement.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.block.BlockFace;

public class WrapperPlayClientPlayerBlockPlacement extends PacketWrapper<WrapperPlayClientPlayerBlockPlacement> {
    public WrapperPlayClientPlayerBlockPlacement(PacketEvent e) { super(e); }
    public Vector3d getCursorPosition() { return new Vector3d(0, 0, 0); }
    public BlockFace getBlockFace() { return BlockFace.UP; }
}""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientInteractEntity.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.util.Vector3d;
public class WrapperPlayClientInteractEntity extends PacketWrapper<WrapperPlayClientInteractEntity> {
    public enum InteractAction { INTERACT, ATTACK, INTERACT_AT }
    public WrapperPlayClientInteractEntity(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public InteractAction getAction() { return InteractAction.ATTACK; }
    public Vector3d getTarget() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientEntityAction.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientEntityAction extends PacketWrapper<WrapperPlayClientEntityAction> {
    public enum Action { START_SNEAKING, STOP_SNEAKING, START_SPRINTING, STOP_SPRINTING, START_RIDING_JUMP, STOP_RIDING_JUMP, OPEN_INVENTORY, START_FALL_FLYING, START_FLYING_WITH_ELYTRA }
    public WrapperPlayClientEntityAction(PacketEvent e) { super(e); }
    public Action getAction() { return Action.START_SPRINTING; }
    public int getEntityId() { return 0; }
    public int getJumpBoost() { return 0; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientHeldItemChange.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientHeldItemChange extends PacketWrapper<WrapperPlayClientHeldItemChange> {
    public WrapperPlayClientHeldItemChange(PacketEvent e) { super(e); }
    public int getSlot() { return 0; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientKeepAlive.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientKeepAlive extends PacketWrapper<WrapperPlayClientKeepAlive> {
    public WrapperPlayClientKeepAlive(PacketEvent e) { super(e); }
    public long getId() { return 0L; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientClientStatus.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientClientStatus extends PacketWrapper<WrapperPlayClientClientStatus> {
    public enum Action { PERFORM_RESPAWN, REQUEST_STATS }
    public WrapperPlayClientClientStatus(PacketEvent e) { super(e); }
    public Action getAction() { return Action.PERFORM_RESPAWN; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientPluginMessage.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientPluginMessage extends PacketWrapper<WrapperPlayClientPluginMessage> {
    public WrapperPlayClientPluginMessage(PacketEvent e) { super(e); }
    public String getChannelName() { return ""; }
    public byte[] getData() { return new byte[0]; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/configuration/client/WrapperConfigClientPluginMessage.java", """package com.github.retrooper.packetevents.wrapper.configuration.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperConfigClientPluginMessage extends PacketWrapper<WrapperConfigClientPluginMessage> {
    public WrapperConfigClientPluginMessage(PacketEvent e) { super(e); }
    public String getChannelName() { return ""; }
    public byte[] getData() { return new byte[0]; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientSteerVehicle.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientSteerVehicle extends PacketWrapper<WrapperPlayClientSteerVehicle> {
    public WrapperPlayClientSteerVehicle(PacketEvent e) { super(e); }
    public float getForward() { return 0f; }
    public float getSideways() { return 0f; }
    public boolean isJump() { return false; }
    public boolean isUnmount() { return false; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientVehicleMove.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.util.Vector3d;
public class WrapperPlayClientVehicleMove extends PacketWrapper<WrapperPlayClientVehicleMove> {
    public WrapperPlayClientVehicleMove(PacketEvent e) { super(e); }
    public Vector3d getPosition() { return new Vector3d(0, 0, 0); }
    public float getYaw() { return 0f; }
    public float getPitch() { return 0f; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/client/WrapperPlayClientPlayerAbilities.java", """package com.github.retrooper.packetevents.wrapper.play.client;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayClientPlayerAbilities extends PacketWrapper<WrapperPlayClientPlayerAbilities> {
    public WrapperPlayClientPlayerAbilities(PacketEvent e) { super(e); }
    public boolean isFlying() { return false; }
    public boolean isFlightAllowed() { return false; }
}
""")

# Server Packet Wrappers
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityVelocity.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.util.Vector3d;
public class WrapperPlayServerEntityVelocity extends PacketWrapper<WrapperPlayServerEntityVelocity> {
    public WrapperPlayServerEntityVelocity(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public Vector3d getVelocity() { return new Vector3d(0, 0, 0); }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerPositionAndLook.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerPlayerPositionAndLook extends PacketWrapper<WrapperPlayServerPlayerPositionAndLook> {
    public WrapperPlayServerPlayerPositionAndLook(PacketEvent e) { super(e); }
    public WrapperPlayServerPlayerPositionAndLook(double x, double y, double z, float yaw, float pitch, byte flags, int teleportId, boolean dismount) { super(null); }
    public WrapperPlayServerPlayerPositionAndLook(double x, double y, double z, float yaw, float pitch, int flags, int teleportId, boolean dismount) { super(null); }
    public double getX() { return 0; }
    public double getY() { return 0; }
    public double getZ() { return 0; }
    public float getYaw() { return 0f; }
    public float getPitch() { return 0f; }
    public byte getRelativeFlags() { return 0; }
    public int getTeleportId() { return 0; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerExplosion.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.util.Vector3d;
public class WrapperPlayServerExplosion extends PacketWrapper<WrapperPlayServerExplosion> {
    public WrapperPlayServerExplosion(PacketEvent e) { super(e); }
    public Vector3d getPlayerMotion() { return new Vector3d(0, 0, 0); }
    public Vector3d getKnockback() { return getPlayerMotion(); }
    public double getX() { return 0; }
    public double getY() { return 0; }
    public double getZ() { return 0; }
    public float getRadius() { return 0f; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerHeldItemChange.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerHeldItemChange extends PacketWrapper<WrapperPlayServerHeldItemChange> {
    public WrapperPlayServerHeldItemChange(PacketEvent e) { super(e); }
    public WrapperPlayServerHeldItemChange(int slot) { super(null); }
    public int getSlot() { return 0; }
}""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityEffect.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
public class WrapperPlayServerEntityEffect extends PacketWrapper<WrapperPlayServerEntityEffect> {
    public WrapperPlayServerEntityEffect(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public PotionType getPotionType() { return null; }
    public byte getEffectAmplifier() { return 0; }
    public int getEffectDurationTicks() { return 0; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerRemoveEntityEffect.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
public class WrapperPlayServerRemoveEntityEffect extends PacketWrapper<WrapperPlayServerRemoveEntityEffect> {
    public WrapperPlayServerRemoveEntityEffect(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public PotionType getPotionType() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerUpdateAttributes.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class WrapperPlayServerUpdateAttributes extends PacketWrapper<WrapperPlayServerUpdateAttributes> {
    public WrapperPlayServerUpdateAttributes(PacketEvent e) { super(e); }
    public List<Property> getProperties() { return new ArrayList<>(); }

    public static class Property {
        public Attribute getAttribute() { return null; }
        public double getValue() { return 0.0; }
        public List<PropertyModifier> getModifiers() { return new ArrayList<>(); }
    }

    public static class PropertyModifier {
        public enum Operation { ADDITION, MULTIPLY_BASE, MULTIPLY_TOTAL }
        public Operation getOperation() { return Operation.ADDITION; }
        public double getAmount() { return 0.0; }
        public UUID getUUID() { return UUID.randomUUID(); }
        public String getName() { return ""; }
    }
}""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityMetadata.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import java.util.List;
public class WrapperPlayServerEntityMetadata extends PacketWrapper<WrapperPlayServerEntityMetadata> {
    public WrapperPlayServerEntityMetadata(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public List<EntityData> getEntityMetadata() { return null; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMove.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerEntityRelativeMove extends PacketWrapper<WrapperPlayServerEntityRelativeMove> {
    public WrapperPlayServerEntityRelativeMove(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public double getDeltaX() { return 0; }
    public double getDeltaY() { return 0; }
    public double getDeltaZ() { return 0; }
    public boolean isOnGround() { return false; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerEntityRelativeMoveAndRotation.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerEntityRelativeMoveAndRotation extends PacketWrapper<WrapperPlayServerEntityRelativeMoveAndRotation> {
    public WrapperPlayServerEntityRelativeMoveAndRotation(PacketEvent e) { super(e); }
    public int getEntityId() { return 0; }
    public double getDeltaX() { return 0; }
    public double getDeltaY() { return 0; }
    public double getDeltaZ() { return 0; }
    public float getYaw() { return 0f; }
    public float getPitch() { return 0f; }
    public boolean isOnGround() { return false; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerChangeGameState.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerChangeGameState extends PacketWrapper<WrapperPlayServerChangeGameState> {
    public enum Reason { START_RAINING, STOP_RAINING, CHANGE_GAME_MODE, WIN_GAME, DEMO_MESSAGE, ARROW_HIT_PLAYER, RAIN_STRENGTH, THUNDER_STRENGTH, PUFFER_FISH_STING, ELDER_GUARDIAN_APPEARANCE, ENABLE_RESPAWN_SCREEN }
    public WrapperPlayServerChangeGameState(PacketEvent e) { super(e); }
    public Reason getReason() { return null; }
    public float getValue() { return 0f; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPing.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerPing extends PacketWrapper<WrapperPlayServerPing> {
    public WrapperPlayServerPing(PacketEvent e) { super(e); }
    public WrapperPlayServerPing(int id) { super(null); }
    public int getId() { return 0; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerWindowConfirmation.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerWindowConfirmation extends PacketWrapper<WrapperPlayServerWindowConfirmation> {
    public WrapperPlayServerWindowConfirmation(PacketEvent e) { super(e); }
    public WrapperPlayServerWindowConfirmation(int windowId, short actionNumber, boolean accepted) { super(null); }
    public int getWindowId() { return 0; }
    public short getActionId() { return 0; }
    public boolean isAccepted() { return false; }
}
""")
write_file("com/github/retrooper/packetevents/wrapper/play/server/WrapperPlayServerPlayerAbilities.java", """package com.github.retrooper.packetevents.wrapper.play.server;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketEvent;
public class WrapperPlayServerPlayerAbilities extends PacketWrapper<WrapperPlayServerPlayerAbilities> {
    public WrapperPlayServerPlayerAbilities(PacketEvent e) { super(e); }
    public boolean isFlying() { return false; }
    public boolean isFlightAllowed() { return false; }
    public float getFlySpeed() { return 0.05f; }
    public float getWalkSpeed() { return 0.1f; }
}
""")

# 10. Folia & Paper schedulers

write_file("io/papermc/paper/threadedregions/scheduler/EntityScheduler.java", """package io.papermc.paper.threadedregions.scheduler;
import org.bukkit.plugin.Plugin;
import java.util.function.Consumer;
public interface EntityScheduler {
    ScheduledTask run(Plugin plugin, Consumer<ScheduledTask> task, Runnable retired);
    ScheduledTask runDelayed(Plugin plugin, Consumer<ScheduledTask> task, Runnable retired, long delayTicks);
    ScheduledTask runAtFixedRate(Plugin plugin, Consumer<ScheduledTask> task, Runnable retired, long initialDelayTicks, long periodTicks);
    boolean execute(Plugin plugin, Runnable run, Runnable retired, long delay);
}
""")

write_file("io/papermc/paper/threadedregions/scheduler/ScheduledTask.java", """package io.papermc.paper.threadedregions.scheduler;
import org.bukkit.plugin.Plugin;
public interface ScheduledTask {
    enum ExecutionState { IDLE, RUNNING, FINISHED, CANCELLED }
    Plugin getPlugin();
    Plugin getOwningPlugin();
    ExecutionState getExecutionState();
    boolean isRepeatingTask();
    void cancel();
    boolean isCancelled();
}
""")
write_file("io/papermc/paper/threadedregions/scheduler/AsyncScheduler.java", """package io.papermc.paper.threadedregions.scheduler;
import org.bukkit.plugin.Plugin;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
public interface AsyncScheduler {
    ScheduledTask runNow(Plugin p, Consumer<ScheduledTask> task);
    ScheduledTask runDelayed(Plugin p, Consumer<ScheduledTask> task, long delay, TimeUnit unit);
    ScheduledTask runAtFixedRate(Plugin p, Consumer<ScheduledTask> task, long initialDelay, long period, TimeUnit unit);
    void cancelTasks(Plugin p);
}
""")
write_file("io/papermc/paper/threadedregions/scheduler/GlobalRegionScheduler.java", """package io.papermc.paper.threadedregions.scheduler;
import org.bukkit.plugin.Plugin;
import java.util.function.Consumer;
public interface GlobalRegionScheduler {
    ScheduledTask run(Plugin p, Consumer<ScheduledTask> task);
    ScheduledTask runDelayed(Plugin p, Consumer<ScheduledTask> task, long delayTicks);
    ScheduledTask runAtFixedRate(Plugin p, Consumer<ScheduledTask> task, long initialDelayTicks, long periodTicks);
    void execute(Plugin p, Runnable task);
    void cancelTasks(Plugin p);
}
""")
write_file("io/papermc/paper/threadedregions/scheduler/RegionScheduler.java", """package io.papermc.paper.threadedregions.scheduler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import java.util.function.Consumer;
public interface RegionScheduler {
    ScheduledTask run(Plugin p, Location loc, Consumer<ScheduledTask> task);
    ScheduledTask runDelayed(Plugin p, Location loc, Consumer<ScheduledTask> task, long delayTicks);
    ScheduledTask runAtFixedRate(Plugin p, Location loc, Consumer<ScheduledTask> task, long delayTicks, long periodTicks);
    void execute(Plugin p, Location loc, Runnable task);
    void execute(Plugin p, World world, int chunkX, int chunkZ, Runnable task);
}""")
write_file("io/github/retrooper/packetevents/util/folia/FoliaScheduler.java", """package io.github.retrooper.packetevents.util.folia;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
public class FoliaScheduler {
    public static RegionScheduler getRegionScheduler() { return null; }
    public static GlobalRegionScheduler getGlobalRegionScheduler() { return null; }
    public static AsyncScheduler getAsyncScheduler() { return null; }
}""")

# 11. Bukkit API full basic set

write_file("org/bukkit/Server.java", """package org.bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.plugin.messaging.Messenger;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
public interface Server {
    String getName();
    String getVersion();
    String getBukkitVersion();
    Logger getLogger();
    PluginManager getPluginManager();
    ServicesManager getServicesManager();
    BukkitScheduler getScheduler();
    Messenger getMessenger();
    Collection<? extends Player> getOnlinePlayers();
    Player getPlayer(String name);
    Player getPlayer(UUID id);
    Player getPlayerExact(String name);
    List<World> getWorlds();
    World getWorld(String name);
    World getWorld(UUID uid);
    void broadcastMessage(String message);
    boolean dispatchCommand(org.bukkit.command.CommandSender sender, String commandLine);
    boolean isPrimaryThread();
    boolean isGlobalTickThread();
    boolean isOwnedByCurrentRegion(Entity entity);
    boolean isOwnedByCurrentRegion(Location location);
    io.papermc.paper.threadedregions.scheduler.RegionScheduler getRegionScheduler();
    io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler getGlobalRegionScheduler();
    io.papermc.paper.threadedregions.scheduler.AsyncScheduler getAsyncScheduler();
}""")

write_file("org/bukkit/Bukkit.java", """package org.bukkit;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.CommandSender;

public final class Bukkit {
    public static Server getServer() { return null; }
    public static Logger getLogger() { return Logger.getLogger("Minecraft"); }
    public static PluginManager getPluginManager() { return null; }
    public static ServicesManager getServicesManager() { return null; }
    public static BukkitScheduler getScheduler() { return null; }
    public static Collection<? extends Player> getOnlinePlayers() { return java.util.Collections.emptyList(); }
    public static Player getPlayer(String name) { return null; }
    public static Player getPlayer(UUID id) { return null; }
    public static Player getPlayerExact(String name) { return null; }
    public static void broadcastMessage(String msg) {}
    public static boolean dispatchCommand(CommandSender sender, String commandLine) { return false; }
    public static ConsoleCommandSender getConsoleSender() { return null; }
    public static boolean getOnlineMode() { return true; }
    public static String getVersion() { return "1.8.8"; }
    public static String getName() { return "CraftBukkit"; }
    public static boolean isPrimaryThread() { return true; }
}""")
write_file("org/bukkit/Location.java", """package org.bukkit;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
public class Location implements Cloneable {
    private World world;
    private double x, y, z;
    private float yaw, pitch;
    public Location(World world, double x, double y, double z) { this(world, x, y, z, 0, 0); }
    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world; this.x = x; this.y = y; this.z = z; this.yaw = yaw; this.pitch = pitch;
    }
    public World getWorld() { return world; }
    public org.bukkit.util.Vector toVector() { return new org.bukkit.util.Vector(x, y, z); }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public int getBlockX() { return (int)Math.floor(x); }
    public int getBlockY() { return (int)Math.floor(y); }
    public int getBlockZ() { return (int)Math.floor(z); }
    public Block getBlock() { return null; }
    public double distance(Location o) { return 0; }
    public double distanceSquared(Location o) { return 0; }
    public Location add(double x, double y, double z) { this.x += x; this.y += y; this.z += z; return this; }
    public Location subtract(double x, double y, double z) { this.x -= x; this.y -= y; this.z -= z; return this; }
    public Location clone() { try { return (Location)super.clone(); } catch (Exception e) { return null; } }
}
""")
write_file("org/bukkit/WorldBorder.java", """package org.bukkit;
public interface WorldBorder {
    Location getCenter();
    double getSize();
}""")

write_file("org/bukkit/World.java", """package org.bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import java.util.Collection;
import java.util.UUID;
public interface World {
    String getName();
    UUID getUID();
    Block getBlockAt(int x, int y, int z);
    Block getBlockAt(Location loc);
    int getMaxHeight();
    int getMinHeight();
    Collection<? extends Entity> getEntities();
    boolean isChunkLoaded(int x, int z);
    boolean hasStorm();
    org.bukkit.WorldBorder getWorldBorder();
    void playEffect(Location loc, Effect effect, int data);
}""")
write_file("org/bukkit/Material.java", """package org.bukkit;
public enum Material {
    AIR, STONE, GRASS, DIRT, COBBLESTONE, WOOD, BEDROCK, WATER, STATIONARY_WATER,
    LAVA, STATIONARY_LAVA, SAND, GRAVEL, GOLD_ORE, IRON_ORE, COAL_ORE, LOG,
    LEAVES, SPONGE, GLASS, LAPIS_ORE, LAPIS_BLOCK, DISPENSER, SANDSTONE, NOTE_BLOCK,
    BED_BLOCK, POWERED_RAIL, DETECTOR_RAIL, PISTON_STICKY_BASE, WEB, LONG_GRASS,
    DEAD_BUSH, PISTON_BASE, PISTON_EXTENSION, WOOL, PISTON_MOVING_PIECE, YELLOW_FLOWER,
    RED_ROSE, BROWN_MUSHROOM, RED_MUSHROOM, GOLD_BLOCK, IRON_BLOCK, DOUBLE_STEP,
    STEP, BRICK, TNT, BOOKSHELF, MOSSY_COBBLESTONE, OBSIDIAN, TORCH, FIRE,
    MOB_SPAWNER, WOOD_STAIRS, CHEST, REDSTONE_WIRE, DIAMOND_ORE, DIAMOND_BLOCK,
    WORKBENCH, CROPS, SOIL, FURNACE, BURNING_FURNACE, SIGN_POST, WOODEN_DOOR,
    LADDER, RAILS, COBBLESTONE_STAIRS, WALL_SIGN, LEVER, STONE_PLATE, IRON_DOOR_BLOCK,
    WOOD_PLATE, REDSTONE_ORE, GLOWING_REDSTONE_ORE, REDSTONE_TORCH_OFF, REDSTONE_TORCH_ON,
    STONE_BUTTON, SNOW, ICE, SNOW_BLOCK, CACTUS, CLAY, SUGAR_CANE_BLOCK, JUKEBOX,
    FENCE, PUMPKIN, NETHERRACK, SOUL_SAND, GLOWSTONE, PORTAL, JACK_O_LANTERN,
    CAKE_BLOCK, DIODE_BLOCK_OFF, DIODE_BLOCK_ON, STAINED_GLASS, TRAP_DOOR, MONSTER_EGGS,
    SMOOTH_BRICK, HUGE_MUSHROOM_1, HUGE_MUSHROOM_2, IRON_FENCE, THIN_GLASS, MELON_BLOCK,
    PUMPKIN_STEM, MELON_STEM, VINE, FENCE_GATE, BRICK_STAIRS, SMOOTH_STAIRS, MYCEL,
    WATER_LILY, NETHER_BRICK, NETHER_FENCE, NETHER_BRICK_STAIRS, NETHER_WARTS,
    ENCHANTMENT_TABLE, BREWING_STAND, CAULDRON, ENDER_PORTAL, ENDER_PORTAL_FRAME,
    ENDER_STONE, DRAGON_EGG, REDSTONE_LAMP_OFF, REDSTONE_LAMP_ON, WOOD_DOUBLE_STEP,
    WOOD_STEP, COCOA, SANDSTONE_STAIRS, EMERALD_ORE, ENDER_CHEST, TRIPWIRE_HOOK,
    TRIPWIRE, EMERALD_BLOCK, SPRUCE_WOOD_STAIRS, BIRCH_WOOD_STAIRS, JUNGLE_WOOD_STAIRS,
    COMMAND, BEACON, COBBLE_WALL, FLOWER_POT, CARROT, POTATO, WOOD_BUTTON, SKULL,
    ANVIL, TRAPPED_CHEST, GOLD_PLATE, IRON_PLATE, BOW, CROSSBOW, REDSTONE_COMPARATOR_OFF,
    REDSTONE_COMPARATOR_ON, DAYLIGHT_DETECTOR, REDSTONE_BLOCK, QUARTZ_ORE, HOPPER,
    QUARTZ_BLOCK, QUARTZ_STAIRS, ACTIVATOR_RAIL, DROPPER, STAINED_CLAY, STAINED_GLASS_PANE,
    LEAVES_2, LOG_2, ACACIA_STAIRS, DARK_OAK_STAIRS, SLIME_BLOCK, BARRIER, IRON_TRAPDOOR,
    PRISMARINE, SEA_LANTERN, HAY_BLOCK, CARPET, HARD_CLAY, COAL_BLOCK, PACKED_ICE,
    DOUBLE_PLANT, STANDING_BANNER, WALL_BANNER, DAYLIGHT_DETECTOR_INVERTED, RED_SANDSTONE,
    RED_SANDSTONE_STAIRS, DOUBLE_STONE_SLAB2, STONE_SLAB2, SPRUCE_FENCE_GATE,
    BIRCH_FENCE_GATE, JUNGLE_FENCE_GATE, DARK_OAK_FENCE_GATE, ACACIA_FENCE_GATE,
    SPRUCE_FENCE, BIRCH_FENCE, JUNGLE_FENCE, DARK_OAK_FENCE, ACACIA_FENCE,
    SPRUCE_DOOR, BIRCH_DOOR, JUNGLE_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, END_ROD,
    CHORUS_PLANT, CHORUS_FLOWER, PURPUR_BLOCK, PURPUR_PILLAR, PURPUR_STAIRS,
    PURPUR_DOUBLE_SLAB, PURPUR_SLAB, END_BRICKS, BEETROOT_BLOCK, GRASS_PATH,
    END_GATEWAY, REPEATING_COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, FROSTED_ICE,
    MAGMA, NETHER_WART_BLOCK, RED_NETHER_BRICK, BONE_BLOCK, STRUCTURE_VOID,
    OBSERVER, WHITE_SHULKER_BOX, ORANGE_SHULKER_BOX, MAGENTA_SHULKER_BOX,
    LIGHT_BLUE_SHULKER_BOX, YELLOW_SHULKER_BOX, LIME_SHULKER_BOX, PINK_SHULKER_BOX,
    GRAY_SHULKER_BOX, SILVER_SHULKER_BOX, CYAN_SHULKER_BOX, PURPLE_SHULKER_BOX,
    BLUE_SHULKER_BOX, BROWN_SHULKER_BOX, GREEN_SHULKER_BOX, RED_SHULKER_BOX,
    BLACK_SHULKER_BOX, WHITE_GLAZED_TERRACOTTA, ORANGE_GLAZED_TERRACOTTA,
    MAGENTA_GLAZED_TERRACOTTA, LIGHT_BLUE_GLAZED_TERRACOTTA, YELLOW_GLAZED_TERRACOTTA,
    LIME_GLAZED_TERRACOTTA, PINK_GLAZED_TERRACOTTA, GRAY_GLAZED_TERRACOTTA,
    SILVER_GLAZED_TERRACOTTA, CYAN_GLAZED_TERRACOTTA, PURPLE_GLAZED_TERRACOTTA,
    BLUE_GLAZED_TERRACOTTA, BROWN_GLAZED_TERRACOTTA, GREEN_GLAZED_TERRACOTTA,
    RED_GLAZED_TERRACOTTA, BLACK_GLAZED_TERRACOTTA, CONCRETE, CONCRETE_POWDER,
    STRUCTURE_BLOCK, SCAFFOLDING, SWEET_BERRY_BUSH, BUBBLE_COLUMN, LILY_PAD,
    POWDER_SNOW, HONEY_BLOCK, BAMBOO, BLUE_ICE, DRIED_KELP_BLOCK, KELP, SEAGRASS,
    TURTLE_EGG, CONDUIT, LOOM, BARREL, SMOKER, BLAST_FURNACE, CARTOGRAPHY_TABLE,
    FLETCHING_TABLE, GRINDSTONE, LECTERN, SMITHING_TABLE, STONECUTTER, BELL,
    CAMPFIRE, SOUL_CAMPFIRE, LANTERN, SOUL_LANTERN, WARPED_STEM, CRIMSON_STEM,
    WARPED_HYPHAE, CRIMSON_HYPHAE, WARPED_NYLIUM, CRIMSON_NYLIUM, WARPED_FUNGUS,
    CRIMSON_FUNGUS, WARPED_ROOTS, CRIMSON_ROOTS, NETHER_SPROUTS, WEEPING_VINES,
    TWISTING_VINES, SOUL_SOIL, TARGET, CRYING_OBSIDIAN, RESPAWN_ANCHOR,
    LODESTONE, BLACKSTONE, NETHERITE_BLOCK, ANCIENT_DEBRIS;

    public boolean isSolid() { return true; }
    public boolean isBlock() { return true; }
    public float getHardness() { return 1.0f; }
}""")
write_file("org/bukkit/Color.java", """package org.bukkit;
public class Color {
    public static final Color RED = new Color(255, 0, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public Color(int r, int g, int b) {}
    public int asRGB() { return 0; }
}
""")
write_file("org/bukkit/Effect.java", """package org.bukkit;
public enum Effect {
    CLICK2, CLICK1, BOW_FIRE, DOOR_TOGGLE, EXTINGUISH, RECORD_PLAY, GHAST_SHRIEK,
    GHAST_SHOOT, BLAZE_SHOOT, ZOMBIE_CHEW_WOODEN_DOOR, ZOMBIE_CHEW_IRON_DOOR,
    ZOMBIE_DESTROY_DOOR, SMOKE, STEP_SOUND, POTION_BREAK, ENDER_SIGNAL, MOBSPAWNER_FLAMES,
    POTION_SWIRL, POTION_SWIRL_TRANSPARENT, VILLAGER_PLANT_GROW, DRAGON_BREATH,
    ENDEREYE_LAUNCH
}""")
write_file("org/bukkit/GameMode.java", """package org.bukkit;
public enum GameMode { SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR }
""")
write_file("org/bukkit/WeatherType.java", """package org.bukkit;
public enum WeatherType { DOWNFALL, CLEAR }
""")

# Bukkit block
write_file("org/bukkit/block/Block.java", """package org.bukkit.block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import java.util.Collection;
public interface Block {
    Material getType();
    Location getLocation();
    World getWorld();
    int getX();
    int getY();
    int getZ();
    BlockData getBlockData();
    byte getData();
    boolean isLiquid();
    boolean isEmpty();
    Block getRelative(BlockFace face);
    Block getRelative(int modX, int modY, int modZ);
    BlockFace getFace(Block block);
    Collection<ItemStack> getDrops(ItemStack tool);
}""")
write_file("org/bukkit/block/BlockFace.java", """package org.bukkit.block;
public enum BlockFace {
    NORTH(0, 0, -1), EAST(1, 0, 0), SOUTH(0, 0, 1), WEST(-1, 0, 0), UP(0, 1, 0), DOWN(0, -1, 0), SELF(0, 0, 0);
    private final int modX, modY, modZ;
    BlockFace(int x, int y, int z) { this.modX = x; this.modY = y; this.modZ = z; }
    public int getModX() { return modX; }
    public int getModY() { return modY; }
    public int getModZ() { return modZ; }
    public BlockFace getOppositeFace() { return this; }
}
""")
write_file("org/bukkit/block/data/BlockData.java", """package org.bukkit.block.data;
import org.bukkit.Material;
public interface BlockData extends Cloneable {
    Material getMaterial();
    String getAsString();
    BlockData clone();
}
""")
write_file("org/bukkit/block/data/Waterlogged.java", """package org.bukkit.block.data;
public interface Waterlogged extends BlockData {
    boolean isWaterlogged();
    void setWaterlogged(boolean b);
}
""")

# Bukkit Commands
write_file("org/bukkit/command/Command.java", """package org.bukkit.command;
import java.util.List;
public abstract class Command {
    protected String name;
    protected String description;
    protected String usageMessage;
    public Command(String name) { this.name = name; }
    public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) { return null; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getUsage() { return usageMessage; }
    public void setPermission(String perm) {}
    public void setAliases(List<String> aliases) {}
    public void setDescription(String desc) {}
    public void setUsage(String usage) {}
}
""")
write_file("org/bukkit/command/CommandExecutor.java", """package org.bukkit.command;
public interface CommandExecutor {
    boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
""")
write_file("org/bukkit/command/CommandSender.java", """package org.bukkit.command;
import org.bukkit.Server;
public interface CommandSender {
    String getName();
    boolean hasPermission(String name);
    void sendMessage(String message);
    Server getServer();
}""")
write_file("org/bukkit/command/ConsoleCommandSender.java", """package org.bukkit.command;
public interface ConsoleCommandSender extends CommandSender {}
""")
write_file("org/bukkit/command/TabCompleter.java", """package org.bukkit.command;
import java.util.List;
public interface TabCompleter {
    List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}
""")

# Bukkit Configuration

write_file("org/bukkit/configuration/InvalidConfigurationException.java", """package org.bukkit.configuration;
public class InvalidConfigurationException extends Exception {
    public InvalidConfigurationException() { super(); }
    public InvalidConfigurationException(String msg) { super(msg); }
    public InvalidConfigurationException(String msg, Throwable cause) { super(msg, cause); }
    public InvalidConfigurationException(Throwable cause) { super(cause); }
}
""")

write_file("org/bukkit/configuration/ConfigurationSection.java", """package org.bukkit.configuration;
import java.util.Set;
import java.util.List;
import java.util.Map;
public interface ConfigurationSection {
    Set<String> getKeys(boolean deep);
    Map<String, Object> getValues(boolean deep);
    boolean contains(String path);
    boolean isSet(String path);
    Object get(String path);
    Object get(String path, Object def);
    void set(String path, Object value);
    String getString(String path);
    String getString(String path, String def);
    int getInt(String path);
    int getInt(String path, int def);
    boolean getBoolean(String path);
    boolean getBoolean(String path, boolean def);
    double getDouble(String path);
    double getDouble(String path, double def);
    long getLong(String path);
    long getLong(String path, long def);
    List<String> getStringList(String path);
    ConfigurationSection getConfigurationSection(String path);
    String getCurrentPath();
    boolean isString(String path);
    boolean isColor(String path);
    org.bukkit.Color getColor(String path);
    ConfigurationSection createSection(String path);
}
""")

write_file("org/bukkit/configuration/file/FileConfigurationOptions.java", """package org.bukkit.configuration.file;
public class FileConfigurationOptions {
    public FileConfigurationOptions header(String value) { return this; }
    public FileConfigurationOptions copyDefaults(boolean value) { return this; }
}
""")

write_file("org/bukkit/configuration/file/YamlConfigurationOptions.java", """package org.bukkit.configuration.file;
public class YamlConfigurationOptions extends FileConfigurationOptions {
    public YamlConfigurationOptions header(String value) { return this; }
    public YamlConfigurationOptions copyDefaults(boolean value) { return this; }
}
""")

write_file("org/bukkit/configuration/file/FileConfiguration.java", """package org.bukkit.configuration.file;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public abstract class FileConfiguration implements ConfigurationSection {
    public void save(java.io.File file) throws java.io.IOException {}
    public void save(String file) throws java.io.IOException {}
    public void load(java.io.File file) throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException {}
    public void load(String file) throws java.io.IOException, org.bukkit.configuration.InvalidConfigurationException {}
    public void loadFromString(String contents) throws org.bukkit.configuration.InvalidConfigurationException {}
    public String saveToString() { return ""; }
    public FileConfigurationOptions options() { return new FileConfigurationOptions(); }
    public void addDefault(String path, Object value) {}
    public boolean isSet(String path) { return false; }
    public Object get(String path) { return null; }
    public Object get(String path, Object def) { return def; }
    public void set(String path, Object value) {}
    public boolean isString(String path) { return false; }
    public String getString(String path) { return ""; }
    public String getString(String path, String def) { return def; }
    public boolean isInt(String path) { return false; }
    public int getInt(String path) { return 0; }
    public int getInt(String path, int def) { return def; }
    public boolean isBoolean(String path) { return false; }
    public boolean getBoolean(String path) { return false; }
    public boolean getBoolean(String path, boolean def) { return def; }
    public boolean isDouble(String path) { return false; }
    public double getDouble(String path) { return 0.0; }
    public double getDouble(String path, double def) { return def; }
    public boolean isLong(String path) { return false; }
    public long getLong(String path) { return 0L; }
    public long getLong(String path, long def) { return def; }
    public List<?> getList(String path) { return Collections.emptyList(); }
    public List<String> getStringList(String path) { return Collections.emptyList(); }
    public List<Integer> getIntegerList(String path) { return Collections.emptyList(); }
    public boolean isColor(String path) { return false; }
    public org.bukkit.Color getColor(String path) { return null; }
    public boolean contains(String path) { return false; }
    public Set<String> getKeys(boolean deep) { return Collections.emptySet(); }
    public Map<String, Object> getValues(boolean deep) { return Collections.emptyMap(); }
    public ConfigurationSection getConfigurationSection(String path) { return null; }
    public ConfigurationSection createSection(String path) { return null; }
    public String getCurrentPath() { return ""; }
    public String getName() { return ""; }
}""")
write_file("org/bukkit/configuration/file/YamlConfiguration.java", """package org.bukkit.configuration.file;
public class YamlConfiguration extends FileConfiguration {
    public static YamlConfiguration loadConfiguration(java.io.File file) { return new YamlConfiguration(); }
    public static YamlConfiguration loadConfiguration(java.io.Reader reader) { return new YamlConfiguration(); }
    public YamlConfigurationOptions options() { return new YamlConfigurationOptions(); }
}""")

# Bukkit Enchantments & Inventory
write_file("org/bukkit/enchantments/Enchantment.java", """package org.bukkit.enchantments;
public class Enchantment {
    public static final Enchantment WATER_WORKER = new Enchantment();
    public static final Enchantment DEPTH_STRIDER = new Enchantment();
    public static final Enchantment SWIFT_SNEAK = new Enchantment();
    public static final Enchantment DIG_SPEED = new Enchantment();
    public static final Enchantment MULTISHOT = new Enchantment();
    public static final Enchantment PROTECTION_ENVIRONMENTAL = new Enchantment();
    public static final Enchantment DAMAGE_ALL = new Enchantment();
    public static final Enchantment ARROW_DAMAGE = new Enchantment();
    public static final Enchantment FIRE_ASPECT = new Enchantment();
    public static final Enchantment KNOCKBACK = new Enchantment();
    public static final Enchantment SOUL_SPEED = new Enchantment();
    public static final Enchantment RIPTIDE = new Enchantment();
    public static final Enchantment LOYALTY = new Enchantment();
    public static final Enchantment IMPALING = new Enchantment();
    public static final Enchantment CHANNELING = new Enchantment();
    public static final Enchantment LUCK = new Enchantment();
    public static final Enchantment LURE = new Enchantment();
    public static final Enchantment FROST_WALKER = new Enchantment();
    public static final Enchantment BINDING_CURSE = new Enchantment();
    public static final Enchantment VANISHING_CURSE = new Enchantment();
    public static final Enchantment SWEEPING_EDGE = new Enchantment();
    public static final Enchantment PIERCING = new Enchantment();
    public static final Enchantment QUICK_CHARGE = new Enchantment();
}""")
write_file("org/bukkit/inventory/ItemStack.java", """package org.bukkit.inventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import java.util.Map;
public class ItemStack implements Cloneable {
    public ItemStack(Material type) {}
    public ItemStack(Material type, int amount) {}
    public Material getType() { return Material.AIR; }
    public int getAmount() { return 1; }
    public int getEnchantmentLevel(Enchantment ench) { return 0; }
    public boolean containsEnchantment(Enchantment ench) { return false; }
    public Map<Enchantment, Integer> getEnchantments() { return null; }
    public ItemStack clone() { return this; }
}
""")
write_file("org/bukkit/inventory/PlayerInventory.java", """package org.bukkit.inventory;
public interface PlayerInventory {
    ItemStack getItemInMainHand();
    ItemStack getItemInOffHand();
    ItemStack getItemInHand();
    ItemStack getHelmet();
    ItemStack getChestplate();
    ItemStack getLeggings();
    ItemStack getBoots();
    ItemStack[] getArmorContents();
    ItemStack[] getContents();
    int getHeldItemSlot();
}
""")

# Bukkit Entities

write_file("org/bukkit/metadata/MetadataValue.java", """package org.bukkit.metadata;
public interface MetadataValue {
    Object value();
    int asInt();
    float asFloat();
    double asDouble();
    long asLong();
    short asShort();
    byte asByte();
    boolean asBoolean();
    String asString();
}
""")

write_file("org/bukkit/metadata/Metadatable.java", """package org.bukkit.metadata;
import java.util.List;
import org.bukkit.plugin.Plugin;
public interface Metadatable {
    void setMetadata(String metadataKey, MetadataValue newMetadataValue);
    List<MetadataValue> getMetadata(String metadataKey);
    boolean hasMetadata(String metadataKey);
    void removeMetadata(String metadataKey, Plugin owningPlugin);
}
""")

write_file("org/bukkit/entity/Entity.java", """package org.bukkit.entity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import java.util.UUID;
import java.util.List;
public interface Entity extends org.bukkit.metadata.Metadatable {
    int getEntityId();
    UUID getUniqueId();
    String getName();
    Location getLocation();
    World getWorld();
    Vector getVelocity();
    void setVelocity(Vector vel);
    EntityType getType();
    boolean isOnGround();
    boolean isDead();
    boolean isValid();
    boolean isInsideVehicle();
    Entity getVehicle();
    List<Entity> getPassengers();
    void remove();
    double getHeight();
    double getWidth();
    boolean hasGravity();
    boolean isLeashed();
    boolean leaveVehicle();
    boolean teleport(Location loc);
    boolean teleport(Location loc, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause cause);
    default io.papermc.paper.threadedregions.scheduler.EntityScheduler getScheduler() { return null; }
}""")
write_file("org/bukkit/entity/EntityType.java", """package org.bukkit.entity;
public enum EntityType {
    PLAYER, ARROW, BOAT, CHEST_BOAT, DONKEY, HORSE, SKELETON_HORSE, ZOMBIE_HORSE, PIG,
    CAMEL, ITEM_FRAME, GLOW_ITEM_FRAME, FIREWORK, EGG, SNOWBALL, THROWN_EXP_BOTTLE,
    ENDER_DRAGON, COMPLEX_PART, SHULKER, LLAMA, TRIDENT, UNKNOWN, COW, CHICKEN, WOLF,
    VILLAGER, SHEEP, ZOMBIE, SKELETON, CREEPER, SPIDER, ENDERMAN
}""")

write_file("org/bukkit/projectiles/ProjectileSource.java", """package org.bukkit.projectiles;
public interface ProjectileSource {}
""")

write_file("org/bukkit/entity/Projectile.java", """package org.bukkit.entity;
import org.bukkit.projectiles.ProjectileSource;
public interface Projectile extends Entity {
    ProjectileSource getShooter();
    void setShooter(ProjectileSource shooter);
}
""")

write_file("org/bukkit/entity/Firework.java", """package org.bukkit.entity;
import org.bukkit.projectiles.ProjectileSource;
public interface Firework extends Projectile {
    ProjectileSource getShooter();
}
""")

write_file("org/bukkit/entity/Arrow.java", """package org.bukkit.entity;
import org.bukkit.projectiles.ProjectileSource;
public interface Arrow extends Projectile {
    ProjectileSource getShooter();
}
""")

write_file("org/bukkit/inventory/HorseInventory.java", """package org.bukkit.inventory;
public interface HorseInventory {
    ItemStack getSaddle();
    ItemStack getArmor();
}""")

write_file("org/bukkit/entity/Horse.java", """package org.bukkit.entity;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
public interface Horse extends LivingEntity {
    HorseInventory getInventory();
    AttributeInstance getAttribute(Attribute attribute);
}
""")

write_file("org/bukkit/entity/LivingEntity.java", """package org.bukkit.entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import java.util.Collection;
import java.util.Set;
public interface LivingEntity extends Entity {
    double getHealth();
    double getMaxHealth();
    double getEyeHeight();
    Location getEyeLocation();
    boolean hasPotionEffect(PotionEffectType type);
    PotionEffect getPotionEffect(PotionEffectType type);
    Collection<PotionEffect> getActivePotionEffects();
    boolean isGliding();
    boolean isSwimming();
    boolean isRiptiding();
    boolean isSleeping();
    int getMaximumNoDamageTicks();
    void setMaximumNoDamageTicks(int ticks);
    Block getTargetBlock(Set<Material> transparent, int maxDistance);
    AttributeInstance getAttribute(Attribute attribute);
}""")
write_file("org/bukkit/entity/Player.java", """package org.bukkit.entity;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import net.md_5.bungee.api.chat.BaseComponent;
import java.net.InetSocketAddress;
import java.util.List;

public interface Player extends LivingEntity, CommandSender {
    PlayerInventory getInventory();
    GameMode getGameMode();
    boolean isFlying();
    boolean getAllowFlight();
    float getFlySpeed();
    float getWalkSpeed();
    boolean isSneaking();
    boolean isSprinting();
    boolean isBlocking();
    int getFoodLevel();
    int getLevel();
    float getExp();
    int getTotalExperience();
    int getPing();
    ItemStack getItemInHand();
    void setItemInHand(ItemStack item);
    ItemStack getItemInMainHand();
    ItemStack getItemInOffHand();
    WeatherType getPlayerWeather();
    void kickPlayer(String message);
    List<Entity> getNearbyEntities(double x, double y, double z);
    boolean performCommand(String command);
    boolean teleport(Location loc);
    boolean teleport(Entity entity);
    boolean teleport(Location loc, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause cause);
    InetSocketAddress getAddress();
    void sendBlockChange(Location loc, org.bukkit.Material material, byte data);
    void sendBlockChange(Location loc, org.bukkit.block.data.BlockData blockData);
    void playEffect(Location loc, org.bukkit.Effect effect, int data);
    void sendPluginMessage(org.bukkit.plugin.Plugin source, String channel, byte[] message);
    Spigot spigot();
    public class Spigot {
        public void sendMessage(BaseComponent... components) {}
        public void sendMessage(BaseComponent component) {}
    }
}""")

# Specific Entity sub-interfaces
for ent in ["Boat", "Camel", "Donkey", "Egg", "EnderDragon", "EnderDragonPart", "ItemFrame", "Pig", "SkeletonHorse", "Snowball", "ThrownExpBottle", "ZombieHorse"]:
    write_file(f"org/bukkit/entity/{ent}.java", f"""package org.bukkit.entity;
public interface {ent} extends Entity {{}}
""")

# ProjectileSource
write_file("org/bukkit/projectiles/ProjectileSource.java", """package org.bukkit.projectiles;
public interface ProjectileSource {}""")

# Potions & Attributes
write_file("org/bukkit/potion/PotionEffectType.java", """package org.bukkit.potion;
public abstract class PotionEffectType {
    public static final PotionEffectType SPEED = null;
    public static final PotionEffectType SLOW = null;
    public static final PotionEffectType FAST_DIGGING = null;
    public static final PotionEffectType SLOW_DIGGING = null;
    public static final PotionEffectType JUMP = null;
    public static final PotionEffectType LEVITATION = null;
    public static final PotionEffectType SLOW_FALLING = null;
    public static final PotionEffectType DOLPHINS_GRACE = null;
    public static final PotionEffectType CONDUIT_POWER = null;
    public static final PotionEffectType BLINDNESS = null;
    public static final PotionEffectType NIGHT_VISION = null;
    public abstract String getName();
    public abstract int getId();
}
""")
write_file("org/bukkit/potion/PotionEffect.java", """package org.bukkit.potion;
public class PotionEffect {
    public PotionEffect(PotionEffectType type, int duration, int amplifier) {}
    public PotionEffectType getType() { return null; }
    public int getDuration() { return 0; }
    public int getAmplifier() { return 0; }
}
""")

write_file("org/bukkit/attribute/AttributeInstance.java", """package org.bukkit.attribute;
public interface AttributeInstance {
    double getBaseValue();
    double getValue();
}
""")

write_file("org/bukkit/attribute/Attribute.java", """package org.bukkit.attribute;
public enum Attribute {
    GENERIC_MAX_HEALTH,
    GENERIC_FOLLOW_RANGE,
    GENERIC_KNOCKBACK_RESISTANCE,
    GENERIC_MOVEMENT_SPEED,
    GENERIC_ATTACK_DAMAGE,
    GENERIC_ATTACK_SPEED,
    GENERIC_ARMOR,
    GENERIC_ARMOR_TOUGHNESS,
    GENERIC_LUCK,
    HORSE_JUMP_STRENGTH,
    ZOMBIE_SPAWN_REINFORCEMENTS;
}""")

# Bukkit Events
write_file("org/bukkit/event/Event.java", """package org.bukkit.event;
public abstract class Event {
    public Event() {}
    public Event(boolean isAsync) {}
    public String getEventName() { return getClass().getSimpleName(); }
    public boolean isAsynchronous() { return false; }
    public static HandlerList getHandlerList() { return null; }
    public HandlerList getHandlers() { return null; }
}
""")
write_file("org/bukkit/event/Cancellable.java", """package org.bukkit.event;
public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancel);
}
""")
write_file("org/bukkit/event/Listener.java", """package org.bukkit.event;
public interface Listener {}
""")
write_file("org/bukkit/event/EventHandler.java", """package org.bukkit.event;
import java.lang.annotation.*;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    EventPriority priority() default EventPriority.NORMAL;
    boolean ignoreCancelled() default false;
}
""")
write_file("org/bukkit/event/EventPriority.java", """package org.bukkit.event;
public enum EventPriority { LOWEST, LOW, NORMAL, HIGH, HIGHEST, MONITOR }
""")
write_file("org/bukkit/event/HandlerList.java", """package org.bukkit.event;
public class HandlerList {
    public static void unregisterAll(Listener listener) {}
}
""")

# Event sub-packages
write_file("org/bukkit/event/block/Action.java", """package org.bukkit.event.block;
public enum Action { LEFT_CLICK_BLOCK, RIGHT_CLICK_BLOCK, LEFT_CLICK_AIR, RIGHT_CLICK_AIR, PHYSICAL }
""")

write_file("org/bukkit/event/block/BlockEvent.java", """package org.bukkit.event.block;
import org.bukkit.event.Event;
import org.bukkit.block.Block;
public abstract class BlockEvent extends Event {
    protected Block block;
    public BlockEvent(Block block) { this.block = block; }
    public Block getBlock() { return block; }
}
""")

write_file("org/bukkit/event/block/BlockBreakEvent.java", """package org.bukkit.event.block;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
public class BlockBreakEvent extends Event implements Cancellable {
    public Block getBlock() { return null; }
    public Player getPlayer() { return null; }
    public boolean isCancelled() { return false; }
    public void setCancelled(boolean cancel) {}
}
""")
write_file("org/bukkit/event/block/BlockPlaceEvent.java", """package org.bukkit.event.block;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
public class BlockPlaceEvent extends Event implements Cancellable {
    public Block getBlock() { return null; }
    public Block getBlockPlaced() { return null; }
    public Block getBlockAgainst() { return null; }
    public Player getPlayer() { return null; }
    public boolean isCancelled() { return false; }
    public void setCancelled(boolean cancel) {}
}
""")
write_file("org/bukkit/event/block/BlockDamageEvent.java", """package org.bukkit.event.block;
import org.bukkit.event.Cancellable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class BlockDamageEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;
    private boolean instaBreak;
    private Player player;
    public BlockDamageEvent(Player player, Block block, ItemStack itemInHand, boolean instaBreak) { super(block); this.player = player; this.instaBreak = instaBreak; }
    public Player getPlayer() { return player; }
    public boolean getInstaBreak() { return instaBreak; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}""")
write_file("org/bukkit/event/entity/EntityDamageEvent.java", """package org.bukkit.event.entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.entity.Entity;
public class EntityDamageEvent extends Event implements Cancellable {
    public enum DamageCause { CONTACT, ENTITY_ATTACK, PROJECTILE, SUFFOCATION, FALL, FIRE, FIRE_TICK, MELTING, LAVA, DROWNING, BLOCK_EXPLOSION, ENTITY_EXPLOSION, VOID, LIGHTNING, SUICIDE, STARVATION, POISON, MAGIC, WITHER, FALLING_BLOCK, THORNS, CUSTOM, FLY_INTO_WALL, HOT_FLOOR, CRAMMING, DRYOUT, FREEZE, SONIC_BOOM }
    private Entity entity;
    private double damage;
    private boolean cancelled;
    public EntityDamageEvent(Entity entity, double damage) { this.entity = entity; this.damage = damage; }
    public Entity getEntity() { return entity; }
    public double getDamage() { return damage; }
    public void setDamage(double damage) { this.damage = damage; }
    public DamageCause getCause() { return DamageCause.CUSTOM; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}""")
write_file("org/bukkit/event/entity/EntityDamageByEntityEvent.java", """package org.bukkit.event.entity;
import org.bukkit.entity.Entity;
public class EntityDamageByEntityEvent extends EntityDamageEvent {
    private Entity damager;
    public EntityDamageByEntityEvent(Entity damager, Entity damagee, double damage) { super(damagee, damage); this.damager = damager; }
    public Entity getDamager() { return damager; }
}""")
write_file("org/bukkit/event/entity/EntityResurrectEvent.java", """package org.bukkit.event.entity;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.entity.LivingEntity;
public class EntityResurrectEvent extends Event implements Cancellable {
    public LivingEntity getEntity() { return null; }
    public boolean isCancelled() { return false; }
    public void setCancelled(boolean cancel) {}
}
""")
write_file("org/bukkit/event/entity/EntitySpawnEvent.java", """package org.bukkit.event.entity;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.entity.Entity;
public class EntitySpawnEvent extends Event implements Cancellable {
    public Entity getEntity() { return null; }
    public boolean isCancelled() { return false; }
    public void setCancelled(boolean cancel) {}
}
""")
write_file("org/bukkit/event/entity/PlayerDeathEvent.java", """package org.bukkit.event.entity;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
public class PlayerDeathEvent extends Event {
    public Player getEntity() { return null; }
}
""")
write_file("org/bukkit/event/entity/ProjectileLaunchEvent.java", """package org.bukkit.event.entity;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.entity.Entity;
public class ProjectileLaunchEvent extends Event implements Cancellable {
    public Entity getEntity() { return null; }
    public boolean isCancelled() { return false; }
    public void setCancelled(boolean cancel) {}
}
""")

# Player events
for pev in ["PlayerBedEnterEvent", "PlayerBedLeaveEvent", "PlayerChangedWorldEvent", "PlayerInteractEvent", "PlayerItemConsumeEvent", "PlayerJoinEvent", "PlayerPickupItemEvent", "PlayerQuitEvent", "PlayerRiptideEvent"]:
    write_file(f"org/bukkit/event/player/{pev}.java", f"""package org.bukkit.event.player;
import org.bukkit.event.Event;
import org.bukkit.event.Cancellable;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
public class {pev} extends Event implements Cancellable {{
    public Player getPlayer() {{ return null; }}
    public Location getFrom() {{ return null; }}
    public Location getTo() {{ return null; }}
    public Block getClickedBlock() {{ return null; }}
    public Action getAction() {{ return Action.RIGHT_CLICK_BLOCK; }}
    public ItemStack getItem() {{ return null; }}
    public boolean isCancelled() {{ return false; }}
    public void setCancelled(boolean cancel) {{}}
}}
""")


write_file("org/bukkit/event/player/PlayerEvent.java", """package org.bukkit.event.player;
import org.bukkit.event.Event;
import org.bukkit.entity.Player;
public abstract class PlayerEvent extends Event {
    protected Player player;
    public PlayerEvent(Player player) { this.player = player; }
    public Player getPlayer() { return player; }
}
""")

write_file("org/bukkit/event/player/PlayerMoveEvent.java", """package org.bukkit.event.player;
import org.bukkit.event.Cancellable;
import org.bukkit.entity.Player;
import org.bukkit.Location;
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    private Location from, to;
    private boolean cancelled;
    public PlayerMoveEvent(Player player, Location from, Location to) { super(player); this.from = from; this.to = to; }
    public Location getFrom() { return from; }
    public Location getTo() { return to; }
    public void setTo(Location to) { this.to = to; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}
""")

write_file("org/bukkit/event/player/PlayerTeleportEvent.java", """package org.bukkit.event.player;
import org.bukkit.entity.Player;
import org.bukkit.Location;
public class PlayerTeleportEvent extends PlayerMoveEvent {
    public enum TeleportCause { COMMAND, PLUGIN, UNKNOWN, NETHER_PORTAL, END_PORTAL, SPECTATE, ENDER_PEARL, CHORUS_FRUIT }
    public PlayerTeleportEvent(Player player, Location from, Location to) { super(player, from, to); }
    public TeleportCause getCause() { return TeleportCause.UNKNOWN; }
}
""")

write_file("org/bukkit/event/player/PlayerFishEvent.java", """package org.bukkit.event.player;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
public class PlayerFishEvent extends PlayerEvent {
    public PlayerFishEvent(Player player) { super(player); }
    public Entity getCaught() { return null; }
}
""")

write_file("org/bukkit/event/player/PlayerKickEvent.java", """package org.bukkit.event.player;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    public PlayerKickEvent(Player player) { super(player); }
    public String getReason() { return ""; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}""")

write_file("org/bukkit/event/player/PlayerInteractEvent.java", """package org.bukkit.event.player;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.Cancellable;

public class PlayerInteractEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    public PlayerInteractEvent(Player player) { super(player); }
    public Location getFrom() { return null; }
    public Location getTo() { return null; }
    public Block getClickedBlock() { return null; }
    public BlockFace getBlockFace() { return BlockFace.UP; }
    public Action getAction() { return Action.RIGHT_CLICK_BLOCK; }
    public ItemStack getItem() { return null; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}""")

write_file("org/bukkit/event/player/PlayerInteractEntityEvent.java", """package org.bukkit.event.player;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
public class PlayerInteractEntityEvent extends PlayerEvent {
    public PlayerInteractEntityEvent(Player player) { super(player); }
    public Entity getRightClicked() { return null; }
}
""")

# Bukkit Plugins & Scheduler & Utils

write_file("org/bukkit/plugin/PluginDescriptionFile.java", """package org.bukkit.plugin;
public class PluginDescriptionFile {
    public String getName() { return "Vulcan"; }
    public String getVersion() { return "2.9.7.23"; }
    public java.util.List<String> getAuthors() { return java.util.Collections.emptyList(); }
}
""")


write_file("org/bukkit/plugin/messaging/Messenger.java", """package org.bukkit.plugin.messaging;
import org.bukkit.plugin.Plugin;
public interface Messenger {
    void registerOutgoingPluginChannel(Plugin plugin, String channel);
}
""")

write_file("org/bukkit/plugin/Plugin.java", """package org.bukkit.plugin;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
public interface Plugin {
    PluginDescriptionFile getDescription();
    java.io.InputStream getResource(String filename);
    File getDataFolder();
    Logger getLogger();
    String getName();
    boolean isEnabled();
    Server getServer();
    void onEnable();
    void onDisable();
}""")
write_file("org/bukkit/plugin/java/JavaPlugin.java", """package org.bukkit.plugin.java;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.Server;
import org.bukkit.Bukkit;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class JavaPlugin implements Plugin {
    public PluginDescriptionFile getDescription() { return new PluginDescriptionFile(); }
    public java.io.InputStream getResource(String filename) { return null; }
    public Logger getLogger() { return Logger.getLogger("Vulcan"); }
    public FileConfiguration getConfig() { return new org.bukkit.configuration.file.YamlConfiguration(); }
    public void saveDefaultConfig() {}
    public void reloadConfig() {}
    public void saveConfig() {}
    public java.io.File getDataFolder() { return new java.io.File("plugins/Vulcan"); }
    public Server getServer() { return Bukkit.getServer(); }
    public String getName() { return "Vulcan"; }
    public boolean isEnabled() { return true; }
    public org.bukkit.command.PluginCommand getCommand(String name) { return new org.bukkit.command.PluginCommand(); }
    public void onEnable() {}
    public void onDisable() {}
}""")
write_file("org/bukkit/command/PluginCommand.java", """package org.bukkit.command;
public class PluginCommand extends Command {
    public PluginCommand() { super(""); }
    public PluginCommand(String name, org.bukkit.plugin.Plugin owner) { super(name); }
    public void setExecutor(CommandExecutor executor) {}
    public void setTabCompleter(TabCompleter completer) {}
    public boolean execute(CommandSender sender, String commandLabel, String[] args) { return true; }
}""")
write_file("org/bukkit/command/PluginIdentifiableCommand.java", """package org.bukkit.command;
import org.bukkit.plugin.Plugin;
public interface PluginIdentifiableCommand {
    Plugin getPlugin();
}
""")
write_file("org/bukkit/plugin/PluginManager.java", """package org.bukkit.plugin;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
public interface PluginManager {
    void registerEvents(Listener listener, Plugin plugin);
    void callEvent(Event event);
    Plugin getPlugin(String name);
    Plugin[] getPlugins();
    boolean isPluginEnabled(String name);
    boolean isPluginEnabled(Plugin plugin);
}
""")

write_file("org/bukkit/plugin/RegisteredServiceProvider.java", """package org.bukkit.plugin;
public class RegisteredServiceProvider<T> {
    public T getProvider() { return null; }
    public Plugin getPlugin() { return null; }
    public ServicePriority getPriority() { return ServicePriority.Normal; }
}""")

write_file("org/bukkit/plugin/ServicesManager.java", """package org.bukkit.plugin;
import java.util.Collection;
import java.util.List;
public interface ServicesManager {
    <T> void register(Class<T> service, T provider, Plugin plugin, ServicePriority priority);
    Collection<Class<?>> getKnownServices();
    Collection getRegistrations(Class service);
    <T> RegisteredServiceProvider<T> getRegistration(Class<T> service);
}""")
write_file("org/bukkit/plugin/RegisteredServiceProvider.java", """package org.bukkit.plugin;
public class RegisteredServiceProvider<T> {
    public T getProvider() { return null; }
    public Plugin getPlugin() { return null; }
    public ServicePriority getPriority() { return ServicePriority.Normal; }
}""")
write_file("org/bukkit/plugin/ServicePriority.java", """package org.bukkit.plugin;
public enum ServicePriority { Lowest, Low, Normal, High, Highest }""")
write_file("org/bukkit/scheduler/BukkitTask.java", """package org.bukkit.scheduler;
import org.bukkit.plugin.Plugin;
public interface BukkitTask {
    int getTaskId();
    Plugin getOwner();
    boolean isSync();
    void cancel();
    boolean isCancelled();
}
""")
write_file("org/bukkit/scheduler/BukkitScheduler.java", """package org.bukkit.scheduler;
import org.bukkit.plugin.Plugin;
public interface BukkitScheduler {
    BukkitTask runTask(Plugin plugin, Runnable task);
    BukkitTask runTaskAsynchronously(Plugin plugin, Runnable task);
    BukkitTask runTaskLater(Plugin plugin, Runnable task, long delay);
    BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay);
    BukkitTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period);
    BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period);
    void cancelTask(int taskId);
    void cancelTasks(Plugin plugin);
    boolean isCurrentlyRunning(int taskId);
    int scheduleSyncDelayedTask(Plugin plugin, Runnable task);
    int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);
}
""")
write_file("org/bukkit/util/Vector.java", """package org.bukkit.util;
import org.bukkit.Location;
import org.bukkit.World;

public class Vector implements Cloneable {
    public double x, y, z;
    public Vector() { this(0, 0, 0); }
    public Vector(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public int getBlockX() { return (int)Math.floor(x); }
    public int getBlockY() { return (int)Math.floor(y); }
    public int getBlockZ() { return (int)Math.floor(z); }
    public Vector setX(double x) { this.x = x; return this; }
    public Vector setY(double y) { this.y = y; return this; }
    public Vector setZ(double z) { this.z = z; return this; }
    public Vector setX(int x) { this.x = x; return this; }
    public Vector setY(int y) { this.y = y; return this; }
    public Vector setZ(int z) { this.z = z; return this; }
    public Vector add(Vector other) { this.x += other.x; this.y += other.y; this.z += other.z; return this; }
    public Vector subtract(Vector other) { this.x -= other.x; this.y -= other.y; this.z -= other.z; return this; }
    public Vector multiply(double m) { this.x *= m; this.y *= m; this.z *= m; return this; }
    public double length() { return Math.sqrt(x * x + y * y + z * z); }
    public double lengthSquared() { return x * x + y * y + z * z; }
    public double distance(Vector other) { return Math.sqrt(distanceSquared(other)); }
    public double distanceSquared(Vector other) { double dx = x - other.x, dy = y - other.y, dz = z - other.z; return dx * dx + dy * dy + dz * dz; }
    public Vector normalize() { double l = length(); if (l != 0) { x /= l; y /= l; z /= l; } return this; }
    public double angle(Vector other) { return 0.0; }
    public Location toLocation(World world) { return new Location(world, x, y, z); }
    public Vector clone() { return new Vector(x, y, z); }
}""")
write_file("org/bukkit/util/NumberConversions.java", """package org.bukkit.util;
public class NumberConversions {
    public static int floor(double num) { return (int)Math.floor(num); }
    public static int ceil(double num) { return (int)Math.ceil(num); }
    public static int round(double num) { return (int)Math.round(num); }
    public static double square(double num) { return num * num; }
    public static int toInt(Object obj) { return obj instanceof Number ? ((Number)obj).intValue() : 0; }
    public static double toDouble(Object obj) { return obj instanceof Number ? ((Number)obj).doubleValue() : 0.0; }
}
""")
write_file("org/bukkit/util/StringUtil.java", """package org.bukkit.util;
import java.util.Collection;
public class StringUtil {
    public static <T extends Collection<? super String>> T copyPartialMatches(String token, Iterable<String> originals, T collection) {
        for (String s : originals) {
            if (startsWithIgnoreCase(s, token)) collection.add(s);
        }
        return collection;
    }
    public static boolean startsWithIgnoreCase(String string, String prefix) {
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
""")

# Compile stubs
java_files = [p.as_posix() for p in STUB_ROOT.rglob("*.java")]
print(f"Generated {len(java_files)} stub Java files")

stub_file_list = ROOT / "tools" / "stub_sources.txt"
with open(stub_file_list, "w", encoding="utf-8") as f:
    for jf in java_files:
        f.write(f'"{jf}"\n')

subprocess.check_call(["javac", "-encoding", "UTF-8", "-d", str(CLASSES_DIR), f"@{stub_file_list}"])
if OUT_JAR.exists():
    OUT_JAR.unlink()
subprocess.check_call(["jar", "cf", str(OUT_JAR), "-C", str(CLASSES_DIR), "."])
print(f"Successfully created stubs.jar: {OUT_JAR}")
