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

import io.github.microserviceapipatterns.protobufgen.exception.FieldAlreadyExistsException;
import io.github.microserviceapipatterns.protobufgen.exception.FieldNumberAlreadyExistsException;
import io.github.microserviceapipatterns.protobufgen.exception.NestedMessageAlreadyExistsException;

import java.util.*;

/**
 * Represents a protocol buffer message.
 *
 * @author Stefan Kapferer
 */
public class Message implements FieldType, Identifiable {

    private Identifier name;
    private String comment;
    private Set<MessageField> fields;
    private List<Message> nestedMessages;
    private Message parent;

    private Message() {
        // use builder to create message
    }

    /**
     * Returns the full name of the message (including parents, if it is a nested message) as string.
     *
     * @return the full name of the represented message
     */
    public String getName() {
        return isNestedMessage() ? parent.getName() + "." + name.toString() : name.toString();
    }

    /**
     * Returns the simple name of the message (without parents, if it is a nested message) as string
     *
     * @return the simple name of the represented message
     */
    public String getSimpleName() {
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

    /**
     * Returns the nested messages inside the represented message.
     *
     * @return a list with the nested messages inside the represented message
     */
    public List<Message> getNestedMessages() {
        return new LinkedList<>(nestedMessages);
    }

    /**
     * Indicates whether the represented message is a nested message or not.
     *
     * @return true if the message is a nested message, false otherwise
     */
    public boolean isNestedMessage() {
        return parent != null;
    }

    /**
     * Gives the parent message, in case the represented message is a nested message, null otherwise.
     *
     * @return the parent message of the nested message, or null if it is not a nested message
     */
    public Message getParent() {
        return parent;
    }

    /**
     * Changes the parent message of the represented message.
     *
     * @param parent the new parent message
     */
    protected void setParent(Message parent) {
        this.parent = parent;
    }

    public static class Builder {
        private final Identifier name;
        private final Set<MessageField> messageFields;
        private final List<Message> nestedMessages;
        private String comment;
        private int fieldCounter = 1;
        private Message parent;

        public Builder(String messageName) {
            this.name = new Identifier(messageName);
            this.comment = "";
            this.messageFields = new TreeSet<>();
            this.nestedMessages = new LinkedList<>();
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

        public Builder withNestedMessage(Message message) {
            if (this.nestedMessages.stream().anyMatch(m -> m.getName().equals(message.getName())))
                throw new NestedMessageAlreadyExistsException(message.getName());
            this.nestedMessages.add(message);
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.name = this.name;
            message.fields = new TreeSet<>(this.messageFields);
            message.nestedMessages = new LinkedList<>(this.nestedMessages);
            for (Message nested : nestedMessages) {
                nested.setParent(message);
            }
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
