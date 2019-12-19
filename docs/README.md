Orbisparents
================

Project holding parent POM for the OrbisGIS project.

### Modules

##### OrbisParent

This parent POM is the base of the different OrbisGIS projects.

##### Project-Archetype

This artifact make easier the creation of a preconfigured project compatible with the OrbisGIS platform.

### Projects guidelines

##### Tests

Tests in the OrbisGIS projects use the framework **Jupiter JUnit 5** along to **Jacoco** for the test coverage.
Each Java class should have its dedicated Java test class. If this Java class goal is to be used in a Groovy script, a 
Groovy test class should be add doing the same test as the Java test class but in Groovy style.