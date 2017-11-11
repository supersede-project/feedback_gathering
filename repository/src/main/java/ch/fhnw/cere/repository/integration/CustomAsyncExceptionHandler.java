package ch.fhnw.cere.repository.integration;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;


public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {

        System.err.println("Exception message - " + throwable.getMessage());
        System.err.println("Method name - " + method.getName());
        for (Object param : obj) {
            System.err.println("Parameter value - " + param);
        }
    }
}