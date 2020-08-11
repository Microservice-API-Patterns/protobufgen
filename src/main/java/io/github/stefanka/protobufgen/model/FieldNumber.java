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

import io.github.stefanka.protobufgen.exception.FieldNumberOutOfRangeException;
import io.github.stefanka.protobufgen.exception.FieldNumberReservedException;

import java.util.Objects;

/**
 * Represents a protocol buffer field number.
 *
 * @author Stefan Kapferer
 */
public class FieldNumber implements Comparable<FieldNumber> {

    private final int number;

    public FieldNumber(int number) {
        if (number < 1 || number > 536870911)
            throw new FieldNumberOutOfRangeException(number);
        if (number >= 19000 && number <= 19999)
            throw new FieldNumberReservedException();
        this.number = number;
    }

    /**
     * Returns the field number as integer.
     *
     * @return the field number as integer
     */
    public int toInt() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldNumber that = (FieldNumber) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public int compareTo(FieldNumber fieldNumber) {
        return Integer.valueOf(number).compareTo(fieldNumber.number);
    }
}
