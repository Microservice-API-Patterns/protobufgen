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

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFieldTest {

    @Test
    public void canBuildFieldWithName() {
        // given
        MessageField.Builder builder = new MessageField.Builder(SimpleFieldType.DOUBLE, "myField", 1);

        // when
        MessageField messageField = builder.build();

        // then
        assertEquals("myField", messageField.getName());
        assertEquals(1, messageField.getNumber());
        assertEquals("double", messageField.getType());
    }

    @Test
    public void canAddComment() {
        // given
        MessageField.Builder builder = new MessageField.Builder(SimpleFieldType.DOUBLE, "myField", 1);

        // when
        MessageField messageField = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", messageField.getComment());
        assertEquals(1, messageField.getNumber());
        assertEquals("double", messageField.getType());
    }

    @Test
    public void canUseMessageAsType() {
        // given
        Message message = new Message.Builder("MyCustomType").build();

        // when
        MessageField messageField = new MessageField.Builder(message, "myField", 1).build();

        // then
        assertEquals("MyCustomType", messageField.getType());
    }

    @Test
    public void canUseAnyType() {
        // given
        MessageField field;

        // when
        field = new MessageField.Builder(new AnyType(), "anyField", 1).build();

        // then
        assertEquals("google.protobuf.Any", field.getType());
    }

    @Test
    public void canSetRepeatedFlag() {
        // given
        MessageField.Builder fb1 = new MessageField.Builder(SimpleFieldType.FLOAT, "myTestField1", 1);
        MessageField.Builder fb2 = new MessageField.Builder(SimpleFieldType.FLOAT, "myTestField2", 2);

        // when
        MessageField messageField1 = fb1.build();
        MessageField messageField2 = fb2.repeated().build();

        // then
        assertFalse(messageField1.isRepeated());
        assertTrue(messageField2.isRepeated());
    }

    @Test
    public void canDetermineEqualityByName() {
        // given
        MessageField messageField1 = new MessageField.Builder(SimpleFieldType.DOUBLE, "myField", 1).build();
        MessageField messageField2 = new MessageField.Builder(SimpleFieldType.DOUBLE, "myField", 2).build();
        MessageField messageField3 = new MessageField.Builder(SimpleFieldType.DOUBLE, "myFieldWithOtherName", 2).build();

        // when
        boolean equal = messageField1.equals(messageField2);
        boolean notEqual = messageField1.equals(messageField3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        MessageField messageField1 = new MessageField.Builder(SimpleFieldType.DOUBLE, "myField", 1).build();
        MessageField messageField2 = new MessageField.Builder(SimpleFieldType.DOUBLE, "myField", 2).build();
        Set<MessageField> messageFieldSet = new HashSet<>();

        // when
        messageFieldSet.add(messageField1);
        messageFieldSet.add(messageField2);

        // then
        assertEquals(1, messageFieldSet.size());
    }

}
