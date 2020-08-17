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
 * Represents a protocol buffer enum field.
 *
 * @author Stefan Kapferer
 */
public class EnumField implements Comparable<EnumField> {

    private Identifier name;
    private int value;
    private String comment;

    private EnumField() {
        // use builder to create enum field
    }

    /**
     * Returns the name of the enum field as String.
     *
     * @return the name of the enum field as String
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Returns the enum value of the enum field.
     *
     * @return the enum value of the enum field
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns a comment (rendered into the .proto file, besides the enum field definition)
     *
     * @return the comment for the represented enum field
     */
    public String getComment() {
        return comment;
    }

    @Override
    public int compareTo(EnumField enumField) {
        return Integer.valueOf(value).compareTo(enumField.value);
    }

    public static class Builder {
        private final Identifier name;
        private final int value;
        private String comment;

        public Builder(String name, int value) {
            this.name = new Identifier(name);
            this.value = value;
            this.comment = "";
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public EnumField build() {
            EnumField enumField = new EnumField();
            enumField.name = this.name;
            enumField.value = this.value;
            enumField.comment = this.comment;
            return enumField;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnumField enumField = (EnumField) o;
        return name.equals(enumField.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
