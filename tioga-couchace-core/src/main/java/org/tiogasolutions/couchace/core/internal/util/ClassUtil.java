/*
 * Copyright 2012 Harlan Noonkester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tiogasolutions.couchace.core.internal.util;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * User: harlan
 * Date: 2/9/14
 * Time: 2:48 AM
 */
public class ClassUtil {

    public static Field findField(Class<?> type, String fieldName) {
        if (type == null || fieldName == null) {
            return null;
        }
        while (type != null && type != Object.class) {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    public static List<Field> listAllFields(Class<?> type) {
        ArrayList<Field> fieldList = new ArrayList<>();
        while (type != null && type != Object.class) {
            Field[] fields = type.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            type = type.getSuperclass();
        }
        return fieldList;
    }

    public static <T extends Annotation> T findClassAnnotation(Class<?> classType, Class<T> annotationType) {
        for (Class<?> type : listAllClassesAndInterfaces(classType)) {
            T annotation = type.getAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    public static List<Class<?>> listAllClassesAndInterfaces(Class<?> type) {
        List<Class<?>> classList = new ArrayList<>();
        while (type != null && type != Object.class) {
            classList.add(type);
            Collections.addAll(classList, type.getInterfaces());
            type = type.getSuperclass();
        }
        return classList;
    }

    public static Method findSetterMethod(Class<?> type, String propertyName, Class<?> argType) {
        String setterName = "set"
            + Character.toUpperCase(propertyName.charAt(0))
            + propertyName.substring(1);
        while (type != null && type != Object.class) {
            Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (isSetter(setterName, argType, method)) {

                    // A match if name is a match, return type is null and arg is a match.
                    return method;
                }
            }

            for (Class<?> interfaceType : type.getInterfaces()) {
                methods = interfaceType.getDeclaredMethods();
                for (Method method : methods) {
                    if (isSetter(setterName, argType, method)) {

                        // A match if name is a match, return type is null and arg is a match.
                        return method;
                    }
                }
            }

            type = type.getSuperclass();
        }
        return null;
    }

    public static List<Method> listGetterMethods(Class<?> type) {
        ArrayList<Method> methodList = new ArrayList<>();
        while (type != null && type != Object.class) {
            Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (isGetter(method)) {
                    // Add any method which has a return type, takes no arguments and starts with get.
                    methodList.add(method);
                }
            }

            for (Class<?> interfaceType : type.getInterfaces()) {
                methods = interfaceType.getDeclaredMethods();
                for (Method method : methods) {
                    if (isGetter(method)) {
                        // Add any method which has a return type, takes no arguments and starts with get.
                        methodList.add(method);
                    }
                }
            }

            type = type.getSuperclass();
        }

        return methodList;
    }

    public static boolean isGetter(Method method) {
        return (method.getReturnType() != null &&
            method.getParameterTypes().length == 0 &&
            method.getName().startsWith("get") &&
            method.getName().length() > 3);
    }

    public static boolean isSetter(String setterName, Class<?> argType, Method method) {
        return (method.getName().equals(setterName) &&
            method.getReturnType() == null &&
            method.getParameterTypes().length == 1 &&
            method.getParameterTypes()[0].isAssignableFrom(argType));
    }

    public static Map<String, Method> toPropertyMethodMap(Collection<Method> methods) {
        Map<String, Method> methodMap = new HashMap<>();
        for (Method method : methods) {
            String propertyName = Introspector.decapitalize(method.getName().substring(3));
            methodMap.put(propertyName, method);
        }
        return methodMap;
    }

}
