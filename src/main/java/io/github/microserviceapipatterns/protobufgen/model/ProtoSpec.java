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

import io.github.microserviceapipatterns.protobufgen.exception.RootElementAlreadyExistsException;
import io.github.microserviceapipatterns.protobufgen.serializer.ProtoSpecSerializer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static io.github.microserviceapipatterns.protobufgen.model.AnyType.ANY_TYPE_IMPORT;

/**
 * Represents a protocol buffers specification; one single *.proto file.
 *
 * @author Stefan Kapferer
 */
public class ProtoSpec {

    private String syntax;
    private List<Message> messages;
    private List<Enum> enums;
    private List<Service> services;
    private List<ImportStatement> importStatements;
    private FullIdentifier packageDef;
    private String comment;

    private ProtoSpecSerializer serializer;

    private ProtoSpec() {
        // use builder to create proto spec
        this.serializer = new ProtoSpecSerializer();
    }

    /**
     * Returns the messages contained by this proto spec.
     *
     * @return the messages contained by the represented proto spec.
     */
    public List<Message> getMessages() {
        return new LinkedList<>(messages);
    }

    /**
     * Returns the enums contained by this proto spec.
     *
     * @return the enums contained by the represented proto spec.
     */
    public List<Enum> getEnums() {
        return new LinkedList<>(enums);
    }

    /**
     * Returns the services contained by this proto spec.
     *
     * @return the services contained by the represented proto spec.
     */
    public List<Service> getServices() {
        return new LinkedList<>(services);
    }

    /**
     * Returns the proto syntax version (currently only 3 supported!)
     *
     * @return returns the proto syntax version as string
     */
    public String getSyntax() {
        return syntax;
    }

    public List<ImportStatement> getImportStatements() {
        return new LinkedList<>(importStatements);
    }

    /**
     * Returns the package as string.
     *
     * @return the package name as string
     */
    public String getPackage() {
        return packageDef != null ? packageDef.toString() : "";
    }

    /**
     * Serializes the proto spec. into the *.proto file format and returns it as a String.
     *
     * @return proto file format as String
     */
    @Override
    public String toString() {
        return serializer.serialize(this);
    }

    /**
     * Persists the proto specification into a file. Use *.proto as file extension of the given file.
     *
     * @param protoFile the file where proto shall be persisted. Use *.proto as file extension of the given file.
     * @throws IOException
     */
    public void persistProto(File protoFile) throws IOException {
        serializer.writeToFile(this, protoFile);
    }

    /**
     * Returns the comment of the represented proto spec (rendered at the top of the *.proto file).
     *
     * @return the comment as a string
     */
    public String getComment() {
        return comment;
    }

    public static class Builder {
        private final List<Message> messages;
        private final List<Enum> enums;
        private final List<Service> services;
        private final Set<Identifiable> allIdentifiableObjects;
        private final List<ImportStatement> importStatements;
        private FullIdentifier packageDef;
        private String comment;

        public Builder() {
            this.messages = new LinkedList<>();
            this.enums = new LinkedList<>();
            this.services = new LinkedList<>();
            this.allIdentifiableObjects = new HashSet<>();
            this.importStatements = new LinkedList<>();
            this.comment = "";
        }

        public Builder withMessage(Message message) {
            addIdentifiable(message);
            this.messages.add(message);
            return this;
        }

        public Builder withMessage(Message.Builder messageBuilder) {
            return this.withMessage(messageBuilder.build());
        }

        public Builder withEnum(Enum enumm) {
            addIdentifiable(enumm);
            this.enums.add(enumm);
            return this;
        }

        public Builder withEnum(Enum.Builder enumBuilder) {
            return this.withEnum(enumBuilder.build());
        }

        public Builder withService(Service service) {
            addIdentifiable(service);
            this.services.add(service);
            return this;
        }

        public Builder withService(Service.Builder serviceBuilder) {
            return this.withService(serviceBuilder.build());
        }

        public Builder withImport(ImportStatement importStatement) {
            this.importStatements.add(importStatement);
            return this;
        }

        public Builder withImport(String fileName) {
            return this.withImport(new ImportStatement(fileName));
        }

        public Builder withImport(String fileName, boolean publicImport) {
            return this.withImport(new ImportStatement(fileName, publicImport));
        }

        public Builder withPackage(FullIdentifier packageDef) {
            this.packageDef = packageDef;
            return this;
        }

        public Builder withPackage(String packageName) {
            return this.withPackage(new FullIdentifier(packageName));
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        private void addIdentifiable(Identifiable identifiable) {
            if (this.allIdentifiableObjects.stream().anyMatch(m -> m.getIdentifier().equals(identifiable.getIdentifier())))
                throw new RootElementAlreadyExistsException(identifiable.getIdentifier().toString());
            this.allIdentifiableObjects.add(identifiable);
        }

        public ProtoSpec build() {
            ProtoSpec spec = new ProtoSpec();
            spec.syntax = "proto3"; // currently we only support proto3
            spec.messages = new LinkedList<>(this.messages);
            spec.enums = new LinkedList<>(this.enums);
            spec.services = new LinkedList<>(this.services);
            spec.packageDef = this.packageDef;
            spec.comment = this.comment;
            if (containsAnyType())
                addAnyTypeImport();
            spec.importStatements = new LinkedList<>(this.importStatements);
            return spec;
        }

        private boolean containsAnyType() {
            for (Message message : this.messages) {
                if (containsAnyType(message))
                    return true;
            }
            return false;
        }

        private boolean containsAnyType(Message message) {
            for (MessageField field : message.getFields()) {
                if (field.getType().equals(AnyType.ANY_TYPE_NAME))
                    return true;
            }
            for (Message nested : message.getNestedMessages()) {
                if (containsAnyType(nested))
                    return true;
            }
            return false;
        }

        private void addAnyTypeImport() {
            if (!this.importStatements.stream().anyMatch(i -> i.getFileName().equals(ANY_TYPE_IMPORT)))
                this.importStatements.add(new ImportStatement(ANY_TYPE_IMPORT));
        }

    }
}
