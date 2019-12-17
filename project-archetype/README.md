# Project-Archetype

Maven archetype to facilitate the creation of a preconfigured project compatible with the OrbisGIS platform.
To create a new project execute the following command : 
``` shell script
mvn archetype:generate                              \
    -DarchetypeGroupId=org.orbisgis.orbisparents    \
    -DarchetypeArtifactId=project-archetype         \
    -DarchetypeVersion=1.0.3-SNAPSHOT
```
It will launch the interactive creation of project where you will be asked for the new project `groupId`, `artifactId` and `version`. 