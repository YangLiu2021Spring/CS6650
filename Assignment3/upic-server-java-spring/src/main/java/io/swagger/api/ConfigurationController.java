package io.swagger.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.manager.SkierMessageQueueChannelManagerFactory;
import io.swagger.model.SkierMessageQueueChannelConfig;
import io.swagger.model.SkierMessageQueueConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigurationController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationController.class);
    private static final Gson GSON = new GsonBuilder().create();
    private static String configVersion = null;
    private static String lastConfiguredTime = null;
    private final SkierMessageQueueChannelManagerFactory skierMessageQueueChannelManagerFactory;

    @Autowired
    public ConfigurationController(SkierMessageQueueChannelManagerFactory skierMessageQueueChannelManagerFactory) {
        this.skierMessageQueueChannelManagerFactory = skierMessageQueueChannelManagerFactory;
    }

    @RequestMapping(
        value = "/dump-config",
        produces = { MediaType.APPLICATION_JSON_VALUE },
        method = RequestMethod.GET
    )
    public Map dumpConfiguration() {
        return this.buildConfig();
    }

    @RequestMapping(
            value = "/set-config",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET
    )
    public Map setConfiguration(
            @RequestParam(required = false) String version,
            @RequestParam(required = false) String rmqConnectionConfig,
            @RequestParam(required = false) String rmqChannelConfig
    ) {
        // log the input
        LOG.info(String.format("Get input args with version: %s, rmqConnectionConfig: %s, rmqChannelConfig: %s",
                version, rmqConnectionConfig, rmqChannelConfig));

        // skip configuration settings since version is null
        if (StringUtils.isEmpty(version)) {
            return this.buildConfig();
        }

        // skip configuration settings since version is not changed
        if (version.equals(configVersion)) {
            return this.buildConfig();
        }

        // re-config RMQ
        configVersion = version;
        skierMessageQueueChannelManagerFactory.destroy();
        SkierMessageQueueConnectionConfig connectionConfig =
                GSON.fromJson(rmqConnectionConfig, SkierMessageQueueConnectionConfig.class);
        SkierMessageQueueChannelConfig channelConfig =
                GSON.fromJson(rmqChannelConfig, SkierMessageQueueChannelConfig.class);
        skierMessageQueueChannelManagerFactory.init(connectionConfig, channelConfig);
        lastConfiguredTime = Instant.now().toString();

        // return the last config
        return this.buildConfig();
    }

    private Map buildConfig() {
        Map config = new HashMap(5);
        config.put("version", configVersion);
        config.put("rmqConnectionConfig", skierMessageQueueChannelManagerFactory.getConnectionConfig());
        config.put("rmqChannelConfig", skierMessageQueueChannelManagerFactory.getChannelConfig());
        config.put("lastConfiguredTime", lastConfiguredTime);
        config.put("currentTime", Instant.now().toString());

        return config;
    }
}
