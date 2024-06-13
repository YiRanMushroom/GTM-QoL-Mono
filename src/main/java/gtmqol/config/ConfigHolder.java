package gtmqol.config;

import dev.toma.configuration.config.Configurable;
import gtmqol.GTMQoL;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTMQoL.MODID)
public class ConfigHolder {
    public static ConfigHolder INSTANCE;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(ConfigHolder.class, ConfigFormats.yaml())
                    .getConfigInstance();
        }
    }

    @Configurable
    @Configurable.Comment({"Is Greenhouse enabled?", "Default: true"})
    public boolean greenhouseEnabled = true;
}
