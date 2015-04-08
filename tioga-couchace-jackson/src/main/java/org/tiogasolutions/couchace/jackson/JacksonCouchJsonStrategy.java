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

import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.CouchDatabaseInfo;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.injectable.CouchInjectables;
import org.tiogasolutions.couchace.core.api.injectable.MissingInjectableResponse;
import org.tiogasolutions.couchace.core.api.json.CouchJsonException;
import org.tiogasolutions.couchace.core.api.meta.CouchEmbeddedAttachmentMeta;
import org.tiogasolutions.couchace.core.api.meta.CouchEntityMeta;
import org.tiogasolutions.couchace.core.api.query.CouchJsonKey;
import org.tiogasolutions.couchace.core.api.request.GetRequestFactory;
import org.tiogasolutions.couchace.core.api.request.PostEntityRequest;
import org.tiogasolutions.couchace.core.api.request.PutEntityRequest;
import org.tiogasolutions.couchace.core.api.response.EntityDocument;
import org.tiogasolutions.couchace.core.api.response.GetAttachmentResponse;
import org.tiogasolutions.couchace.core.api.response.TextDocument;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;
import org.tiogasolutions.couchace.core.spi.json.CouchJsonStrategy;
import org.tiogasolutions.couchace.jackson.internal.CustomJacksonInjectableValues;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: harlan
 * Date: 2/1/14
 * Time: 4:37 PM
 */
public class JacksonCouchJsonStrategy implements CouchJsonStrategy {
    private static final Logger log = LoggerFactory.getLogger(JacksonCouchJsonStrategy.class);

    private ObjectMapper objectMapper;
    private CouchInjectables couchInjectables;
    private List<Module> moduleList = new ArrayList<>();
    private MissingInjectableResponse missingInjectableResponse = MissingInjectableResponse.THROW_EXCEPTION;

    public JacksonCouchJsonStrategy() {
    }

    public JacksonCouchJsonStrategy(Module... modules) {
        if (modules != null) {
            moduleList.addAll(Arrays.asList(modules));
        }
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    protected void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CouchInjectables getCouchInjectables() {
        return couchInjectables;
    }

    public void setCouchInjectables(CouchInjectables couchInjectables) {
        this.couchInjectables = couchInjectables;
    }

    public MissingInjectableResponse getMissingInjectableResponse() {
        return missingInjectableResponse;
    }

    public void setMissingInjectableResponse(MissingInjectableResponse missingInjectableResponse) {
        this.missingInjectableResponse = (missingInjectableResponse != null) ? missingInjectableResponse : MissingInjectableResponse.THROW_EXCEPTION;
    }

    /**
     * Required by CouchJsonStrategy
     *
     * @param couchSetup -
     */
    @Override
    public void init(CouchSetup couchSetup) {
        CouchJacksonMapper couchJacksonMapper = new CouchJacksonMapper(couchSetup.getMetaRepository(), moduleList);
        setObjectMapper(couchJacksonMapper);
        if (this.couchInjectables == null) {
            setCouchInjectables(couchSetup.getInjectables());
        }
        setMissingInjectableResponse(couchSetup.getMissingInjectableResponse());
    }

    @Override
    public CouchDatabaseInfo readDatabaseInfo(String json) {
        try {
            return objectMapper.readValue(json, CouchDatabaseInfo.class);
        } catch (IOException e) {
            throw new CouchJsonException(e);
        }
    }

    @Override
    public String createJsonForPut(PutEntityRequest request) {
        return createJsonForWrite(
                request.getDocumentId(),
                request.getDocumentRevision(),
                request.getEntityType(),
                request.getEntity(),
                request.getAttachmentInfoMap());
    }

    @Override
    public String createJsonForPost(PostEntityRequest request) {
        return createJsonForWrite(null, null, request.getEntityType(), request.getEntity(), null);
    }

    @Override
    public TextDocument readTextDocument(String json) throws CouchJsonException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        try {
            try (JsonParser parser = jsonFactory.createParser(json)) {
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("Expected response JSON to start with an Object: " + parser.getCurrentToken());
                }

                String documentId = null;
                String revision = null;
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String name = parser.getCurrentName();
                    parser.nextToken();
                    if ("_id".equals(name)) {
                        documentId = parser.getText();
                    } else if ("_rev".equals(name)) {
                        revision = parser.getText();
                    }

                    if (documentId != null && revision != null) {
                        // We have what we needed, stop parsing.
                        break;
                    }
                }

                return new TextDocument(documentId, revision, null, json);
            }
        } catch (IOException e) {
            throw new CouchJsonException(e);
        }
    }

    @Override
    public List<TextDocument> readTextDocuments(String json) throws CouchJsonException {
        return readTextDocumentsUsingTree(json);
    }

    protected List<TextDocument> readTextDocumentsUsingTree(String json) throws CouchJsonException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        try {
            List<TextDocument> documentList;
            try (JsonParser parser = jsonFactory.createParser(json)) {
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("Expected response JSON to start with an Object: " + parser.getCurrentToken());
                }
                documentList = new ArrayList<>();
                while (parser.nextToken() != null) {
                    String name = parser.getCurrentName();
                    if ("rows".equals(name)) {
                        parser.nextToken();

                        ArrayNode rowsNode = objectMapper.readTree(parser);

                        for (int i = 0; i < rowsNode.size(); i++) {
                            JsonNode documentNode = rowsNode.get(i);

                            // Id
                            JsonNode idNode = documentNode.get("id");
                            String id = (idNode != null) ? idNode.asText() : null;

                            // Key
                            JsonNode keyNode = documentNode.get("key");
                            CouchJsonKey jsonKey = (keyNode != null) ? new CouchJsonKey(keyNode.asText()) : new CouchJsonKey();

                            // Content may come from value or doc
                            JsonNode contentNode = documentNode.get("doc");
                            if (contentNode == null) {
                                contentNode = documentNode.get("value");
                            }

                            // TODO - review this
                            // If we have a content node then get string content and revision
                            String content = null;
                            String revision = null;
                            if (contentNode != null) {
                                // Use toString() on the node to the the actual JSON document (this came from the Jackson developer)
                                content = contentNode.toString();

                                // Get the revision from within the content (may not always be there).
                                JsonNode revNode = contentNode.get("_rev");
                                if (revNode == null) {
                                    revNode = contentNode.get("rev");
                                }
                                revision = (revNode != null) ? revNode.asText() : null;
                            }

                            // Create the document
                            documentList.add(new TextDocument(
                                    id,
                                    revision,
                                    jsonKey,
                                    content
                            ));
                        }
                    }
                }
            }
            return documentList;
        } catch (IOException ex) {
            throw new CouchJsonException(ex);
        }
    }

    @Override
    public <T> EntityDocument<T> readEntityDocument(GetRequestFactory getRequestFactory, CouchEntityMeta<T> entityMeta, String json) throws CouchJsonException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        try {
            try (JsonParser parser = jsonFactory.createParser(json)) {
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("Expected response JSON to start with an Object: " + parser.getCurrentToken());
                }

                return finalizeEntityDoc(getRequestFactory, entityMeta, parser, null, json);
            }
        } catch (IOException e) {
            throw new CouchJsonException(e);
        }
    }

    @Override
    public <T> List<EntityDocument<T>> readEntityDocuments(GetRequestFactory getRequestFactory, CouchEntityMeta<T> entityMeta, String json) throws CouchJsonException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        try {
            List<EntityDocument<T>> entityDocumentList;
            try (JsonParser parser = jsonFactory.createParser(json)) {
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("Expected response JSON to start with an Object: " + parser.getCurrentToken());
                }
                entityDocumentList = new ArrayList<>();
                while (parser.nextToken() != null) {
                    String name = parser.getCurrentName();
                    parser.nextToken();
                    if ("rows".equals(name)) {
                        CouchJsonKey key = null;
                        while (parser.hasCurrentToken()) {
                            name = parser.getCurrentName();
                            if ("key".equals(name)) {
                                JsonToken token = parser.getCurrentToken();
                                if (token.isNumeric()) {
                                    key = new CouchJsonKey(parser.getNumberValue());
                                } else {
                                    key = new CouchJsonKey(parser.getText());
                                }
                            } else if ("value".equals(name)) {
                                // At this point we pass processing to the finalize methods - in many cases they will be parsing "doc" node
                                parser.nextToken();

                                // Parsing an entity
                                EntityDocument<T> entityDocument = finalizeEntityDoc(getRequestFactory, entityMeta, parser, key, json);
                                entityDocumentList.add(entityDocument);

                                // Since we have parsed the value now is the time to clear out the key.
                                key = null;
                            }
                            parser.nextToken();
                        }
                    }
                }
            }
            return entityDocumentList;
        } catch (IOException ex) {
            throw new CouchJsonException(ex);
        }
    }

    protected <T> EntityDocument<T> finalizeEntityDoc(GetRequestFactory getRequestFactory,
                                                      CouchEntityMeta<T> entityMeta,
                                                      JsonParser parser,
                                                      CouchJsonKey key,
                                                      String json) throws IOException {
        Class<T> entityClass = entityMeta.getEntityClass();
        String id = null;
        String rev = null;
        String entityType = null;
        CouchAttachmentInfoMap attachmentInfoMap = null;
        T entity = null;
        while (parser.nextToken() != JsonToken.END_OBJECT && parser.getCurrentToken() != null) {
            String name = parser.getCurrentName();
            parser.nextToken();
            if ("_id".equals(name)) {
                id = parser.getText();
            } else if ("_rev".equals(name)) {
                rev = parser.getText();
            } else if ("_attachments".equals(name)) {
                attachmentInfoMap = parser.readValueAs(CouchAttachmentInfoMap.class);
            } else if ("entityType".equals(name)) {
                entityType = parser.getText();
            } else if ("entity".equals(name) && entityClass != String.class) {

                if (id == null) {
                    throw CouchJsonException.internalServerError("Error finalizing Entity from JSON, found entity but _id was never found.");
                }
                if (rev == null) {
                    throw CouchJsonException.internalServerError("Error finalizing Entity from JSON, found entity but _rev was never found.");
                }

                // Create couchInjectables, which may include attachments.
                CustomJacksonInjectableValues inject = buildInjectableValues(getRequestFactory, entityMeta, id, rev);

                // Instantiate the entity using mapper and injectable values
                entity = objectMapper.reader(entityClass).with(inject).readValue(parser);

            }
        }

        // We are done reading, should have entity, id, rev and attachments so finalize
        if (entity != null) {
            // If we have an id and it's not set (may have been set via injectable)
            if (entityMeta.hasId() && !entityMeta.isIdSet(entity)) {
                entityMeta.writeDocumentId(entity, id);
            }

            // If we have a revision and it's not set (may have been set via injectable)
            if (entityMeta.hasRevision() && !entityMeta.isRevisionSet(entity)) {
                entityMeta.writeDocumentRevision(entity, rev);
            }

            // If entity has attachmentMeta
            if (entityMeta.hasAttachmentsMeta()) {
                entityMeta.writeAttachmentInfoMap(entity, attachmentInfoMap);
            }

        }

        if (entity == null && entityClass == String.class) {
            // With String entity we just use json as the entity
            entity = entityClass.cast(json);
        }

        return new EntityDocument<>(id, rev, key, entityType, entity);
    }

    protected String createJsonForWrite(String documentId, String revision, String entityType, Object entity, CouchAttachmentInfoMap attachmentInfoMap) {
        ArgUtil.assertNotEmpty(entityType, "entityType");
        ArgUtil.assertNotNull(entity, "entity");

        // TODO - is StringWriter the best choice here
        JsonFactory jsonFactory = objectMapper.getFactory();
        try {
            StringWriter writer = new StringWriter();
            try (JsonGenerator generator = jsonFactory.createGenerator(writer)) {
                generator.writeStartObject();
                if (documentId != null) {
                    generator.writeStringField("_id", documentId);
                }
                if (revision != null) {
                    generator.writeStringField("_rev", revision);
                }
                generator.writeStringField("entityType", entityType);
                generator.writeObjectField("entity", entity);

                // Attachment meta map.
                if (attachmentInfoMap != null) {
                    generator.writeObjectField("_attachments", attachmentInfoMap);
                }

                generator.writeEndObject();
                generator.flush();
                String json = writer.toString();

                if (log.isDebugEnabled()) {
                    log.debug("Put JSON: " + json);
                }

                return json;
            }
        } catch (IOException e) {
            throw new CouchJsonException(e);
        }
    }

    protected CustomJacksonInjectableValues buildInjectableValues(GetRequestFactory getRequestFactory, CouchEntityMeta<?> entityMeta, String documentId, String revision) {
        CustomJacksonInjectableValues inject = new CustomJacksonInjectableValues(missingInjectableResponse, couchInjectables);

        // Add id if it's defined
        if (entityMeta.hasId() && documentId != null) {
            inject.addValue(entityMeta.getIdName(), documentId);
        }

        // Add revision if it's defined
        if (entityMeta.hasRevision() && revision != null) {
            inject.addValue(entityMeta.getRevisionName(), revision);
        }

        // Add any attachments
        if (entityMeta.hasEmbeddedAttachments()) {
            for (CouchEmbeddedAttachmentMeta attachmentDef : entityMeta.getEmbeddedAttachmentMetaList()) {
                GetAttachmentResponse response = getRequestFactory.attachment(documentId, revision, attachmentDef.getAttachmentName()).execute();
                inject.addValue(attachmentDef.getAttachmentName(), response.getContent());
            }
        }
        return inject;
    }
}
