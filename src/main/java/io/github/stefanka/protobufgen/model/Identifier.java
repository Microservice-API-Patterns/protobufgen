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

import io.github.stefanka.protobufgen.exception.WrongIdentifierException;

import java.util.Objects;

/**
 * Represents an identifier (ident) according to the .proto lang spec:
 * <p>
 * letter = "A" … "Z" | "a" … "z"
 * decimalDigit = "0" … "9"
 * ident = letter { letter | decimalDigit | "_" }
 *
 * @author Stefan Kapferer
 */
public class Identifier {

    private static final String IDENT_REGEX = "^[a-zA-Z][a-zA-Z0-9_]*";

    private final String name;

    public Identifier(String name) {
        if (name == null || !name.matches(IDENT_REGEX))
            throw new WrongIdentifierException(name);
        this.name = name;
    }

    /**
     * Returns the identifier name/string.
     *
     * @return the identifier name/string
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
