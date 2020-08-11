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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageFieldTypeTest {

    @ParameterizedTest
    @MethodSource("simpleTypesTestParams")
    public void canCreateSimpleType(FieldType type, String expectedStringRepresentation) {
        assertEquals(expectedStringRepresentation, type.getName());
    }

    private static Stream simpleTypesTestParams() {
        return Stream.of(
                Arguments.of(SimpleFieldType.DOUBLE, "double"),
                Arguments.of(SimpleFieldType.FLOAT, "float"),
                Arguments.of(SimpleFieldType.INT32, "int32"),
                Arguments.of(SimpleFieldType.INT64, "int64"),
                Arguments.of(SimpleFieldType.UINT32, "uint32"),
                Arguments.of(SimpleFieldType.UINT64, "uint64"),
                Arguments.of(SimpleFieldType.SINT32, "sint32"),
                Arguments.of(SimpleFieldType.SINT64, "sint64"),
                Arguments.of(SimpleFieldType.FIXED32, "fixed32"),
                Arguments.of(SimpleFieldType.FIXED64, "fixed64"),
                Arguments.of(SimpleFieldType.SFIXED32, "sfixed32"),
                Arguments.of(SimpleFieldType.SFIXED64, "sfixed64"),
                Arguments.of(SimpleFieldType.BOOL, "bool"),
                Arguments.of(SimpleFieldType.STRING, "string"),
                Arguments.of(SimpleFieldType.BYTES, "bytes")
        );
    }


}
