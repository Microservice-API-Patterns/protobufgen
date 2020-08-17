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
package io.github.microserviceapipatterns.protobufgen;

import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

public abstract class AbstractProtoIntegTest {

    private static final String INTEG_TESTS_FILE_DIR = "src/test/resources/integTests";
    private static final String TEST_DATA_DIR = "test-data";

    @BeforeEach
    protected void prepare() throws IOException {
        File testDir = new File(TEST_DATA_DIR);
        if (testDir.exists()) {
            Files.walk(Paths.get(testDir.toURI()))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        testDir.mkdir();
    }

    protected String readIntegTestFile(String fileName) throws IOException {
        return readFileFromDir(fileName, new File(INTEG_TESTS_FILE_DIR));
    }

    protected String readTestOutputFile(String fileName) throws IOException {
        return readFileFromDir(fileName, new File(TEST_DATA_DIR));
    }

    private String readFileFromDir(String fileName, File dir) throws IOException {
        File file = new File(dir, fileName);
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append(System.lineSeparator()));
        return contentBuilder.toString();
    }

    protected File getTestDir() {
        return new File(TEST_DATA_DIR);
    }

}
