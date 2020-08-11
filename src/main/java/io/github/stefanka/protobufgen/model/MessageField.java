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
 * Represents a protocol buffer message field.
 *
 * @author Stefan Kapferer
 */
public class MessageField implements Comparable<MessageField> {

    private Identifier name;
    private FieldNumber number;
    private String comment;
    private FieldType type;
    private boolean repeated;

    private MessageField() {
        // use builder to create message field
    }

    /**
     * Returns the name of the field.
     *
     * @return the name of the represented field
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Returns the type of the field as string.
     *
     * @return the type of the field as string
     */
    public String getType() {
        return type.getName();
    }

    /**
     * Returns a comment (rendered into the .proto file, besides the field definition)
     *
     * @return the comment for the represented field
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the field number as integer.
     *
     * @return the field number as integer
     */
    public int getNumber() {
        return number.toInt();
    }

    /**
     * Indicates whether the field can be repeated or not.
     *
     * @return boolean that is true when the field can be repeated, false otherwise
     */
    public boolean isRepeated() {
        return repeated;
    }

    @Override
    public int compareTo(MessageField messageField) {
        return number.compareTo(messageField.number);
    }

    public static class Builder {
        private final Identifier name;
        private final FieldNumber number;
        private final FieldType type;
        private String comment;
        private boolean repeated = false;

        public Builder(FieldType type, String fieldName, int fieldNumber) {
            this.name = new Identifier(fieldName);
            this.number = new FieldNumber(fieldNumber);
            this.type = type;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder repeated() {
            this.repeated = true;
            return this;
        }

        public MessageField build() {
            MessageField messageField = new MessageField();
            messageField.name = this.name;
            messageField.comment = this.comment;
            messageField.number = this.number;
            messageField.type = this.type;
            messageField.repeated = this.repeated;
            return messageField;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageField messageField = (MessageField) o;
        return name.equals(messageField.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
