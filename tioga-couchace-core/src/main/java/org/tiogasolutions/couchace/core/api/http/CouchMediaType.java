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

package org.tiogasolutions.couchace.core.api.http;

/**
 * User: harlan
 * Date: 3/10/12
 * Time: 5:56 PM
 */


public class CouchMediaType {
    private final String type;
    private final String subType;
    private final String fileExtension;

    public static final CouchMediaType UNDEFINED = applicationType("undefined");

    public final static CouchMediaType WILDCARD = type("*", "*");

    public boolean isTextType() {
        return (getType().equalsIgnoreCase("TEXT") ||
            getSubType().equalsIgnoreCase("XML") ||
            getSubType().equalsIgnoreCase("XHTML") ||
            getSubType().equalsIgnoreCase("JSON"));
    }

    public static final CouchMediaType APPLICATION_XML = applicationType("xml");
    public static final CouchMediaType APPLICATION_XHTML = applicationType("xhtml+xml", "xhtml");
    public static final CouchMediaType APPLICATION_JSON = applicationType("json");
    public static final CouchMediaType APPLICATION_ZIP = applicationType("zip");
    public static final CouchMediaType APPLICATION_PDF = applicationType("pdf");

    public static final CouchMediaType TEXT_PLAIN = textType("plain", "text");
    public static final CouchMediaType TEXT_HTML = textType("html");
    public static final CouchMediaType TEXT_XML = textType("xml");

    public static final CouchMediaType IMAGE_GIF = imageType("gif");
    public static final CouchMediaType IMAGE_JPEG = imageType("jpeg");
    public static final CouchMediaType IMAGE_TIFF = imageType("tiff");
    public static final CouchMediaType IMAGE_PNG = imageType("png");

    public static final CouchMediaType AUDIO_MPEG = audioType("mpeg");
    public static final CouchMediaType VIDEO_MP4 = videoType("mp4");

    public static final CouchMediaType MULTIPART_MIXED = multipartType("mixed");
    public static final CouchMediaType MULTIPART_RELATED = multipartType("related");
    public static final CouchMediaType MULTIPART_ALTERNATIVE = multipartType("alternative");
    public static final CouchMediaType MULTIPART_FORM_DATA = multipartType("form-data");
    public static final CouchMediaType MULTIPART_SIGNED = multipartType("signed");
    public static final CouchMediaType MULTIPART_ENCRYPTED = multipartType("encrypted");

    public static CouchMediaType newFromJson(String mediaString) {
        return fromString(mediaString);
    }

    public static CouchMediaType type(String type, String subType, String fileExtension) {
        fileExtension = (fileExtension != null) ? fileExtension : subType;
        return new CouchMediaType(type, subType, fileExtension);
    }

    public static CouchMediaType fromString(String mediaString) {
        String[] split = mediaString.split("/");
        if (split.length != 2) {
            throw new IllegalArgumentException(String.format("Invalid mime type value: %s", mediaString));
        }
        return type(split[0], split[1]);
    }

    public static CouchMediaType type(String type, String subType) {
        return type(type, subType, subType);
    }

    public static CouchMediaType applicationType(String subType) {
        return type("application", subType);
    }

    public static CouchMediaType applicationType(String subType, String fileExtension) {
        return type("application", subType, fileExtension);
    }

    public static CouchMediaType multipartType(String subType) {
        return type("multipart", subType);
    }

    public static CouchMediaType textType(String subType, String fileExtension) {
        return type("text", subType, fileExtension);
    }

    public static CouchMediaType textType(String subType) {
        return type("text", subType);
    }

    public static CouchMediaType imageType(String subType, String fileExtension) {
        return type("image", subType, fileExtension);
    }

    public static CouchMediaType imageType(String subType) {
        return type("image", subType);
    }

    public static CouchMediaType messageType(String subType, String fileExtension) {
        return type("message", subType, fileExtension);
    }

    public static CouchMediaType audioType(String subType, String fileExtension) {
        return type("audio", subType, fileExtension);
    }

    public static CouchMediaType audioType(String subType) {
        return type("audio", subType);
    }

    public static CouchMediaType videoType(String subType, String fileExtension) {
        return type("text", subType, fileExtension);
    }

    public static CouchMediaType videoType(String subType) {
        return type("text", subType);
    }

    private CouchMediaType(String type, String subType, String fileExtension) {
        if (type == null) {
            throw new NullPointerException("Null type argument give to HttpMediaType.");
        }
        if (subType == null) {
            throw new NullPointerException("Null subType argument give to HttpMediaType.");
        }
        if (fileExtension == null) {
            throw new NullPointerException("Null fileExtension argument give to HttpMediaType.");
        }
        this.type = type;
        this.subType = subType;
        this.fileExtension = fileExtension;
    }

    public String getMediaString() {
        return String.format("%s/%s", type, subType);
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public boolean isUndefined() {
        return this.equals(UNDEFINED);
    }

    public boolean isDefined() {
        return !this.equals(UNDEFINED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CouchMediaType contentMediaType = (CouchMediaType) o;

        if (!subType.equals(contentMediaType.subType)) return false;
        if (!type.equals(contentMediaType.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + subType.hashCode();
        result = 31 * result + fileExtension.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getMediaString();
    }
}
