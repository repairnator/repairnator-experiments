/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.toolkit.admin.util

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile
import org.apache.commons.lang3.StringUtils

class AdminUtil {

    protected static String getNiFiVersionFromNar(final File nifiLibDir){

        if(nifiLibDir.isDirectory()){
            File[] files = nifiLibDir.listFiles(new FilenameFilter() {
                @Override
                boolean accept(File dir, String name) {
                    name.startsWith("nifi-framework-nar")
                }
            })

            if(files.length == 1){
                final ZipFile zipFile =  new ZipFile(files[0])
                final ZipArchiveEntry archiveEntry = zipFile.getEntry("META-INF/MANIFEST.MF")
                final InputStream is = zipFile.getInputStream(archiveEntry)
                final Properties manifestProperties = new Properties()
                manifestProperties.load(is)
                String version = manifestProperties.get("Nar-Version")
                zipFile.close()
                return StringUtils.isEmpty(version)? null : version

            }
        }

        null
    }

    protected static String getNiFiVersionFromProperties(final File nifiConfDir) {
        final String nifiPropertiesFileName = nifiConfDir.getAbsolutePath() + File.separator +"nifi.properties"
        final File nifiPropertiesFile = new File(nifiPropertiesFileName)
        final Properties nifiProperties = new Properties()
        nifiProperties.load(new FileInputStream(nifiPropertiesFile))
        nifiProperties.getProperty("nifi.version")
    }

    public static String getNiFiVersion(final File nifiConfDir, final File nifiLibDir){

        String nifiVersion = getNiFiVersionFromProperties(nifiConfDir)
        if(StringUtils.isEmpty(nifiVersion)){
            nifiVersion = getNiFiVersionFromNar(nifiLibDir)
        }
        return nifiVersion.replace("-SNAPSHOT","")

    }

}
