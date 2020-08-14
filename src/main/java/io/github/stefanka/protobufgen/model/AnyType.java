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

import java.util.Objects;

/**
 * Represents Google's Any type for message fields.
 *
 * @author Stefan Kapferer
 */
public class AnyType implements FieldType {

    public static final String ANY_TYPE_NAME = "google.protobuf.Any";
    public static final String ANY_TYPE_IMPORT = "google/protobuf/any.proto";

    @Override
    public String getName() {
        return ANY_TYPE_NAME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ANY_TYPE_NAME);
    }

    @Override
    public String toString() {
        return ANY_TYPE_NAME;
    }
}
