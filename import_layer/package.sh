#!/bin/sh
# Builds the command line version of the ISA Tools
# 

#MVNOPTS="--offline"

cd ../import_layer
mvn $MVNOPTS -Ptools,build_cmd_deps -Dmaven.test.skip=true clean package

# This is necessary due to some wrong packaging in one of our dependencies
# Final jar won't work unless you remove this silly file
#
zip target/import_layer_deps_1.3.2.28-P.jar -d '\*.class'

mvn $MVNOPTS -Ptools,build_cmd -Dmaven.test.skip=true package
