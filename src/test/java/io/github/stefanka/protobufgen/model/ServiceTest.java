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

import io.github.stefanka.protobufgen.exception.RemoteProcedureCallAlreadyExistsException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    @Test
    public void canCreateService() {
        // given
        Service.Builder builder = new Service.Builder("TestService");

        // when
        Service service = builder.build();

        // then
        assertEquals("TestService", service.getName());
        assertEquals("TestService", service.getIdentifier().toString());
    }

    @Test
    public void canAddComment() {
        // given
        Service.Builder builder = new Service.Builder("TestService");

        // when
        Service service = builder.withComment("test-comment").build();

        // then
        assertEquals("test-comment", service.getComment());
    }

    @Test
    public void canAddRPC() {
        // given
        Service.Builder builder = new Service.Builder("TestService");

        // when
        Message input = new Message.Builder("InputMessage").build();
        Message output = new Message.Builder("OutputMessage").build();
        RemoteProcedureCall.Builder rpc = new RemoteProcedureCall.Builder("TestCall", input, output);
        builder.withRPC(rpc);
        Service service = builder.build();

        // then
        assertEquals(1, service.getRemoteProcedureCalls().size());
        assertEquals("TestCall", service.getRemoteProcedureCalls().iterator().next().getName());
    }

    @Test
    public void cannotAddRPCWithNameThatAlreadyExists() {
        // given
        Service.Builder builder = new Service.Builder("TestService");
        Message input = new Message.Builder("InputMessage").build();
        Message output = new Message.Builder("OutputMessage").build();
        RemoteProcedureCall.Builder rpc = new RemoteProcedureCall.Builder("TestCall", input, output);

        // when
        builder.withRPC(rpc);

        // then
        assertThrows(RemoteProcedureCallAlreadyExistsException.class, () -> {
            builder.withRPC(new RemoteProcedureCall.Builder("TestCall", input, output));
        });
    }

    @Test
    public void canDetermineEquality() {
        // given
        Service service1 = new Service.Builder("TestService").build();
        Service service2 = new Service.Builder("TestService").build();
        Service service3 = new Service.Builder("AnotherTestService").build();

        // when
        boolean equal = service1.equals(service2);
        boolean notEqual = service1.equals(service3);

        // then
        assertTrue(equal);
        assertFalse(notEqual);
    }

    @Test
    public void canCalculateHashCode() {
        // given
        Set<Service> services = new HashSet<>();

        // when
        services.add(new Service.Builder("TestService").build());
        services.add(new Service.Builder("TestService").build());

        // then
        assertEquals(1, services.size());
    }

}
