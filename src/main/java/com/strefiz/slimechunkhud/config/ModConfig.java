package com.strefiz.slimechunkhud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Persistência da configuraçāo em {@code config/slimechunkhud.json} (Gson).
 * <p>
 * As sementes de multiplayer sāo guardadas por servidor (chave = endereço).
 * O diretório de configuraçōes vem do FabricLoader (nāo de MinecraftClient).
 */
public final class ModConfig {

    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("slimechunkhud.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static ModConfig instance;

    @SerializedName("hudEnabled")
    public boolean hudEnabled = true;

    /** Quando true, mostra notificaçāo acima da hotbar ao entrar numa slime chunk em vez do HUD contínuo. */
    @SerializedName("notificationMode")
    public boolean notificationMode = false;

    /** Quando true, ativa a notificaçāo de entrada na slime chunk (só relevante se notificationMode=true). */
    @SerializedName("notifyOnEnterSlimeChunk")
    public boolean notifyOnEnterSlimeChunk = true;

    @SerializedName("hudAnchor")
    public HudAnchor hudAnchor = HudAnchor.TOP_LEFT;

    @SerializedName("hudOffsetX")
    public int hudOffsetX = 10;

    @SerializedName("hudOffsetY")
    public int hudOffsetY = 10;

    @SerializedName("hudScale")
    public float hudScale = 1.0f;

    @SerializedName("showBackground")
    public boolean showBackground = true;

    @SerializedName("textColorSlime")
    public int textColorSlime = 0xFF00FF00; // verde

    @SerializedName("textColorNotSlime")
    public int textColorNotSlime = 0xFFFF5555; // vermelho

    @SerializedName("textColorUnknown")
    public int textColorUnknown = 0xFFFFFF00; // amarelo

    @SerializedName("borderOverlayEnabled")
    public boolean borderOverlayEnabled = true;

    @SerializedName("slimeBorderColor")
    public int slimeBorderColor = 0x00FF00; // verde

    @SerializedName("borderOpacity")
    public float borderOpacity = 0.5f;

    @SerializedName("overlayRadius")
    public int overlayRadius = 6; // chunks em volta do jogador

    @SerializedName("serverSeeds")
    public Map<String, Long> serverSeeds = new LinkedHashMap<>();

    private ModConfig() {}

    public static ModConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static ModConfig load() {
        try {
            Files.createDirectories(CONFIG_DIR);
            if (Files.exists(CONFIG_FILE)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_FILE)) {
                    ModConfig cfg = GSON.fromJson(reader, ModConfig.class);
                    if (cfg != null) {
                        instance = cfg;
                        return instance;
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            // Falha ao ler config (incluindo JSON corrompido): usa padrões.
            e.printStackTrace();
        }
        instance = new ModConfig();
        return instance;
    }

    public static void save() {
        ModConfig cfg = get();
        try {
            Files.createDirectories(CONFIG_DIR);
            try (Writer writer = Files.newBufferedWriter(CONFIG_FILE)) {
                GSON.toJson(cfg, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Ăancoras possíveis para o HUD. */
    public enum HudAnchor {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT,
        MIDDLE_LEFT, MIDDLE_RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }

    /** Fornece a config atual (permite reler a cada frame sem reiniciar). */
    @FunctionalInterface
    public interface Supplier {
        ModConfig get();
    }

    public static void resetForTests() {
        instance = null;
    }
}
