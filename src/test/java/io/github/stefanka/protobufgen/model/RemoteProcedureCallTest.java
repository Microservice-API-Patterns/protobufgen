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

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RemoteProcedureCallTest {

    @Test
    public void canCreateRPC() {
        // given
        RemoteProcedureCall.Builder builder = new RemoteProcedureCall.Builder("TestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build());

        // when
        RemoteProcedureCall rpc = builder.build();

        // then
        assertEquals("TestRPC", rpc.getName());
        assertEquals("TestRPC", rpc.getIdentifier().toString());
        assertEquals("InputMessage", rpc.getInput().getName());
        assertEquals("OutputMessage", rpc.getOutput().getName());
        assertFalse(rpc.isInputStreamed());
        assertFalse(rpc.isOutputStreamed());
    }

    @Test
    public void canSetInputAndOutputStreamed() {
        // given
        RemoteProcedureCall.Builder builder = new RemoteProcedureCall.Builder("TestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build());

        // when
        RemoteProcedureCall rpc = builder.withInputAsStream()
                .withOutputAsStream()
                .build();

        // then
        assertEquals("TestRPC", rpc.getName());
        assertEquals("TestRPC", rpc.getIdentifier().toString());
        assertEquals("InputMessage", rpc.getInput().getName());
        assertEquals("OutputMessage", rpc.getOutput().getName());
        assertTrue(rpc.isInputStreamed());
        assertTrue(rpc.isOutputStreamed());
    }

    @Test
    public void canAddComment() {
        // given
        Message input = new Message.Builder("TestInput").build();
        Message output = new Message.Builder("TestOutput").build();
        RemoteProcedureCall.Builder builder = new RemoteProcedureCall.Builder("TestCall", input, output);

        // when
        RemoteProcedureCall rpc = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", rpc.getComment());
    }

    @Test
    public void canDetermineEquality() {
        // given
        RemoteProcedureCall rpc1 = new RemoteProcedureCall.Builder("TestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build()).build();
        RemoteProcedureCall rpc2 = new RemoteProcedureCall.Builder("TestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build()).build();
        RemoteProcedureCall rpc3 = new RemoteProcedureCall.Builder("AnotherTestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build()).build();

        // when
        boolean equal = rpc1.equals(rpc2);
        boolean notEqual = rpc1.equals(rpc3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<RemoteProcedureCall> rpcs = new HashSet<>();
        RemoteProcedureCall rpc1 = new RemoteProcedureCall.Builder("TestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build()).build();
        RemoteProcedureCall rpc2 = new RemoteProcedureCall.Builder("TestRPC",
                new Message.Builder("InputMessage").build(),
                new Message.Builder("OutputMessage").build()).build();

        // when
        rpcs.add(rpc1);
        rpcs.add(rpc2);

        // then
        assertEquals(1, rpcs.size());
    }

}
