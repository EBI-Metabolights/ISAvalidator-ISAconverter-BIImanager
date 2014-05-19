#!/bin/sh
# Builds the command line version of the ISA Tools
#

#MVNOPTS="--offline"

ISAversion=1.6.8
MVN=/usr/local/apache-maven-2.2.1/bin/mvn
#MVN=/usr/bin/mvn

cd ../import_layer
$MVN $MVNOPTS -Ptools,build_cmd_deps -Dmaven.test.skip=true clean package

# This is necessary due to some wrong packaging in one of our dependencies
# Final jar won't work unless you remove this silly file
#
zip target/import_layer_deps_${ISAversion}.jar -d '\*.class'

$MVN install:install-file -Dfile=target/import_layer_deps_${ISAversion}.jar -DgroupId=org.isatools -DartifactId=import_layer_MTBLS -Dversion=${ISAversion}-SNAPSHOT -Dpackaging=jar -DuniqueVersion=false
$MVN deploy:deploy-file -Dfile=target/import_layer_deps_${ISAversion}.jar -DgroupId=org.isatools -DartifactId=import_layer_MTBLS -Dversion=${ISAversion}-SNAPSHOT -Dpackaging=jar -DrepositoryId=chebi-repo2 -Durl=scp://prod.ebi.ac.uk/ebi/sp/pro1/chebi/maven2_repo -DuniqueVersion=false

# ssh the file to the cluster:
# mvn deploy:deploy-file -Dfile=ISAcreator-1.7.3-SNAPSHOT.jar -DgroupId=org.isatools -DartifactId=ISAcreator -Dversion=1.7.3-SNAPSHOT -Dpackaging=jar -DrepositoryId=ebi-001.ebi.ac.uk -Durl=file:/ebi/sp/pro1/chebi/maven2_repo
