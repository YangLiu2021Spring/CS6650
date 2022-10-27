package io.swagger.api;

import io.swagger.manager.SkierMessageQueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class ConfigurationController {
    private final SkierMessageQueueManager skierMessageQueueConnectionManager;

    @Autowired
    public ConfigurationController(SkierMessageQueueManager skierMessageQueueConnectionManager) {
        this.skierMessageQueueConnectionManager = skierMessageQueueConnectionManager;
    }

    @RequestMapping(
        value = "/dump-configuration",
        produces = { MediaType.APPLICATION_JSON_VALUE },
        method = RequestMethod.GET
    )
    public Map dumpConfiguration() {
        Map config = new HashMap(1);
        config.put("rmqHost", this.getRmqHost());

        return config;
    }

    @RequestMapping(
            value = "/set-configuration",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET
    )
    public void setConfiguration(@RequestParam String rmqHostIP) {
        if (!StringUtils.isEmpty(rmqHostIP)) {
            skierMessageQueueConnectionManager.setCurrentHost(rmqHostIP);
        }
    }

    private String getRmqHost() {
        String host = skierMessageQueueConnectionManager.getConnectedHost();

        return Objects.isNull(host) ? "NOT_INITIALIZED" : host;
    }
}
