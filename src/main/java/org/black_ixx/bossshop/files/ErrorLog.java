package org.black_ixx.bossshop.files;

import org.black_ixx.bossshop.config.DataFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple text file writer to potentially replace BugFinder.yml. May be removed in the future.
 */
public final class ErrorLog {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss");
    private final BufferedWriter writer;

    /**
     * Constructs the object.
     *
     * @param pluginPath The plugin's path.
     */
    public ErrorLog(final Path pluginPath) throws IOException {
        Path path = DataFactory.createFile(pluginPath, "errors.txt");
        this.writer = new BufferedWriter(new FileWriter(path.toFile()));
    }

    public void write(final String string) {
        String message = String.format("[%s] %s", this.getPrefix(), string);
        try {
            this.writer.write(message);
            this.writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(final Iterable<String> strings) {
        strings.forEach(this::write);
    }

    /**
     * Closes the buffer. Used for server shutdown.
     */
    public void closeBuffer() {
        try {
            this.writer.flush();
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets time/date prefix to prepend logged message.
     *
     * @return The prefix.
     */
    private String getPrefix() {
        return DATE_FORMAT.format(LocalDateTime.now());
    }
}
