/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.jackrabbit.oak.segment.file;

import org.apache.jackrabbit.oak.segment.spi.persistence.ManifestFile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class LocalManifestFile implements ManifestFile {

    private final File file;

    public LocalManifestFile(File parent, String name) {
        this(new File(parent, name));
    }

    public LocalManifestFile(File file) {
        this.file = file;
    }

    @Override
    public boolean exists() {
        return file.exists();
    }

    @Override
    public Properties load() throws IOException {
        Properties properties = new Properties();
        try (FileReader r = new FileReader(file)) {
            properties.load(r);
        }
        return properties;
    }

    @Override
    public void save(Properties properties) throws IOException {
        properties.store(new FileWriter(file), null);
    }

}
