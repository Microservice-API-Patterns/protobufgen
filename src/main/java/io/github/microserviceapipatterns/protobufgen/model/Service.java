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

import io.github.microserviceapipatterns.protobufgen.exception.RemoteProcedureCallAlreadyExistsException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a protocol buffer service.
 *
 * @author Stefan Kapferer
 */
public class Service implements Identifiable {

    private Identifier name;
    private String comment;
    private Set<RemoteProcedureCall> rpcs;

    private Service() {
        // use builder to create service
    }

    /**
     * Returns the name of the service.
     *
     * @return the name of the represented service
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Returns the identifier (name) of the service.
     *
     * @return the identifier of the represented service
     */
    @Override
    public Identifier getIdentifier() {
        return name;
    }

    /**
     * Returns a comment (rendered into the .proto file, above the service definition)
     *
     * @return the comment for the represented service
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns a set with the RPCs of the represented service.
     *
     * @return a set with the RPCs of the represented service
     */
    public Set<RemoteProcedureCall> getRemoteProcedureCalls() {
        return new HashSet<>(rpcs);
    }

    public static class Builder {
        private final Identifier name;
        private String comment;
        private final Set<RemoteProcedureCall> rpcs;

        public Builder(String serviceName) {
            this.name = new Identifier(serviceName);
            this.comment = "";
            this.rpcs = new HashSet<>();
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder withRPC(RemoteProcedureCall rpc) {
            if (this.rpcs.stream().anyMatch(c -> c.getName().equals(rpc.getName())))
                throw new RemoteProcedureCallAlreadyExistsException(rpc.getName());
            this.rpcs.add(rpc);
            return this;
        }

        public Builder withRPC(RemoteProcedureCall.Builder rpcBuilder) {
            return this.withRPC(rpcBuilder.build());
        }

        public Service build() {
            Service service = new Service();
            service.name = this.name;
            service.comment = this.comment;
            service.rpcs = new HashSet<>(this.rpcs);
            return service;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return name.equals(service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
