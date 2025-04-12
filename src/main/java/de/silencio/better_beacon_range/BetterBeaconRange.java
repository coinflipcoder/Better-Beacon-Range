package de.silencio.better_beacon_range;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterBeaconRange implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("better_beacon_range");
    private final BeaconConfig config = new BeaconConfig();

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing BetterBeaconRange");
        config.loadConfig();
    }
}
