package ac.vulcan.anticheat;

import org.bukkit.plugin.Plugin;

public final class UniversalScheduler {
    public static final boolean IS_FOLIA = classExists("io.papermc.paper.threadedregions.RegionizedServer")
        || classExists("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
    public static final boolean IS_CANVAS = classExists("io.canvasmc.canvas.server.ThreadedServer")
        || classExists("io.canvasmc.canvas.server.CanvasServer");
    public static final boolean HAS_FOLIA_SCHEDULER =
        classExists("io.papermc.paper.threadedregions.scheduler.ScheduledTask");

    public static SchedulerAdapter createSchedulerAdapter(Plugin plugin) {
        if (IS_FOLIA || IS_CANVAS || HAS_FOLIA_SCHEDULER) {
            return new FoliaSchedulerAdapter(plugin);
        }
        return new BukkitSchedulerAdapter(plugin);
    }

    public static SchedulerAdapter createSchedulerAdapter(Object[] args) {
        return createSchedulerAdapter((Plugin) args[0]);
    }

    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static boolean classExists(Object[] args) {
        return classExists((String) args[0]);
    }
}
