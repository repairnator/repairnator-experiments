#!/usr/bin/env bash
# this takes a confidential data bundle in zip format (generated by Google Drive when downloading the folder) and puts 
# content in the appropriate places in our build directories
# warning: do not commit this data accidentally!
set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

zipArchive=$1

unzip -p $zipArchive config_file.txt >dockstore-integration-testing/src/test/resources/config_file.txt
unzip -p $zipArchive config_file2.txt >dockstore-integration-testing/src/test/resources/config_file2.txt
unzip -p $zipArchive migrations.test.confidential1.xml >dockstore-webservice/src/main/resources/migrations.test.confidential1.xml
unzip -p $zipArchive migrations.test.confidential2.xml >dockstore-webservice/src/main/resources/migrations.test.confidential2.xml
unzip -p $zipArchive dockstoreTest.yml >dockstore-integration-testing/src/test/resources/dockstoreTest.yml
