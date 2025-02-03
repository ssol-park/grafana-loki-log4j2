package com.tjahzi.api.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Plugin(name = "SimpleNameConverter", category = PatternConverter.CATEGORY)
//@ConverterKeys({"simpleName"})
public class SimpleNameConverter extends LogEventPatternConverter {

    private static final Map<Class<?>, String> classNameCache = new ConcurrentHashMap<>();

    public SimpleNameConverter(String name, String style) {
        super(name, style);
    }

    public static SimpleNameConverter newInstance(String[] options) {
        return new SimpleNameConverter(SimpleNameConverter.class.getName(), SimpleNameConverter.class.getName());
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        if (event.getThrown() != null) {

            String simpleName = classNameCache.computeIfAbsent(
                    event.getThrown().getClass(), Class::getSimpleName
            );

            toAppendTo.append(simpleName);
        } else {
            toAppendTo.append("NoException");
        }
    }
}
