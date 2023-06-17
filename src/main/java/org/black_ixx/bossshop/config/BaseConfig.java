package org.black_ixx.bossshop.config;

import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.util.NamingSchemes;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

/**
 * Represents basic implementation of a config.
 *
 * @param <T> The config data type.
 */
public class BaseConfig<T extends ConfigData> implements Config<T> {
    private final transient ObjectMapper.Factory factory = ObjectMapper.factoryBuilder().defaultNamingScheme(NamingSchemes.LOWER_CASE_DASHED).build();
    private T data;
    private transient YamlConfigurationLoader loader;
    private transient CommentedConfigurationNode root;

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T load(final Path path, final TypeToken<T> type) {
        if (this.loader == null) {
            this.loader = this.createYamlLoader(path);
        }
        try {
            this.root = this.loader.load();
            this.data = this.factory.get(type).load(this.root);
            this.save();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        this.save(this.root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final CommentedConfigurationNode root) {
        try {
            this.loader.save(this.root);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the config loader.
     *
     * @return The loader.
     */
    private YamlConfigurationLoader createYamlLoader(final Path path) {
        return YamlConfigurationLoader.builder()
                .defaultOptions(x -> x.serializers(ConfigurateComponentSerializer.configurate().serializers()))
                .path(path)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
    }
}
