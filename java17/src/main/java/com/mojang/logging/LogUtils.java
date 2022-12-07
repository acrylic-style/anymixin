package com.mojang.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.function.Supplier;

public class LogUtils {
    public static final String FATAL_MARKER_ID = "FATAL";

    public static final Marker FATAL_MARKER = MarkerFactory.getMarker(FATAL_MARKER_ID);

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static boolean isLoggerActive() {
        LoggerContext loggerContext = LogManager.getContext();
        if (loggerContext instanceof LifeCycle lifeCycle) {
            return !lifeCycle.isStopped();
        }
        return true;
    }

    public static void configureRootLoggingLevel(org.slf4j.event.Level level) {
        org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig("");
        loggerConfig.setLevel(convertLevel(level));
        ctx.updateLoggers();
    }

    private static Level convertLevel(org.slf4j.event.Level level) {
        switch (level) {
            default:
                throw new IncompatibleClassChangeError();
            case INFO:

            case WARN:

            case DEBUG:

            case ERROR:

            case TRACE:
                break;
        }
        return

                Level.TRACE;
    }

    public static Object defer(final Supplier<Object> result) {
        class ToString {
            public String toString() {
                return result.get().toString();
            }
        };
        return new ToString();
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger(STACK_WALKER.getCallerClass());
    }

    public static Logger getClassLogger() {
        return LoggerFactory.getLogger(STACK_WALKER.getCallerClass().getSimpleName());
    }
}
