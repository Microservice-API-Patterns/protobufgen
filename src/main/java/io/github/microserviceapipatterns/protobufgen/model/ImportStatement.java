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

import java.util.Objects;

/**
 * Represents an import statement to import other proto files.
 *
 * @author Stefan Kapferer
 */
public class ImportStatement {

    private String fileName;
    private boolean publicImport;

    public ImportStatement(String fileName) {
        this.fileName = fileName;
        this.publicImport = false;
    }

    public ImportStatement(String fileName, boolean publicImport) {
        this.fileName = fileName;
        this.publicImport = publicImport;
    }

    /**
     * Returns the filename of the imported proto file.
     *
     * @return the filename of the imported proto file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Indicates whether the import is "public" or not.
     *
     * @return true in case the import is public, false otherwise
     */
    public boolean isPublic() {
        return publicImport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportStatement that = (ImportStatement) o;
        return fileName.equals(that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }
}
