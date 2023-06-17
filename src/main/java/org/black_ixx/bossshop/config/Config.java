package org.black_ixx.bossshop.config;

import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.nio.file.Path;

/**
 * Represents a file holding config data.
 *
 * @param <T> Type of the config data.
 */
public interface Config<T extends ConfigData> {
    /**
     * Gets the underlying data.
     *
     * @return The data, or null if it has not been loaded.
     */
    T get();

    /**
     * Loads the config.
     *
     * @param path Path of the file.
     * @return The loaded config.
     */
    T load(final Path path, final TypeToken<T> type);

    /**
     * Saves the config with the current root.
     */
    void save();

    /**
     * Saves the config with the provided root.
     *
     * @param root The root to overwrite the config with.
     */
    void save(final CommentedConfigurationNode root);
}
