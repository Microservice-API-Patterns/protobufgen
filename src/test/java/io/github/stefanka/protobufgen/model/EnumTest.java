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
package io.github.stefanka.protobufgen.model;

import io.github.stefanka.protobufgen.exception.FieldAlreadyExistsException;
import io.github.stefanka.protobufgen.exception.FirstEnumFieldZeroValueException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EnumTest {

    @Test
    public void canCreateEnum() {
        // given
        Enum.Builder builder = new Enum.Builder("MyTestEnum");

        // when
        Enum enumm = builder.build();

        // then
        assertEquals("MyTestEnum", enumm.getName());
        assertEquals("MyTestEnum", enumm.getIdentifier().toString());
    }

    @Test
    public void canAddEnumField() {
        // given
        Enum.Builder builder = new Enum.Builder("TestEnum");

        // when
        Enum enumm = builder.withField(new EnumField.Builder("VAL", 0)).build();

        // then
        assertEquals(1, enumm.getFields().size());
        assertEquals("VAL", enumm.getFields().iterator().next().getName());
    }

    @Test
    public void canCountValueAutomatically() {
        // given
        Enum.Builder builder = new Enum.Builder("TestEnum");

        // when
        Enum enumm = builder.withField("VAL1")
                .withField("VAL2")
                .build();

        // then
        assertEquals(0, enumm.getFields().stream().filter(f -> f.getName().equals("VAL1")).map(f -> f.getValue()).findAny().get());
        assertEquals(1, enumm.getFields().stream().filter(f -> f.getName().equals("VAL2")).map(f -> f.getValue()).findAny().get());
    }

    @Test
    public void canEnsureFieldOrderAccordingToValue() {
        // given
        Enum enumm = new Enum.Builder("TestEnum")
                .withField(new EnumField.Builder("myField0", 0))
                .withField(new EnumField.Builder("myField2", 2))
                .withField(new EnumField.Builder("myField1", 1))
                .build();

        // when
        Iterator<EnumField> it = enumm.getFields().iterator();
        EnumField first = it.next();
        EnumField second = it.next();
        EnumField third = it.next();

        // then
        assertEquals(0, first.getValue());
        assertEquals(1, second.getValue());
        assertEquals(2, third.getValue());
    }

    @Test
    public void ensureFirstFieldHasZeroValue() {
        // given
        Enum.Builder builder = new Enum.Builder("TestEnum");

        // when, then
        assertThrows(FirstEnumFieldZeroValueException.class, () -> {
            builder.withField(new EnumField.Builder("notZeroValue", 1));
        });
    }

    @Test
    public void cannotAddFieldWithAlreadyExistingName() {
        // given
        Enum.Builder builder = new Enum.Builder("TestEnum")
                .withField("TEST");

        // when, then
        assertThrows(FieldAlreadyExistsException.class, () -> {
            builder.withField("TEST");
        });
    }

    @Test
    public void canAddComment() {
        // given
        Enum.Builder builder = new Enum.Builder("TestEnum");

        // when
        Enum enumm = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", enumm.getComment());
    }

    @Test
    public void canDetermineEquality() {
        // given
        Enum enum1 = new Enum.Builder("TestEnum").build();
        Enum enum2 = new Enum.Builder("TestEnum").build();
        Enum enum3 = new Enum.Builder("OtherTestEnum").build();

        // when
        boolean equal = enum1.equals(enum2);
        boolean notEqual = enum1.equals(enum3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<Enum> enumSet = new HashSet<>();

        // when
        enumSet.add(new Enum.Builder("TestEnum").build());
        enumSet.add(new Enum.Builder("TestEnum").build());

        // then
        assertEquals(1, enumSet.size());
    }

}
