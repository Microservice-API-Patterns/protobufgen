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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FullIdentifierTest {

    @ParameterizedTest
    @ValueSource(strings = {"ident", "ident.sub", "Ident_1", "Another_Ident1.Sub_Ident", "Another_Ident1.Sub_Ident.SubSubIdent"})
    public void canCreateIdentifier(String identifierName) {
        // given
        FullIdentifier ident;

        // when
        ident = new FullIdentifier(identifierName);

        // then
        assertEquals(identifierName, ident.getName());
        assertEquals(identifierName, ident.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1_ident", "_ident", "@ident", "#ident", "ident."})
    public void cannotCreateIdentifierWithWrongPattern(String identifierName) {
        assertThrows(WrongIdentifierException.class, () -> {
            new FullIdentifier(identifierName);
        });
    }

    @Test
    public void canDetermineEquality() {
        // given
        FullIdentifier fullIdentifier1 = new FullIdentifier("root.sub");
        FullIdentifier fullIdentifier2 = new FullIdentifier("root.sub");
        FullIdentifier fullIdentifier3 = new FullIdentifier("root.sub.subsub");

        // when
        boolean equal = fullIdentifier1.equals(fullIdentifier2);
        boolean notEqual = fullIdentifier1.equals(fullIdentifier3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<FullIdentifier> fullIdentifiers = new HashSet<>();

        // when
        fullIdentifiers.add(new FullIdentifier("root.sub"));
        fullIdentifiers.add(new FullIdentifier("root.sub"));

        // then
        assertEquals(1, fullIdentifiers.size());
    }

}
