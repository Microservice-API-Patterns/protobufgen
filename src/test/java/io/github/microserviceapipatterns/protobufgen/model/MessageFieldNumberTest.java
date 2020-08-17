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

import io.github.microserviceapipatterns.protobufgen.exception.FieldNumberOutOfRangeException;
import io.github.microserviceapipatterns.protobufgen.exception.FieldNumberReservedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MessageFieldNumberTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 536870911})
    public void canCreateFieldNumberInRange(int number) {
        // given
        FieldNumber fieldNumber;

        // when
        fieldNumber = new FieldNumber(number);

        // then
        assertEquals(number, fieldNumber.toInt());
    }

    @ParameterizedTest
    @ValueSource(ints = {19000, 19001, 19999})
    public void cannotUseReservedNumbers(int number) {
        assertThrows(FieldNumberReservedException.class, () -> {
            new FieldNumber(number);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 536870912})
    public void cannotUsedNumbersOutOfRange(int number) {
        assertThrows(FieldNumberOutOfRangeException.class, () -> {
            new FieldNumber(number);
        });
    }

    @Test
    public void canDetermineEquality() {
        // given
        FieldNumber fieldNumber1 = new FieldNumber(1);
        FieldNumber fieldNumber2 = new FieldNumber(1);
        FieldNumber fieldNumber3 = new FieldNumber(2);

        // when
        boolean equal = fieldNumber1.equals(fieldNumber2);
        boolean notEqual = fieldNumber1.equals(fieldNumber3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<FieldNumber> numberSet = new HashSet<FieldNumber>();

        // when
        numberSet.add(new FieldNumber(1));
        numberSet.add(new FieldNumber(1));

        // then
        assertEquals(1, numberSet.size());
    }

}
