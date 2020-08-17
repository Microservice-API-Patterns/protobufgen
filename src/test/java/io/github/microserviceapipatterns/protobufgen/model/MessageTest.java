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

import io.github.microserviceapipatterns.protobufgen.exception.NestedMessageAlreadyExistsException;
import io.github.microserviceapipatterns.protobufgen.exception.FieldAlreadyExistsException;
import io.github.microserviceapipatterns.protobufgen.exception.FieldNumberAlreadyExistsException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void canBuildMessageWithName() {
        // given
        Message.Builder builder = new Message.Builder("MyMessage");

        // when
        Message message = builder.build();

        // then
        assertEquals("MyMessage", message.getName());
        assertEquals("MyMessage", message.getIdentifier().toString());
    }

    @Test
    public void canAddComment() {
        // given
        Message.Builder builder = new Message.Builder("MyMessage");

        // when
        Message message = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", message.getComment());
    }

    @Test
    public void canAddField() {
        // given
        Message.Builder builder = new Message.Builder("MyTestMessage");

        // when
        builder.withField(new MessageField.Builder(SimpleFieldType.FLOAT, "myField", 1).build());
        Message message = builder.build();

        // then
        assertEquals(1, message.getFields().size());
        assertEquals("myField", message.getFields().iterator().next().getName());
    }

    @Test
    public void canAddFieldViaBuilder() {
        // given
        Message.Builder builder = new Message.Builder("MyTestMessage");

        // when
        builder.withField(new MessageField.Builder(SimpleFieldType.FLOAT, "myField", 1));
        Message message = builder.build();

        // then
        assertEquals(1, message.getFields().size());
        assertEquals("myField", message.getFields().iterator().next().getName());
    }

    @Test
    public void cannotAddFieldWithAlreadyExistingName() {
        // given
        Message.Builder builder = new Message.Builder("MyTestMessage");

        // when
        builder.withField(new MessageField.Builder(SimpleFieldType.FLOAT, "myTestField", 1));

        // then
        assertThrows(FieldAlreadyExistsException.class, () -> {
            builder.withField(new MessageField.Builder(SimpleFieldType.FLOAT, "myTestField", 2));
        });
    }

    @Test
    public void cannotAddFieldWithAlreadyExistingIndex() {
        // given
        Message.Builder builder = new Message.Builder("MyTestMessage");

        // when
        builder.withField(new MessageField.Builder(SimpleFieldType.FLOAT, "myTestField", 1));

        // then
        assertThrows(FieldNumberAlreadyExistsException.class, () -> {
            builder.withField(new MessageField.Builder(SimpleFieldType.FLOAT, "myTestField2", 1));
        });
    }

    @Test
    public void canGiveFieldNumbersAutomatically() {
        // given
        Message.Builder builder = new Message.Builder("TestMessage");

        // when
        Message message = builder.withField(SimpleFieldType.FLOAT, "testField1")
                .withField(SimpleFieldType.BOOL, "testField2")
                .build();

        // then
        assertEquals(1, message.getFields().stream().filter(f -> f.getName().equals("testField1")).map(f -> f.getNumber()).findAny().get());
        assertEquals(2, message.getFields().stream().filter(f -> f.getName().equals("testField2")).map(f -> f.getNumber()).findAny().get());
    }

    @Test
    public void canNestMessages() {
        // given
        Message.Builder parentMessage = new Message.Builder("ParentMessage");
        Message.Builder childMessage = new Message.Builder("ChildMessage");

        // when
        Message child = childMessage.build();
        Message parent = parentMessage.withNestedMessage(child).build();

        // then
        assertEquals(1, parent.getNestedMessages().size());
        assertTrue(child.isNestedMessage());
        assertEquals("ParentMessage", child.getParent().getName());
        assertEquals("ParentMessage.ChildMessage", child.getName());
    }

    @Test
    public void canNestNestedMessages() {
        // given
        Message.Builder parentMessage = new Message.Builder("ParentMessage");
        Message.Builder childMessage = new Message.Builder("ChildMessage");
        Message.Builder childChildMessage = new Message.Builder("ChildChildMessage");

        // when
        Message childChild = childChildMessage.build();
        Message child = childMessage.withNestedMessage(childChild).build();
        Message parent = parentMessage.withNestedMessage(child).build();

        // then
        assertEquals(1, parent.getNestedMessages().size());
        assertEquals(1, child.getNestedMessages().size());
        assertTrue(child.isNestedMessage());
        assertTrue(childChild.isNestedMessage());
        assertEquals("ParentMessage", child.getParent().getName());
        assertEquals("ParentMessage.ChildMessage", childChild.getParent().getName());
        assertEquals("ParentMessage.ChildMessage.ChildChildMessage", childChild.getName());
        assertEquals("ChildMessage", childChild.getParent().getSimpleName());
        assertEquals("ChildChildMessage", childChild.getSimpleName());
    }

    @Test
    public void cannotAddDuplicateNestedMessage() {
        // given
        Message.Builder parentMessage = new Message.Builder("ParentMessage");
        Message.Builder childMessage1 = new Message.Builder("ChildMessage");
        Message.Builder childMessage2 = new Message.Builder("ChildMessage");
        parentMessage.withNestedMessage(childMessage1.build());

        // when, then
        assertThrows(NestedMessageAlreadyExistsException.class, () -> {
            parentMessage.withNestedMessage(childMessage2.build());
        });
    }

    @Test
    public void canDetermineEquality() {
        // given
        Message message1 = new Message.Builder("TestMessage1").build();
        Message message2 = new Message.Builder("TestMessage1").build();
        Message message3 = new Message.Builder("TestMessage2").build();

        // when
        boolean equal = message1.equals(message2);
        boolean notEqual = message1.equals(message3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<Message> messageSet = new HashSet<>();

        // when
        messageSet.add(new Message.Builder("TestMessage1").build());
        messageSet.add(new Message.Builder("TestMessage1").build());

        // then
        assertEquals(1, messageSet.size());
    }

}
