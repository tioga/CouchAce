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

package org.tiogasolutions.couchace.core.api.meta;

import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.internal.util.ClassUtil;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ValueAccessor {
    private final String propertyName;
    private final Method readMethod;
    private final Method writeMethod;
    private final Field field;

    public ValueAccessor(Class<?> type, Method readMethod) {
        this.readMethod = readMethod;
        this.propertyName = Introspector.decapitalize(readMethod.getName().substring(3));
        this.writeMethod = ClassUtil.findSetterMethod(type, propertyName, readMethod.getReturnType());
        if (writeMethod == null) {
            field = ClassUtil.findField(type, this.propertyName);
            if (field != null) {
                field.setAccessible(true);
            }
        } else {
            field = null;
        }
    }

    public ValueAccessor(Field field) {
        this.readMethod = null;
        this.propertyName = field.getName();
        this.writeMethod = null;
        this.field = field;
        this.field.setAccessible(true);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object readValue(Object object) {

        try {
            if (readMethod != null) {
                return readMethod.invoke(object);
            } else if (field != null) {
                return field.get(object);
            } else {
                throw CouchException.internalServerError("Cannot read value from " + propertyName + ", did not find a corresponding field or getter.");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw CouchException.internalServerError("Error reading value from " + propertyName + ", likely an issue with CouchAce id, revision or attachment annotations", e);
        }
    }

    public void writeValue(Object object, Object value) {
        try {
            if (writeMethod != null) {
                writeMethod.invoke(object, value);
            } else if (field != null) {
                field.set(object, value);
            } else {
                throw CouchException.internalServerError("Cannot assign value to " + propertyName + ", did not find a corresponding field or setter.");
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw CouchException.internalServerError("Error reading value from " + propertyName + ", likely an issue with CouchAce id, revision or attachment annotations", e);
        }
    }

    public boolean isWritable() {
        return writeMethod != null || field != null;
    }

    public boolean isReadable() {
        return readMethod != null || field != null;
    }


}
