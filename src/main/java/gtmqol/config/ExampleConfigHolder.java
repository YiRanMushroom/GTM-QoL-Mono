package gtmqol.config;

import gtmqol.GTMQoL;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GTMQoL.MODID)
public class ExampleConfigHolder {
    public static ExampleConfigHolder INSTANCE;

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(ExampleConfigHolder.class, ConfigFormats.yaml())
                    .getConfigInstance();
        }
    }
}
