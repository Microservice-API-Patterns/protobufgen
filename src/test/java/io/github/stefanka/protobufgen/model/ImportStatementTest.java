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

import static org.junit.jupiter.api.Assertions.*;

public class ImportStatementTest {

    @Test
    public void canCreateImportStatement() {
        // given
        ImportStatement importStatement;

        // when
        importStatement = new ImportStatement("protos/test.proto");

        // then
        assertEquals("protos/test.proto", importStatement.getFileName());
        assertFalse(importStatement.isPublic());
    }

    @Test
    public void canCreatePublicImport() {
        // given
        ImportStatement importStatement;

        // when
        importStatement = new ImportStatement("protos/test.proto", true);

        // then
        assertEquals("protos/test.proto", importStatement.getFileName());
        assertTrue(importStatement.isPublic());
    }

    @Test
    public void canDetermineEquality() {
        // given
        ImportStatement statement1 = new ImportStatement("protos/Test.proto");
        ImportStatement statement2 = new ImportStatement("protos/Test.proto");
        ImportStatement statement3 = new ImportStatement("protos/Test2.proto");

        // when
        boolean equal = statement1.equals(statement2);
        boolean notEqual = statement1.equals(statement3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<ImportStatement> importStatements = new HashSet<>();

        // when
        importStatements.add(new ImportStatement("protos/Test.proto"));
        importStatements.add(new ImportStatement("protos/Test.proto"));

        // then
        assertEquals(1, importStatements.size());
    }

}
