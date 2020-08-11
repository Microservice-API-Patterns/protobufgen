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

import io.github.stefanka.protobufgen.exception.FieldAlreadyExistsException;
import io.github.stefanka.protobufgen.exception.FirstEnumFieldZeroValueException;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a protocol buffer enum.
 *
 * @author Stefan Kapferer
 */
public class Enum implements FieldType, Identifiable {

    private Identifier name;
    private Set<EnumField> fields;
    private String comment;

    private Enum() {
        // use builder to create enum
    }

    /**
     * Returns the name of the enum as String.
     *
     * @return the name of the enum as String
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Returns the identifier (name) of the enum.
     *
     * @return the identifier of the represented enum
     */
    @Override
    public Identifier getIdentifier() {
        return name;
    }

    /**
     * Returns a set with the fields of the represented enum.
     *
     * @return a set with the fields of the represented enum
     */
    public Set<EnumField> getFields() {
        return new TreeSet<>(fields);
    }

    /**
     * Returns a comment (rendered into the .proto file, above the enum definition)
     *
     * @return the comment for the represented enum
     */
    public String getComment() {
        return comment;
    }

    public static class Builder {
        private final Identifier name;
        private final Set<EnumField> fields;
        private String comment;
        private int valueCounter = 0;

        public Builder(String name) {
            this.name = new Identifier(name);
            this.comment = "";
            this.fields = new TreeSet<>();
        }

        public Builder withField(EnumField enumField) {
            if (this.fields.isEmpty() && enumField.getValue() != 0)
                throw new FirstEnumFieldZeroValueException();
            if (this.fields.stream().anyMatch(f -> f.getName().equals(enumField.getName())))
                throw new FieldAlreadyExistsException(enumField.getName());
            this.fields.add(enumField);
            this.valueCounter = enumField.getValue() + 1;
            return this;
        }

        public Builder withField(EnumField.Builder enumFieldBuilder) {
            this.withField(enumFieldBuilder.build());
            return this;
        }

        public Builder withField(String fieldName) {
            this.withField(new EnumField.Builder(fieldName, valueCounter).build());
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Enum build() {
            Enum enumm = new Enum();
            enumm.name = this.name;
            enumm.comment = this.comment;
            enumm.fields = new TreeSet<>(this.fields);
            return enumm;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enum anEnum = (Enum) o;
        return name.equals(anEnum.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
