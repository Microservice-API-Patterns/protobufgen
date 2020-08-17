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

public class EnumMessageFieldTest {

    @Test
    public void canCreateEnumField() {
        // given
        EnumField.Builder builder = new EnumField.Builder("VALUE", 1);

        // when
        EnumField enumField = builder.build();

        // then
        assertEquals("VALUE", enumField.getName());
        assertEquals(1, enumField.getValue());
    }

    @Test
    public void canAddComment() {
        // given
        EnumField.Builder builder = new EnumField.Builder("TestField", 0);

        // when
        EnumField enumField = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", enumField.getComment());
    }

    @Test
    public void canDetermineEquality() {
        // given
        EnumField field1 = new EnumField.Builder("VAL1", 1).build();
        EnumField field2 = new EnumField.Builder("VAL1", 2).build();
        EnumField field3 = new EnumField.Builder("VAL2", 3).build();

        // when
        boolean equal = field1.equals(field2);
        boolean notEqual = field1.equals(field3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canGenerateHashCode() {
        // given
        Set<EnumField> enumFieldSet = new HashSet<>();

        // when
        enumFieldSet.add(new EnumField.Builder("VAL", 1).build());
        enumFieldSet.add(new EnumField.Builder("VAL", 2).build());

        // then
        assertEquals(1, enumFieldSet.size());
    }

}
