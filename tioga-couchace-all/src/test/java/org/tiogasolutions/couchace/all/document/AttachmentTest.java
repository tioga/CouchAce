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

package org.tiogasolutions.couchace.all.document;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.response.GetAttachmentResponse;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.http.CouchMethodType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
public class AttachmentTest {
    CouchDatabase couchDatabase;
    String attachDocId = "AttachDoc";
    String attachDocJson = "{\"say\" : \"Hello\"}";
    String htmlContent = "<html><h1>Hello</h1></html>";
    RenderedImage aceOfSpacesImage;
    byte[] aceOfSpacesBytes;
    String latestRevision;

    @BeforeClass()
    public void setup() {
        couchDatabase = TestSetup.couchDatabase();
        try {
            URL url = getClass().getClassLoader().getResource("AceOfSpades.jpg");
            assertNotNull(url, "Image URL is null");
            aceOfSpacesImage = ImageIO.read(url);
            assertNotNull(aceOfSpacesImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(aceOfSpacesImage, "jpg", baos);
            baos.flush();
            aceOfSpacesBytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void putTest() {

        // Put the document and verify
        WriteResponse putDocResponse = couchDatabase.put().document(attachDocId, attachDocJson).execute();
        assertEquals(putDocResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putDocResponse.isCreated());
        assertNotNull(putDocResponse.getDocumentRevision());
        assertEquals(putDocResponse.getMethodType(), CouchMethodType.PUT);
        latestRevision = putDocResponse.getDocumentRevision();

        // Put the html attachment and verify
        WriteResponse putAttachResponse = couchDatabase
            .put()
            .attachment(putDocResponse.getDocumentId(),
                putDocResponse.getDocumentRevision(),
                "html",
                CouchMediaType.TEXT_HTML,
                htmlContent)
            .execute();

        assertEquals(putAttachResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putAttachResponse.isCreated());
        assertNotNull(putAttachResponse.getDocumentRevision());
        assertEquals(putAttachResponse.getMethodType(), CouchMethodType.PUT);
        latestRevision = putAttachResponse.getDocumentRevision();

        // Put the image bytes attachment and verify
        putAttachResponse = couchDatabase
            .put()
            .attachment(putDocResponse.getDocumentId(),
                latestRevision,
                "image",
                CouchMediaType.IMAGE_JPEG,
                aceOfSpacesBytes)
            .execute();
        assertEquals(putAttachResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        assertTrue(putAttachResponse.isCreated());
        assertNotNull(putAttachResponse.getDocumentRevision());
        assertEquals(putAttachResponse.getMethodType(), CouchMethodType.PUT);
        latestRevision = putAttachResponse.getDocumentRevision();

    }

    @Test(dependsOnMethods = {"putTest"})
    public void getLatestTest() {

        // Get html
        GetAttachmentResponse response = couchDatabase.get()
                .attachment(attachDocId, "html")
                .execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK, response.getStringContent());
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.TEXT_HTML);
        assertNotNull(response.getEtag());
        assertEquals(response.getStringContent(), htmlContent);

        // Get image
        response = couchDatabase.get()
                .attachment(attachDocId, "image")
                .execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK, response.getStringContent());
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.IMAGE_JPEG);
        assertNotNull(response.getEtag());
        byte[] responseImageBytes = (byte[]) response.getContent();

        assertEquals(responseImageBytes.length, aceOfSpacesBytes.length);
        assertTrue(Arrays.equals(responseImageBytes, aceOfSpacesBytes));
    }

    @Test(dependsOnMethods = {"getLatestTest"})
    public void getVersionTest() {

        GetAttachmentResponse response = couchDatabase.get()
                .attachment(attachDocId, latestRevision, "html")
                .execute();

        // Get html
        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK, response.getStringContent());
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.TEXT_HTML);
        assertNotNull(response.getEtag());
        assertEquals(response.getStringContent(), htmlContent);

        // Get image
        response = couchDatabase.get().attachment(attachDocId, latestRevision, "image").execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK, response.getStringContent());
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.IMAGE_JPEG);
        assertNotNull(response.getEtag());
        byte[] responseImageBytes = (byte[]) response.getContent();

        assertEquals(responseImageBytes.length, aceOfSpacesBytes.length);
        assertTrue(Arrays.equals(responseImageBytes, aceOfSpacesBytes));
    }

    @Test(dependsOnMethods = {"getVersionTest"})
    public void deleteTest() {

        // Delete the attachment
        WriteResponse deleteResponse = couchDatabase
                .delete()
                .attachment(attachDocId, latestRevision, "html")
                .execute();
        assertEquals(deleteResponse.getHttpStatus(), CouchHttpStatus.OK);
        String previousVersion = latestRevision;
        latestRevision = deleteResponse.getDocumentRevision();

        // Get attachment should now return not found
        GetAttachmentResponse response = couchDatabase
                .get()
                .attachment(attachDocId, latestRevision, "html")
                .execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.NOT_FOUND);

        // Get previous version should be found
        response = couchDatabase
                .get()
                .attachment(attachDocId, previousVersion, "html")
                .execute();
        assertEquals(response.getHttpStatus(), CouchHttpStatus.OK, response.getStringContent());
        assertTrue(response.isOk());
        assertEquals(response.getContentType(), CouchMediaType.TEXT_HTML);
        assertNotNull(response.getEtag());
        assertEquals(response.getStringContent(), htmlContent);

    }

}
