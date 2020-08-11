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
package io.github.stefanka.protobufgen.serializer;

import io.github.stefanka.protobufgen.AbstractProtoIntegTest;
import io.github.stefanka.protobufgen.model.Enum;
import io.github.stefanka.protobufgen.model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.github.stefanka.protobufgen.model.SimpleFieldType.INT32;
import static io.github.stefanka.protobufgen.model.SimpleFieldType.STRING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProtoSpecSerializerTest extends AbstractProtoIntegTest {

    @Test
    public void canSerializeEmptySpec() {
        // given
        ProtoSpecSerializer serializer = new ProtoSpecSerializer();

        // when
        String proto = serializer.serialize(new ProtoSpec.Builder().build());

        // then
        assertEquals("syntax = \"proto3\";" + System.lineSeparator() + System.lineSeparator(), proto);
    }

    @Test
    public void canSerializeIntegrationTestSample1() throws IOException {
        // given
        Enum phoneType = new Enum.Builder("PhoneType")
                .withComment("PhoneType comment")
                .withField(new EnumField.Builder("MOBILE", 0).withComment("mobile comment"))
                .withField("HOME")
                .withField(new EnumField.Builder("WORK", 2).withComment("work comment"))
                .build();
        Message phoneNumber = new Message.Builder("PhoneNumber")
                .withField(STRING, "number")
                .withField(phoneType, "type")
                .build();
        Message person = new Message.Builder("Person")
                .withComment("Person comment")
                .withField(new MessageField.Builder(STRING, "name", 1).withComment("name comment"))
                .withField(new MessageField.Builder(INT32, "id", 2).withComment("id comment"))
                .withField(new MessageField.Builder(STRING, "email", 3).withComment("email comment"))
                .withField(new MessageField.Builder(phoneNumber, "phones", 4).repeated().withComment("phones comment"))
                .build();
        Message addressBook = new Message.Builder("AddressBook")
                .withComment("AddressBook comment")
                .withField(new MessageField.Builder(person, "people", 1).repeated().withComment("people comment"))
                .build();
        Message searchRequest = new Message.Builder("SearchPersonRequest")
                .withField(new MessageField.Builder(STRING, "name", 1))
                .build();
        Message searchResponse = new Message.Builder("SearchPersonResponse")
                .withField(new MessageField.Builder(person, "persons", 1).repeated())
                .build();
        Service searchService = new Service.Builder("SearchPersonService")
                .withComment("SearchPersonService comment")
                .withRPC(new RemoteProcedureCall.Builder("Search", searchRequest, searchResponse).withComment("search comment"))
                .build();
        ProtoSpec spec = new ProtoSpec.Builder()
                .withPackage("integTests.sample1")
                .withMessage(person)
                .withMessage(phoneNumber)
                .withMessage(addressBook)
                .withEnum(phoneType)
                .withMessage(searchRequest)
                .withMessage(searchResponse)
                .withService(searchService)
                .build();

        // when
        String proto = new ProtoSpecSerializer().serialize(spec);
        new ProtoSpecSerializer().writeToFile(spec, new File(getTestDir(), "sample1.proto"));

        // then
        assertEquals(readIntegTestFile("sample1.proto"), proto);
        assertTrue(new File(getTestDir(), "sample1.proto").exists());
    }

    @Test
    public void canSerializeImports() throws IOException {
        // given
        ProtoSpec spec = new ProtoSpec.Builder()
                .withPackage("integTests.importTest")
                .withImport("sample1.proto")
                .withMessage(new Message.Builder("JustSomeTestMessage"))
                .build();

        // when
        String proto = new ProtoSpecSerializer().serialize(spec);

        // then
        assertEquals(readIntegTestFile("import-test.proto"), proto);
    }

    @Test
    public void canSerializeStreamsKeywordsInRPC() throws IOException {
        // given
        Message inputMessage = new Message.Builder("InputMessage").build();
        Message outputMessage = new Message.Builder("OutputMessage").build();
        ProtoSpec spec = new ProtoSpec.Builder()
                .withPackage("integTests.streamFlagTest")
                .withMessage(inputMessage)
                .withMessage(outputMessage)
                .withService(new Service.Builder("TestService")
                        .withRPC(new RemoteProcedureCall.Builder("TestCall", inputMessage, outputMessage)
                                .withInputAsStream()
                                .withOutputAsStream()))
                .build();

        // when
        String proto = new ProtoSpecSerializer().serialize(spec);

        // then
        assertEquals(readIntegTestFile("streams-test.proto"), proto);
    }

    @Test
    public void canWriteToFile() throws IOException {
        // given
        ProtoSpecSerializer serializer = new ProtoSpecSerializer();

        // when
        File testFile = new File(getTestDir(), "empty.proto");
        serializer.writeToFile(new ProtoSpec.Builder().build(), testFile);

        // then
        assertTrue(testFile.exists());
        assertEquals("syntax = \"proto3\";" + System.lineSeparator() + System.lineSeparator(), readTestOutputFile("empty.proto"));
    }

}
