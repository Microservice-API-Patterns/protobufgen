/*
 * Copyright 2020 Stefan Kapferer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.microserviceapipatterns.protobufgen.model;

import io.github.microserviceapipatterns.protobufgen.AbstractProtoIntegTest;
import io.github.microserviceapipatterns.protobufgen.exception.RootElementAlreadyExistsException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ProtoSpecTest extends AbstractProtoIntegTest {

    @Test
    public void canCreateProto3Spec() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.build();

        // then
        assertEquals("proto3", spec.getSyntax());
    }

    @Test
    public void canAddMessage() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withMessage(new Message.Builder("MyTestMessage")).build();

        // then
        assertEquals(1, spec.getMessages().size());
        assertEquals("MyTestMessage", spec.getMessages().iterator().next().getName());
    }

    @Test
    public void cannotAddMessageWithAlreadyExistingName() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        builder.withMessage(new Message.Builder("MyTestMessage"));

        // then
        assertThrows(RootElementAlreadyExistsException.class, () -> {
            builder.withMessage(new Message.Builder("MyTestMessage"));
        });
    }

    @Test
    public void canAddEnum() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withEnum(new Enum.Builder("TestEnum")).build();

        // then
        assertEquals(1, spec.getEnums().size());
        assertEquals("TestEnum", spec.getEnums().iterator().next().getName());
    }

    @Test
    public void cannotAddEnumWithAlreadyExistingName() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        builder.withMessage(new Message.Builder("MyTestMessage"));

        // then
        assertThrows(RootElementAlreadyExistsException.class, () -> {
            builder.withEnum(new Enum.Builder("MyTestMessage"));
        });
    }

    @Test
    public void canAddService() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withService(new Service.Builder("TestService")).build();

        // then
        assertEquals(1, spec.getServices().size());
        assertEquals("TestService", spec.getServices().iterator().next().getName());
    }

    @Test
    public void cannotAddServiceWithAlreadyExistingName() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        builder.withMessage(new Message.Builder("MyTestObject"));

        // then
        assertThrows(RootElementAlreadyExistsException.class, () -> {
            builder.withService(new Service.Builder("MyTestObject"));
        });
    }

    @Test
    public void canAddImport() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withImport("protos/test.proto").build();

        // then
        assertEquals(1, spec.getImportStatements().size());
        ImportStatement importStatement = spec.getImportStatements().iterator().next();
        assertEquals("protos/test.proto", importStatement.getFileName());
        assertFalse(importStatement.isPublic());
    }

    @Test
    public void canAddPublicImport() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withImport("protos/test.proto", true).build();

        // then
        assertEquals(1, spec.getImportStatements().size());
        ImportStatement importStatement = spec.getImportStatements().iterator().next();
        assertEquals("protos/test.proto", importStatement.getFileName());
        assertTrue(importStatement.isPublic());
    }

    @Test
    public void canSetPackage() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withPackage("ch.kapferer.stefan").build();

        // then
        assertEquals("ch.kapferer.stefan", spec.getPackage());
    }

    @Test
    public void canSerializeItself() {
        // given
        ProtoSpec spec = new ProtoSpec.Builder().build();

        // when
        String proto = spec.toString();

        // then
        assertEquals("syntax = \"proto3\";" + System.lineSeparator() + System.lineSeparator(), proto);
    }

    @Test
    public void canPersistItself() throws IOException {
        // given
        ProtoSpec spec = new ProtoSpec.Builder().build();

        // when
        File testFile = new File(getTestDir(), "persisted.proto");
        spec.persistProto(testFile);

        // then
        assertTrue(testFile.exists());
        assertEquals("syntax = \"proto3\";" + System.lineSeparator() + System.lineSeparator(), readTestOutputFile("persisted.proto"));
    }

    @Test
    public void canAddComment() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();

        // when
        ProtoSpec spec = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", spec.getComment());
    }

    @Test
    public void canAutomaticallyImportAnyTypeIfNeeded() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();
        Message message = new Message.Builder("TestMessage")
                .withField(new AnyType(), "anyField")
                .build();

        // when
        ProtoSpec spec = builder.withMessage(message).build();

        // then
        assertEquals(1, spec.getMessages().size());
        assertEquals("google.protobuf.Any", spec.getMessages().iterator().next().getFields().iterator().next().getType());
        assertEquals(1, spec.getImportStatements().size());
        assertEquals("google/protobuf/any.proto", spec.getImportStatements().iterator().next().getFileName());
    }

    @Test
    public void canAutomaticallyImportAnyType4NestedMessageIfNeeded() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();
        Message nested = new Message.Builder("NestedType")
                .withField(new AnyType(), "anyField")
                .build();
        Message message = new Message.Builder("TestMessage")
                .withField(nested, "messageField")
                .withNestedMessage(nested)
                .build();

        // when
        ProtoSpec spec = builder.withMessage(message).build();

        // then
        assertEquals(1, spec.getImportStatements().size());
        assertEquals("google/protobuf/any.proto", spec.getImportStatements().iterator().next().getFileName());
    }

    @Test
    public void doNotAddAnyTypeImportIfDoneManually() {
        // given
        ProtoSpec.Builder builder = new ProtoSpec.Builder();
        Message message = new Message.Builder("TestMessage")
                .withField(new AnyType(), "anyField")
                .build();

        // when
        ProtoSpec spec = builder.withMessage(message)
                .withImport("google/protobuf/any.proto")
                .build();

        // then
        assertEquals(1, spec.getImportStatements().size());
        assertEquals("google/protobuf/any.proto", spec.getImportStatements().iterator().next().getFileName());
    }

}
