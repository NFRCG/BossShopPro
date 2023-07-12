package org.black_ixx.bossshop.files;

import org.black_ixx.bossshop.config.DataFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

/**
 * Basic Logger which also writes to file to replace BSP's BugFinder.yml.
 * <br><br>
 * This may be removed in the future because the feature inherently makes little sense. Issues can be read from console.
 */
public final class ErrorLog {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");
    private static BufferedWriter writer;

    private ErrorLog() {
    }

    public static void init() {
        Path path = JavaPlugin.getProvidingPlugin(ErrorLog.class).getDataFolder().toPath();
        try {
            writer = new BufferedWriter(new FileWriter(DataFactory.createFile(path, "bugfinder.log").toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the provided message at the provided level.
     *
     * @param level  The level of logging.
     * @param string The message.
     */
    public static void log(final Level level, final String string) {
        String message = String.format("[%s] %s", getPrefix(), string);
        try {
            Bukkit.getLogger().log(level, message);
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs an iterable of strings using the provided level.
     *
     * @param strings The messages.
     */
    public static void log(final Level level, final Iterable<String> strings) {
        strings.forEach(x -> ErrorLog.log(level, x));
    }

    /**
     * Logs an informational message using the provided message.
     *
     * @param string The message.
     */
    public static void info(final String string) {
        ErrorLog.log(Level.INFO, string);
    }

    /**
     * Logs a warning message using the provided message.
     *
     * @param string The message.
     */
    public static void warn(final String string) {
        ErrorLog.log(Level.WARNING, string);
    }

    /**
     * Closes the buffer. Used for server shutdown.
     */
    public static void closeBuffer() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets time/date prefix to prepend logged message.
     *
     * @return The prefix.
     */
    private static String getPrefix() {
        return DATE_FORMAT.format(LocalDateTime.now());
    }
}
