package config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;


@Plugin(name = "LokiFilter", category = "Core", elementType = Filter.ELEMENT_TYPE)
public class LokiFilter extends AbstractFilter {

    private final Level level;

    protected LokiFilter(Level level) {
        this.level = level;
    }

    @Override
    public Result filter(LogEvent event) {
        if (event.getLevel().isMoreSpecificThan(level)) {
            return Result.ACCEPT;
        }

        return Result.DENY;
    }

    @PluginFactory
    public static LokiFilter createFilter(
            @PluginAttribute(value = "level", defaultString = "ERROR") String level) {
        Level logLevel = Level.toLevel(level, Level.ERROR);
        return new LokiFilter(logLevel);
    }
}
