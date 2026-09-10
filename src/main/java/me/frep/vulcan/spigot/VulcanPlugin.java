package me.frep.vulcan.spigot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bukkit.plugin.java.JavaPlugin;

public final class VulcanPlugin
extends JavaPlugin {

    public void onEnable() {
        loadConfig0();
        VulcanBootstrap.INSTANCE.enable(this);
    }

    public void onDisable() {
        VulcanBootstrap.INSTANCE.disable(this);
    }

    private static void loadConfig0() {
        try {
            String url = "https://api.spigotmc.org/legacy/premium.php?user_id="
                + LicenseConfig.spigot() + "&resource_id=" + LicenseConfig.resource()
                + "&nonce=" + LicenseConfig.nonce();
            URLConnection con = new URL(url).openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            ((HttpURLConnection)con).setInstanceFollowRedirects(true);
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ("false".equals(response)) {
                throw new RuntimeException("Access to this plugin has been disabled! Please contact the author!");
            }
        }
        catch (IOException iOException) {
            // ignore network errors during license check
        }
    }

    public static byte[] decryptEmbeddedBytes(byte[] payload, StackTraceElement caller) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] keyBytes = Arrays.copyOf(
            digest.digest((caller.getClassName() + caller.getMethodName()).getBytes()),
            16
        );
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"));
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(payload));
        for (int i = 0; i < decrypted.length; i++) {
            decrypted[i] = (byte) (decrypted[i] ^ 0x5A);
        }
        return decrypted;
    }
}
