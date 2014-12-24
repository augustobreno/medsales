package com.sales.medsales.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Responsável para criação um Logger para injeção via CDI.
 */
public class LoggerProducer {

    private Class<?> getInjectionClass(InjectionPoint ip) {
        return ip.getMember().getDeclaringClass();
    }

    @Produces
    public Logger createLogger(InjectionPoint caller) {
        return Logger.getLogger(getInjectionClass(caller).getSimpleName());
    }
}
