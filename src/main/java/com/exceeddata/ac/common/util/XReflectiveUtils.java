package com.exceeddata.ac.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.exceeddata.ac.common.exception.EngineException;

public final class XReflectiveUtils {
    private XReflectiveUtils() {}
    
    public static Object invokeInstanceMethod(
            final String className, 
            final String methodName, 
            final Object[] args,
            final Class<?>[] argClasses) 
                    throws EngineException, NoClassDefFoundError, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        try {
            final Class<?> clazz = Class.forName(className);
            final Object obj = clazz.newInstance();
            final Method method = clazz.getMethod(methodName, argClasses);
            return method.invoke(obj, args);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
            throw new EngineException(e.getMessage(), e);
        }
    }
    
    public static Object invokeInstanceMethodNoArgs(
            final String className, 
            final String methodName) 
                    throws EngineException, NoClassDefFoundError, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        try {
            final Class<?> clazz = Class.forName(className);
            final Object obj = clazz.newInstance();
            final Method method = clazz.getMethod(methodName);
            return method.invoke(obj, new Object[0]);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
            throw new EngineException(e.getMessage(), e);
        }
    }
    
    public static Method createStaticMethod(
            final String className, 
            final String methodName,
            final Class<?>[] argClasses) 
                    throws EngineException, NoClassDefFoundError, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        try {
            final Class<?> clazz = Class.forName(className);
            return clazz.getMethod(methodName, argClasses);
        } catch (SecurityException | IllegalArgumentException e) {
            throw new EngineException(e.getMessage(), e);
        }
    }
    
    public static Object invokeStaticMethod(
            final String className, 
            final String methodName, 
            final Object[] args,
            final Class<?>[] argClasses) 
                    throws EngineException, NoClassDefFoundError, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        try {
            final Class<?> clazz = Class.forName(className);
            final Method method = clazz.getMethod(methodName, argClasses);
            return method.invoke(null, args);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            throw new EngineException(e.getMessage(), e);
        }
    }
    
    public static Object invokeStaticMethodNoArgs(
            final String className, 
            final String methodName) 
                    throws EngineException, NoClassDefFoundError, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        try {
            final Class<?> clazz = Class.forName(className);
            final Method method = clazz.getMethod(methodName);
            return method.invoke(null);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            throw new EngineException(e.getMessage(), e);
        }
    }
}
