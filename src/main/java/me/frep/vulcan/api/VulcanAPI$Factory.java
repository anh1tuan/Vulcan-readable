package me.frep.vulcan.api;

import org.jetbrains.annotations.Nullable;


public class VulcanAPI$Factory {
    private static VulcanAPI api;

    @Nullable
    public static VulcanAPI getApi() {
        return api;
    }

    public static void setApi(VulcanAPI vulcanAPI) {
        api = vulcanAPI;
    }
}
