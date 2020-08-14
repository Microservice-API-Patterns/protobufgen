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

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnyTypeTest {

    @Test
    public void canCreateAnyType() {
        // given
        AnyType any;

        // when
        any = new AnyType();

        // then
        assertEquals("google.protobuf.Any", any.getName());
        assertEquals("google.protobuf.Any", any.toString());
    }

    @Test
    public void ensureAllAnyTypeInstancesAreEqual() {
        // given
        AnyType any1 = new AnyType();
        AnyType any2 = new AnyType();

        // when
        boolean equal = any1.equals(any2);

        // then
        assertTrue(equal);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<FieldType> typeSet = new HashSet<>();

        // when
        typeSet.add(new AnyType());
        typeSet.add(new AnyType());

        // then
        assertEquals(1, typeSet.size());
    }

}
