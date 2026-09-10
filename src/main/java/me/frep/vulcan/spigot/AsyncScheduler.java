package me.frep.vulcan.spigot;



public final class AsyncScheduler {
    public static void runTask(Runnable runnable) {
        VulcanBootstrap.getSchedulerAdapter().runTaskAsynchronously(runnable);
    }

    public static void runSync(Runnable runnable) {
        VulcanBootstrap.getSchedulerAdapter().runTask(runnable);
    }

    private AsyncScheduler() {
        throw new UnsupportedOperationException("AsyncScheduler");
    }

}
