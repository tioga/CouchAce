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

package org.tiogasolutions.couchace.all.entity.notification;

import org.tiogasolutions.couchace.all.test.TestSetup;
import org.tiogasolutions.couchace.annotations.CouchAttachmentInfo;
import org.tiogasolutions.couchace.annotations.CouchAttachmentInfoMap;
import org.tiogasolutions.couchace.core.api.CouchDatabase;
import org.tiogasolutions.couchace.core.api.http.CouchHttpStatus;
import org.tiogasolutions.couchace.core.api.http.CouchMediaType;
import org.tiogasolutions.couchace.core.api.response.GetAttachmentResponse;
import org.tiogasolutions.couchace.core.api.response.WriteResponse;
import org.tiogasolutions.couchace.core.internal.util.ClassUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.testng.Assert.*;

/**
 * User: harlan
 * Date: 9/15/12
 * Time: 8:21 PM
 */
@Test
public class NotificationTest {

    private static final String FIRST_ATTACHMENT_NAME = "FirstAttachment";
    private static final String FIRST_ATTACHMENT_CONTENT = "<html><p>Hello Attachment</p></html>";
    private static final String SECOND_ATTACHMENT_NAME = "SecondAttachment";
    private static final String SECOND_ATTACHMENT_CONTENT = "<html><p>Hello Second Attachment</p></html>";

    private CouchDatabase couchDatabase = TestSetup.couchDatabase();

    public void notificationWithNoAttachmentsTest() throws IllegalAccessException {
        // Write the initial notification
        Notification notification = Notification.newNotification("Some Message");
        WriteResponse writeNotificationResponse = couchDatabase.put()
                .entity(notification)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeNotificationResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();

        // Now update the notification
        notification.setSent(true);
        writeNotificationResponse = couchDatabase.put()
                .entity(notification)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeNotificationResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        // Retrieve the notification again, sent should now be true
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();
        Assert.assertEquals(notification.isSent(), true);
        Assert.assertEquals(notification.getRevision(), writeNotificationResponse.getDocumentRevision());
    }

    public void notificationWithOneAttachmentsTest() throws IllegalAccessException {
        // Write the initial notification
        Notification notification = Notification.newNotification("Some Message");
        WriteResponse writeNotificationResponse = couchDatabase.put()
                .entity(notification)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeNotificationResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();

        // Now write an attachment
        WriteResponse writeAttachmentResponse = couchDatabase.put()
                .attachment(notification.getId(), notification.getRevision(), FIRST_ATTACHMENT_NAME, CouchMediaType.TEXT_HTML, FIRST_ATTACHMENT_CONTENT)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeAttachmentResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        // Retrieve the attachment
        GetAttachmentResponse getAttachmentResponse = couchDatabase.get()
                .attachment(notification.getId(), FIRST_ATTACHMENT_NAME)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(getAttachmentResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getAttachmentResponse.getContent(), FIRST_ATTACHMENT_CONTENT);

        // Get notification with the updated revision
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();
        Assert.assertEquals(writeAttachmentResponse.getDocumentRevision(), notification.getRevision());

        // Ensure the notification has attachment meta.
        Field metaMatchField = ClassUtil.findField(Notification.class, "attachmentInfoMap");
        assertNotNull(metaMatchField);
        metaMatchField.setAccessible(true);
        CouchAttachmentInfoMap attachmentMap = (CouchAttachmentInfoMap)metaMatchField.get(notification);
        assertNotNull(attachmentMap);
        assertEquals(attachmentMap.size(), 1);
        assertTrue(attachmentMap.containsAttachment(FIRST_ATTACHMENT_NAME));
        CouchAttachmentInfo attachmentInfo = attachmentMap.get(FIRST_ATTACHMENT_NAME);
        assertEquals(attachmentInfo.getContentType(), "text/html");

        // Now update the notification
        notification.setSent(true);
        writeNotificationResponse = couchDatabase.put()
                .entity(notification)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeNotificationResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        // Retrieve the notification again, sent should now be true
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();
        Assert.assertEquals(notification.isSent(), true);
        Assert.assertEquals(notification.getRevision(), writeNotificationResponse.getDocumentRevision());

        // Retrieve the attachment, should still be there.
        getAttachmentResponse = couchDatabase.get()
                .attachment(notification.getId(), FIRST_ATTACHMENT_NAME)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(getAttachmentResponse.getHttpStatus(), CouchHttpStatus.OK);
        Assert.assertEquals(getAttachmentResponse.getContent(), FIRST_ATTACHMENT_CONTENT);

    }

    public void notificationWithTwoAttachmentsTest() throws IllegalAccessException {
        // Write the initial notification
        Notification notification = Notification.newNotification("Some Message");
        WriteResponse writeNotificationResponse = couchDatabase.put()
                .entity(notification)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeNotificationResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();

        // Now write two attachments
        WriteResponse writeAttachmentResponse = couchDatabase.put()
                .attachment(notification.getId(), notification.getRevision(), FIRST_ATTACHMENT_NAME, CouchMediaType.TEXT_HTML, FIRST_ATTACHMENT_CONTENT)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeAttachmentResponse.getHttpStatus(), CouchHttpStatus.CREATED);
        writeAttachmentResponse = couchDatabase.put()
                .attachment(notification.getId(), writeAttachmentResponse.getDocumentRevision(), SECOND_ATTACHMENT_NAME, CouchMediaType.TEXT_HTML, SECOND_ATTACHMENT_CONTENT)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeAttachmentResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        // Retrieve the first attachment
        GetAttachmentResponse getAttachmentResponse = couchDatabase.get()
                .attachment(notification.getId(), FIRST_ATTACHMENT_NAME)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(getAttachmentResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getAttachmentResponse.getContent(), FIRST_ATTACHMENT_CONTENT);

        // Retrieve the second attachment
        getAttachmentResponse = couchDatabase.get()
                .attachment(notification.getId(), SECOND_ATTACHMENT_NAME)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(getAttachmentResponse.getHttpStatus(), CouchHttpStatus.OK);
        assertEquals(getAttachmentResponse.getContent(), SECOND_ATTACHMENT_CONTENT);

        // Get notification with the updated revision
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();
        Assert.assertEquals(writeAttachmentResponse.getDocumentRevision(), notification.getRevision());

        // Ensure the notification has attachment meta.
        Field metaMatchField = ClassUtil.findField(Notification.class, "attachmentInfoMap");
        assertNotNull(metaMatchField);
        metaMatchField.setAccessible(true);
        CouchAttachmentInfoMap attachmentMap = (CouchAttachmentInfoMap)metaMatchField.get(notification);
        assertNotNull(attachmentMap);
        assertEquals(attachmentMap.size(), 2);
        assertTrue(attachmentMap.containsAttachment(FIRST_ATTACHMENT_NAME));
        assertTrue(attachmentMap.containsAttachment(SECOND_ATTACHMENT_NAME));
        CouchAttachmentInfo attachmentMeta = attachmentMap.get(FIRST_ATTACHMENT_NAME);
        assertEquals(attachmentMeta.getContentType(), "text/html");
        attachmentMeta = attachmentMap.get(SECOND_ATTACHMENT_NAME);
        assertEquals(attachmentMeta.getContentType(), "text/html");

        // Now update the notification
        notification.setSent(true);
        writeNotificationResponse = couchDatabase.put()
                .entity(notification)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(writeNotificationResponse.getHttpStatus(), CouchHttpStatus.CREATED);

        // Retrieve the notification again, sent should now be true
        notification = couchDatabase.get()
                .entity(Notification.class, writeNotificationResponse.getDocumentId())
                .onError(r -> fail(r.getErrorReason()))
                .execute()
                .getFirstEntity();
        Assert.assertEquals(notification.isSent(), true);
        Assert.assertEquals(notification.getRevision(), writeNotificationResponse.getDocumentRevision());

        // Retrieve the attachment, should still be there.
        getAttachmentResponse = couchDatabase.get()
                .attachment(notification.getId(), FIRST_ATTACHMENT_NAME)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(getAttachmentResponse.getHttpStatus(), CouchHttpStatus.OK);
        Assert.assertEquals(getAttachmentResponse.getContent(), FIRST_ATTACHMENT_CONTENT);
        getAttachmentResponse = couchDatabase.get()
                .attachment(notification.getId(), SECOND_ATTACHMENT_NAME)
                .onError(r -> fail(r.getErrorReason()))
                .execute();
        Assert.assertEquals(getAttachmentResponse.getHttpStatus(), CouchHttpStatus.OK);
        Assert.assertEquals(getAttachmentResponse.getContent(), SECOND_ATTACHMENT_CONTENT);

    }
}
