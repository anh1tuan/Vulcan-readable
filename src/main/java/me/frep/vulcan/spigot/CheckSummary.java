package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.List;

public class CheckSummary {
    private final int transactionId;
    private final long createdAtMillis;
    private final List pendingRunnables = new ArrayList();

    public CheckSummary(int transactionId, long createdAtMillis) {
        this.transactionId = transactionId;
        this.createdAtMillis = createdAtMillis;
    }

    public boolean addPendingRunnable(Runnable runnable) {
        return this.pendingRunnables.add(runnable);
    }

    public void runPendingRunnables() {
        for (Object task : this.pendingRunnables) {
            ((Runnable) task).run();
        }
        this.pendingRunnables.clear();
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public long getCreatedAtMillis() {
        return this.createdAtMillis;
    }

    public List getPendingRunnables() {
        return this.pendingRunnables;
    }
}
