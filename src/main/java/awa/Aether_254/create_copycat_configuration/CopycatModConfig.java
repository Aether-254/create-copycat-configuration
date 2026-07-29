package awa.Aether_254.create_copycat_configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.neoforged.fml.loading.FMLPaths;

public final class CopycatModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FMLPaths.CONFIGDIR.get().resolve("create_copycat_configuration.json");
    private static Data data = new Data();

    private CopycatModConfig() {
    }

    public static Data get() {
        return data;
    }

    public static void load() {
        try {
            if (Files.isRegularFile(PATH)) {
                Data loaded = GSON.fromJson(Files.readString(PATH), Data.class);
                data = loaded == null ? new Data() : loaded;
            }
        } catch (IOException | RuntimeException ignored) {
            data = new Data();
        }
        save();
    }

    public static void save() {
        data.defaultBrightness = Math.max(0, Math.min(15, data.defaultBrightness));
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(data));
        } catch (IOException ignored) {
        }
    }

    public static final class Data {
        public boolean enabled = true;
        public boolean defaultCollision = true;
        public boolean defaultLightOcclusion = true;
        public int defaultBrightness = 0;
    }
}
