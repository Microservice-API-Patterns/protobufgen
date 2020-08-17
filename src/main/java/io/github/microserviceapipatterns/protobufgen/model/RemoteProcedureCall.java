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
 * Represents a protocol buffer RPC call.
 *
 * @author Stefan Kapferer
 */
public class RemoteProcedureCall implements Identifiable {

    private Identifier name;
    private Message input;
    private Message output;
    private boolean streamInput;
    private boolean streamOutput;
    private String comment;

    private RemoteProcedureCall() {
        // use builder to create rpc
    }

    /**
     * Returns the name of the rpc as String.
     *
     * @return the name of the rpc as String
     */
    public String getName() {
        return name.toString();
    }

    /**
     * Returns the identifier (name) of the represented rpc.
     *
     * @return the identifier (name) of the represented rpc
     */
    @Override
    public Identifier getIdentifier() {
        return name;
    }

    /**
     * Returns the message that is passed to the rpc as input/parameter.
     *
     * @return the message that is passed to the rpc as input/parameter
     */
    public Message getInput() {
        return input;
    }

    /**
     * Returns the message that is returned by the rpc.
     *
     * @return the message that is returned by the rpc
     */
    public Message getOutput() {
        return output;
    }

    /**
     * Indicates whether the "stream" flag is set on the input message or not.
     *
     * @return true if the "stream" flag is set on the input message, false otherwise
     */
    public boolean isInputStreamed() {
        return streamInput;
    }

    /**
     * Indicates whether the "stream" flag is set on the output/return message or not.
     *
     * @return true if the "stream" flag is set on the output/return message, false otherwise
     */
    public boolean isOutputStreamed() {
        return streamOutput;
    }

    /**
     * Returns a comment (rendered to the right of the rpc)
     *
     * @return the comment for the represented rpc
     */
    public String getComment() {
        return comment;
    }

    public static class Builder {
        private final Identifier name;
        private final Message input;
        private final Message output;
        private boolean inputStreamed = false;
        private boolean outputStreamed = false;
        private String comment;

        public Builder(String name, Message input, Message output) {
            this.name = new Identifier(name);
            this.input = input;
            this.output = output;
            this.comment = "";
        }

        public Builder withInputAsStream() {
            this.inputStreamed = true;
            return this;
        }

        public Builder withOutputAsStream() {
            this.outputStreamed = true;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public RemoteProcedureCall build() {
            RemoteProcedureCall rpc = new RemoteProcedureCall();
            rpc.name = this.name;
            rpc.input = this.input;
            rpc.output = this.output;
            rpc.streamInput = this.inputStreamed;
            rpc.streamOutput = this.outputStreamed;
            rpc.comment = this.comment;
            return rpc;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteProcedureCall that = (RemoteProcedureCall) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
