#!/bin/sh
# Builds the command line version of the ISA Tools
#

#MVNOPTS="--offline"

#MVN=/usr/local/apache-maven-2.2.1/bin/mvn
#MVN=/usr/bin/mvn

cd ../import_layer
mvn $MVNOPTS -Dmaven.test.skip=true clean package

mvn deploy
