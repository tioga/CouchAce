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

package org.tiogasolutions.couchace.jersey;

import org.tiogasolutions.couchace.core.api.CouchException;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.core.api.http.*;
import org.tiogasolutions.couchace.core.api.response.CouchDocument;
import org.tiogasolutions.couchace.core.internal.util.ArgUtil;
import org.tiogasolutions.couchace.core.internal.util.UriUtil;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.uri.UriComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiogasolutions.couchace.core.spi.http.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: harlan
 * Date: 2/3/14
 * Time: 10:31 PM
 */
public class JerseyCouchHttpClient implements CouchHttpClient {
    private static final Logger log = LoggerFactory.getLogger(JerseyCouchHttpClient.class);
    private Client client;
    private String baseUrl;

    public JerseyCouchHttpClient() {
    }

    @Override
    public void init(CouchSetup couchSetup) {
        try {
            // Build the client
            ClientBuilder clientBuilder = ClientBuilder.newBuilder();
            if (couchSetup.getSslSetup() != null) {
                // Using SSL set assign context.
                clientBuilder.sslContext(couchSetup.getSslSetup().getSSLContext());
            }
            client = clientBuilder.build();

            // If we have authentication set the auth filter.
            if (couchSetup.getUserName() != null) {
                HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(couchSetup.getUserName(), couchSetup.getPassword());
                client.register(feature);
            }

            // Set baseUrl.
            this.baseUrl = couchSetup.getUrl();
        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public CouchHttpResponse createDatabase(String databaseName) {
        try {
            // Jersey does not allow for null entity on put.
            Entity entity = Entity.entity("", MediaType.WILDCARD_TYPE);

            WebTarget webTarget = newWebTarget(databaseName);
            Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .put(entity);

            CouchHttpStatus statusCode = CouchHttpStatus.findByCode(response.getStatus());
            String eTag = getETag(response, null);

            byte[] content = response.readEntity(byte[].class);
            CouchMediaType mediaType = CouchMediaType.fromString(response.getMediaType().toString());
            int contentLength = response.getLength();

            return CouchHttpResponse.builder(CouchMethodType.PUT, webTarget.getUri(), statusCode)
                .setEtag(eTag)
                .setContent(mediaType, contentLength, content)
                .build();

        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public CouchHttpResponse deleteDatabase(String databaseName) {
        try {
            WebTarget webTarget = newWebTarget(databaseName);
            Response response = webTarget.request().delete();

            CouchHttpStatus statusCode = CouchHttpStatus.findByCode(response.getStatus());
            String eTag = getETag(response, null);

            byte[] content = response.readEntity(byte[].class);
            CouchMediaType mediaType = CouchMediaType.fromString(response.getMediaType().toString());
            int contentLength = response.getLength();

            return CouchHttpResponse.builder(CouchMethodType.DELETE, webTarget.getUri(), statusCode)
                .setEtag(eTag)
                .setContent(mediaType, contentLength, content)
                .build();

        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public CouchHttpResponse head(HttpHeadRequest request) {
        try {
            WebTarget webTarget = newWebTarget(request);
            Response headResponse = webTarget.request().head();

            URI uri = webTarget.getUri();
            if (log.isDebugEnabled()) {
                log.debug("Head url: " + uri.toString());
            }

            return buildCouchResponse(request, uri, headResponse, request.getDocumentId());
        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }

    }

    @Override
    public CouchHttpResponse get(HttpGetRequest request) {

        try {
            WebTarget webTarget = newWebTarget(request);
            MediaType acceptType = (request.getAcceptType() != null) ? MediaType.valueOf(request.getAcceptType().getMediaString()) : null;
            Response getResponse = webTarget.request(acceptType).get();

            URI uri = webTarget.getUri();
            if (log.isDebugEnabled()) {
                log.debug("Get url: " + uri.toString());
            }

            return buildCouchResponse(request, uri, getResponse, request.getDocumentId());
        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }

    }

    @Override
    public CouchHttpResponse put(HttpPutRequest request) {
        try {
            MediaType contentType = MediaType.valueOf(request.getContentType().getMediaString());

            // Jersey does not allow entity value to be null.
            Object content = request.getContent() != null ? request.getContent() : "";
            Entity entity = Entity.entity(content, contentType);
            WebTarget webTarget = newWebTarget(request.getPath());
            for (CouchHttpQueryParam queryParam : request.getHttpQuery()) {
                webTarget = webTarget.queryParam(queryParam.getName(), queryParam.getValue());
            }

            URI uri = webTarget.getUri();
            if (log.isDebugEnabled()) {
                log.debug("Put url: " + uri.toString());
            }

            Response putResponse = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Content-Type", contentType)
                .put(entity);

            return buildCouchResponse(request, uri, putResponse, request.getDocumentId());
        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public CouchHttpResponse post(HttpPostRequest request) {

        try {
            // Jersey does not allow entity value to be null.
            Entity entity = Entity.json(request.getContent());
            WebTarget webTarget = newWebTarget(request.getPath());
            for (CouchHttpQueryParam queryParam : request.getHttpQuery()) {
                webTarget = webTarget.queryParam(queryParam.getName(), queryParam.getValue());
            }

            Response postResponse = webTarget
              .request(MediaType.APPLICATION_JSON_TYPE)
              .post(entity);

            URI uri = webTarget.getUri();
            if (log.isDebugEnabled()) {
                log.debug("POST url: " + uri.toString());
            }

            String documentId = UriUtil.lastPathElement(postResponse.getLocation());

            // Build our response.
            return buildCouchResponse(request, uri, postResponse, documentId);

        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public CouchHttpResponse delete(HttpDeleteRequest request) {
        try {
            // To avoid deleting database with this call we ensure path is not empty.
            if (ArgUtil.isEmpty(request.getPath())) {
                throw CouchException.badRequest("Attempting to delete with empty path, which would delete the database so we will fail. If you really want to delete the database use the explicit deleteDatabase method.");
            }

            WebTarget webTarget = newWebTarget(request.getPath());
            for (CouchHttpQueryParam queryParam : request.getHttpQuery()) {
                webTarget = webTarget.queryParam(queryParam.getName(), queryParam.getValue());
            }
            Response deleteResponse = webTarget.request().delete();

            URI uri = webTarget.getUri();
            if (log.isDebugEnabled()) {
                log.debug("DELETE url: " + uri.toString());
            }

            // Build our response.
            return buildCouchResponse(request, uri, deleteResponse, request.getDocumentId());
        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public URI pageUri(URI currentPageUri, CouchDocument document, boolean reverse) {

        try {
            // Get uriPath starting from _design
            String uriPath = currentPageUri.getPath();
            int designIndex = uriPath.indexOf("_design");
            if (designIndex > 0) {
                uriPath = uriPath.substring(designIndex);
            }

            // Create uriBuilder from path.
            UriBuilder uriBuilder = UriBuilder.fromPath(uriPath)
                .queryParam("startkey", document.getKey().getJsonValue())
                .queryParam("startkey_docid", document.getDocumentId())
                .queryParam("skip", 1)
                .queryParam("descending", reverse);

            // Add any key end key.
            MultivaluedMap<String, String> query = UriComponent.decodeQuery(currentPageUri, true);
            if (query.containsKey("endkey")) {
                uriBuilder.queryParam("endkey", query.getFirst("endkey"));
            }
            if (query.containsKey("key")) {
                uriBuilder.queryParam("key", query.getFirst("key"));
            }

            // Return uri path.
            return uriBuilder.build();
        } catch (Throwable ex) {
            throw CouchHttpException.internalServerError(ex);
        }
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public URI uri(String... paths) {
        UriBuilder uriBuilder = UriBuilder.fromPath(baseUrl);
        if (paths != null) {
            for (String path : paths) {
                uriBuilder.path(path);
            }
        }
        return uriBuilder.build();
    }

    /**
     * Create WebTarget always relative to baseUrl.
     *
     * @param url -
     * @return WebTarget
     */
    protected WebTarget newWebTarget(String url) {
        url = (url == null) ? "" : url;

        if (baseUrl.endsWith("/") && url.startsWith("/")) {
          // They don't both need a slash.
          url = url.substring(1);
        }

        String middle;
        middle = (baseUrl.endsWith("/") || url.startsWith("/")) ? "" : "/";
        String absoluteUrl = baseUrl + middle + url;

        return client.target(absoluteUrl);
    }

    protected WebTarget newWebTarget(HttpReadRequest request) {
        WebTarget webTarget = newWebTarget(request.getPath());
        for (CouchHttpQueryParam queryParam : request.getHttpQuery()) {
            webTarget = webTarget.queryParam(queryParam.getName(), queryParam.getValue());
        }
        return webTarget;
    }

  /**
   * This method is 100% hack. Cloudant is not producing a valid eTag and so we
   * are having to parse the document to look for the revision.
   * @param json the text from which the revision will be parsed.
   * @return the document's revision
   */
    protected String parseRevision(String json) {
      if (json == null) {
        return null;
      }

      int posA = json.indexOf("\"rev\"");
      if (posA < 0) return null;

      posA = json.indexOf(":", posA+5);
      if (posA < 0) return null;

      posA = json.indexOf("\"", posA+1);
      if (posA < 0) return null;

      int posB = json.indexOf("\"", posA+1);
      if (posB < 0) return null;

      return json.substring(posA+1, posB);
    }

    protected String getETag(Response response, String json) {

      String eTag = null;

      // Rewritten to do a case-insensitive lookup of the ETag.
      Set<Map.Entry<String, List<String>>> headers = response.getStringHeaders().entrySet();
      for (Map.Entry<String, List<String>> entry : headers) {
        if ("ETag".equalsIgnoreCase(entry.getKey())) {
          List<String> values = entry.getValue();
          eTag = values.isEmpty() ? null : values.get(0);
          break;
        }
      }

      if (eTag != null && (eTag.charAt(0) == '"')) {
          // Remove enclosing quotes
          eTag = eTag.substring(1, eTag.length() - 1);
      }

      if (eTag == null && response.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)) {
        // HACK, HACK, HACK...
        eTag = parseRevision(json);
      }

      return eTag;
    }

    protected CouchHttpResponse buildCouchResponse(HttpRequest request, URI uri, Response response, String documentId) {
        CouchHttpStatus statusCode = CouchHttpStatus.findByCode(response.getStatus());

        CouchMediaType mediaType = CouchMediaType.fromString(response.getMediaType().toString());

        String eTag;
        Object content;

        if (mediaType.isTextType()) {
            String text = response.readEntity(String.class);
            eTag = getETag(response, text);
            content = text;

        } else {
            eTag = getETag(response, null);
            content = response.readEntity(byte[].class);
        }

        int contentLength = response.getLength();

        return CouchHttpResponse.builder(request.getMethodType(), uri, statusCode)
            .setDocumentId(documentId)
            .setEtag(eTag)
            .setContent(mediaType, contentLength, content)
            .build();
    }
}
