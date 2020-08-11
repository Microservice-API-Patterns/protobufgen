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
import io.github.stefanka.protobufgen.exception.FieldNumberAlreadyExistsException;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a protocol buffer message.
 *
 * @author Stefan Kapferer
 */
public class Message implements FieldType, Identifiable {

    private Identifier name;
    private String comment;
    private Set<MessageField> fields;

    private Message() {
        // use builder to create message
    }

    /**
     * Returns the name of the message as string.
     *
     * @return the name of the represented message
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Returns the identifier (name) of the message.
     *
     * @return the identifier if the represented message
     */
    @Override
    public Identifier getIdentifier() {
        return name;
    }

    /**
     * Returns a comment (rendered into the .proto file, above the message definition)
     *
     * @return the comment for the represented message
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns a set with the fields of the represented message.
     *
     * @return a set containing the fields of the represented message
     */
    public Set<MessageField> getFields() {
        return new TreeSet<>(fields);
    }

    public static class Builder {
        private final Identifier name;
        private final Set<MessageField> messageFields;
        private String comment;
        private int fieldCounter = 1;

        public Builder(String messageName) {
            this.name = new Identifier(messageName);
            this.comment = "";
            this.messageFields = new TreeSet<>();
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder withField(MessageField messageField) {
            if (this.messageFields.stream().anyMatch(f -> f.getName().equals(messageField.getName())))
                throw new FieldAlreadyExistsException(messageField.getName());
            if (this.messageFields.stream().anyMatch(f -> f.getNumber() == messageField.getNumber()))
                throw new FieldNumberAlreadyExistsException(this.name.toString(), messageField.getNumber());
            this.messageFields.add(messageField);
            this.fieldCounter = messageField.getNumber() + 1;
            return this;
        }

        public Builder withField(MessageField.Builder fieldBuilder) {
            this.withField(fieldBuilder.build());
            return this;
        }

        public Builder withField(FieldType type, String name) {
            this.withField(new MessageField.Builder(type, name, fieldCounter).build());
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.name = this.name;
            message.fields = new TreeSet<>(this.messageFields);
            message.comment = this.comment;
            return message;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return name.equals(message.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
