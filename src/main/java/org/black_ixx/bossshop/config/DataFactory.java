package org.black_ixx.bossshop.config;

import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Factory for Configuration creation.
 */
public final class DataFactory {
    private final Path path;
    private Config<PageLayoutData> pageLayouts;
    private Config<MessagesData> messages;
    private Config<SettingsData> settings;

    /**
     * Constructs the object.
     *
     * @param path The configuration path.
     */
    public DataFactory(final Path path) {
        this.path = path;
    }

    /**
     * Creates a file and path's directories if they do not exist.
     *
     * @param path The path to create the file.
     * @param name Name of the file to be created.
     * @return the path of the created file.
     */
    public static Path createFile(final Path path, final String name) {
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
            return Files.createFile(path.resolve(name));
        } catch (FileAlreadyExistsException ignored) { //Ignore if file already exists.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path.resolve(name);
    }

    public void loadPageLayouts() {
        this.pageLayouts = new BaseConfig<>();
        Path filePath = this.createFile("test-pagelayout.yml");
        this.pageLayouts.load(filePath, TypeToken.get(PageLayoutData.class));
    }

    public void loadMessages() {
        this.messages = new BaseConfig<>();
        Path filePath = this.createFile("test-messages.yml");
        this.messages.load(filePath, TypeToken.get(MessagesData.class));
    }

    public void loadSettings() {
        this.settings = new BaseConfig<>();
        Path filePath = this.createFile("test-config.yml");
        this.settings.load(filePath, TypeToken.get(SettingsData.class));
    }

    /**
     * Gets the page layout object.
     *
     * @return The page layout data.
     */
    public @NotNull PageLayoutData pageLayouts() {
        if (this.pageLayouts == null) {
            this.loadPageLayouts();
        }
        return this.pageLayouts.get();
    }

    /**
     * Gets the message data object.
     *
     * @return The message data.
     */
    public @NotNull MessagesData messages() {
        if (this.messages == null) {
            this.loadMessages();
        }
        return this.messages.get();
    }

    /**
     * Gets the settings data object.
     *
     * @return The settings data.
     */
    public @NotNull SettingsData settings() {
        if (this.settings == null) {
            this.loadSettings();
        }
        return this.settings.get();
    }

    /**
     * Creates a file and path's directories if they do not exist.
     *
     * @param name Name of the file to be created.
     * @return the path of the created file.
     */
    private Path createFile(final String name) {
        return DataFactory.createFile(this.path, name);
    }
}
