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

import io.github.microserviceapipatterns.protobufgen.exception.WrongIdentifierException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IdentifierTest {

    @ParameterizedTest
    @ValueSource(strings = {"ident", "Ident", "Ident_1", "Another_Ident1"})
    public void canCreateIdentifier(String identifierName) {
        // given
        Identifier ident;

        // when
        ident = new Identifier(identifierName);

        // then
        assertEquals(identifierName, ident.getName());
        assertEquals(identifierName, ident.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1_ident", "_ident", "@ident", "#ident"})
    public void cannotCreateIdentifierWithWrongPattern(String identifierName) {
        assertThrows(WrongIdentifierException.class, () -> {
            new Identifier(identifierName);
        });
    }

}
