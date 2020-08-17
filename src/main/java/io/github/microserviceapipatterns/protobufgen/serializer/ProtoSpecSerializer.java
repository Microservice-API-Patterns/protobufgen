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
package io.github.microserviceapipatterns.protobufgen.serializer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.github.microserviceapipatterns.protobufgen.exception.ProtoSerializationException;
import io.github.microserviceapipatterns.protobufgen.model.ProtoSpec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import static freemarker.template.Configuration.VERSION_2_3_30;

/**
 * Class that allows to serialize a ProtoSpec object into a *.proto file.
 *
 * @author Stefan Kapferer
 */
public class ProtoSpecSerializer {

    public String serialize(ProtoSpec spec) {
        Configuration cfg = new Configuration(VERSION_2_3_30);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "template");
        cfg.setDefaultEncoding("UTF-8");

        try {
            Template template = cfg.getTemplate("proto.ftl");
            StringWriter writer = new StringWriter();
            template.process(spec, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new ProtoSerializationException(e);
        }
    }

    public void writeToFile(ProtoSpec spec, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(serialize(spec));
        writer.close();
    }

}
