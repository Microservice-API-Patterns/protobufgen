# A Java Library to Serialize Protocol Buffer Files
[![Build Status](https://travis-ci.com/stefan-ka/protobufgen.svg?branch=master)](https://travis-ci.com/stefan-ka/protobufgen) [![codecov](https://codecov.io/gh/stefan-ka/protobufgen/branch/master/graph/badge.svg)](https://codecov.io/gh/stefan-ka/protobufgen) [![Maven Central](https://img.shields.io/maven-central/v/io.github.stefan-ka/protobufgen.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.stefan-ka%22%20AND%20a:%22protobufgen%22) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This is a simple Java library containing a [protocol buffer](https://developers.google.com/protocol-buffers) model and serializer. It can be used in case you want to generate [\*.proto files](https://developers.google.com/protocol-buffers/docs/proto3) within a Java application. We currently support [proto3](https://developers.google.com/protocol-buffers/docs/proto3) only.

## Usage
**Note:** The examples below just give you a glimpse how the API works. Of course, our intention is not writing protocol buffer files (\*.proto) manually in Java, as show below. If that is your goal, just write \*.proto files directly :) We use this library in generators to transform other code/models into protocol buffer specifications.

You find all model classes to represent a proto specification in Java within the `io.github.stefanka.protobufgen.model` package. `io.github.stefanka.protobufgen.model.ProtoSpec` is the root element and with `persistProto(File protoFile)` you can simply serialize the \*.proto file. An example:

```java
ProtoSpec proto = new ProtoSpec.Builder()
        .withMessage(new Message.Builder("SearchRequest")
                .withField(STRING, "query")
                .withField(INT32, "page_number")
                .withField(INT32, "result_per_page"))
        .withEnum(new Enum.Builder("PhoneType")
                .withField("MOBILE")
                .withField("HOME")
                .withField("WORK"))
        .build();
proto.persistProto(new File("demo.proto"));
```

_Note:_ You can also use the `toString()` method on `io.github.stefanka.protobufgen.model.ProtoSpec` and get the serialized model as a String.

The example above generates the following simple \*.proto file:

```proto
syntax = "proto3";

message SearchRequest {
  string query = 1;
  int32 page_number = 2;
  int32 result_per_page = 3;
}

enum PhoneType {
  MOBILE = 0;
  HOME = 1;
  WORK = 2;
}

```

Services are supported as well:

```java
Message request = new Message.Builder("RequestMessage").build();
Message response = new Message.Builder("ResponseMessage").build();

ProtoSpec proto = new ProtoSpec.Builder()
        .withMessage(request)
        .withMessage(response)
        .withService(new Service.Builder("SampleService")
                .withRPC(new RemoteProcedureCall.Builder("sampleRPC", request, response)))
        .build();
proto.persistProto(new File("demo.proto"));
```
```proto
syntax = "proto3";

message RequestMessage {
}

message ResponseMessage {
}

service SampleService {
  rpc sampleRPC(RequestMessage) returns (ResponseMessage);
}
```
### Publication
The library is published to Maven central. Thus, you can easily use it in your project with Gradle or Maven:

**Gradle:**
```gradle
implementation 'io.github.stefan-ka:protobufgen:1.1.0'
```

**Maven:**
```xml
<dependency>
  <groupId>io.github.stefan-ka</groupId>
  <artifactId>protobufgen</artifactId>
  <version>1.1.0</version>
</dependency>
```

## Contributing
Contribution is always welcome! Create an issue if you just want to report a bug or missing feature. In case you want to implement a change/bugfix by yourself, follow these steps:

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request (PR)

## Licence
This library is published under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0). Copyright (c) 2020 Stefan Kapferer
