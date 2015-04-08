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

package org.tiogasolutions.couchace.jackson;

import org.tiogasolutions.couchace.core.api.injectable.MissingInjectableResponse;
import org.tiogasolutions.couchace.core.api.meta.CouchMetaRepository;
import org.tiogasolutions.couchace.jackson.internal.CouchJacksonModule;
import org.tiogasolutions.couchace.jackson.internal.CouchJacksonSerializerIntrospector;
import org.tiogasolutions.couchace.jackson.internal.CustomJacksonInjectableValues;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * User: harlan
 * Date: 7/21/12
 * Time: 11:15 PM
 */
public class CouchJacksonMapper extends ObjectMapper {
    private final CouchMetaRepository metaRepository;

    public CouchJacksonMapper(ObjectMapper objectMapper, CouchMetaRepository metaRepository) {
        super(objectMapper);
        this.metaRepository = metaRepository;
        init(new CustomJacksonInjectableValues(MissingInjectableResponse.RETURN_NULL), metaRepository, Collections.emptyList());
    }

    public CouchJacksonMapper(CouchMetaRepository metaRepository, Module... modules) {
        this.metaRepository = metaRepository;
        init(new CustomJacksonInjectableValues(MissingInjectableResponse.RETURN_NULL), metaRepository, Arrays.asList(modules));
    }

    public CouchJacksonMapper(CouchMetaRepository metaRepository, Collection<Module> modules) {
        this.metaRepository = metaRepository;
        init(new CustomJacksonInjectableValues(MissingInjectableResponse.RETURN_NULL), metaRepository, modules);
    }

    protected void init(InjectableValues injectableValues, CouchMetaRepository metaRepository, Collection<Module> modules) {
        // Stops LocalDateTime from being rendered as array.
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Pretty printing
        configure(SerializationFeature.INDENT_OUTPUT, true);

        // Fail on unknown
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register CouchJacksonModule
        registerModule(new CouchJacksonModule());

        // Register additional modules.
        modules.forEach(this::registerModule);

        // Configure CustomInjectableValues that will not fail on missing injectable values.
        setInjectableValues(injectableValues);

        // Annotation introspect used to exclude revisions and attachments
        CouchJacksonSerializerIntrospector serializerIntrospector = new CouchJacksonSerializerIntrospector(metaRepository);
        setAnnotationIntrospectors(serializerIntrospector, new JacksonAnnotationIntrospector());

    }

    public void noErrorOnUnknownFilter() {
        // Configure an empty FilterProvider so it will not complain when a filter is not found (such as Couch).
        this.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
    }

    @Override
    public ObjectMapper copy() {
        _checkInvalidCopy(CouchJacksonMapper.class);
        return new CouchJacksonMapper(this, metaRepository);
    }

}
