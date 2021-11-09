# Orbisparents
[![GitHub](https://img.shields.io/github/license/orbisgis/orbisparents.svg)](https://github.com/orbisgis/orbisparents/blob/master/docs/LICENSE.md) 
[![Build Stat](https://img.shields.io/jenkins/s/http/jenkins-ng.orbisgis.org/job/orbisparents.svg)](http://jenkins-ng.orbisgis.org/job/orbisparents) 
[![Build Test](https://img.shields.io/jenkins/t/http/jenkins-ng.orbisgis.org/job/orbisparents.svg)](https://jenkins-ng.orbisgis.org/job/orbisparents/test_results_analyzer/) 
[![codecov](https://img.shields.io/codecov/c/github/orbisgis/orbisparents.svg)](https://codecov.io/gh/orbisgis/orbisparents) 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/281e5a0861ac4b029db7b33157cebe99)](https://www.codacy.com/gh/orbisgis/orbisparents?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=orbisgis/orbisparents&amp;utm_campaign=Badge_Grade)

This project is the root of all the OrbisGIS projects.
It contains a parent pom with the dependency and plugin management and a basic `site.xml` file used for the reporting
site generation.

### Building

To build with plugin/dependency version check, use the maven goal
`versions:display-plugin-updates versions:display-dependency-updates`.
To generate the reporting site, use the maven goal `site:effective-site site site:deploy`.

### Versions

The version number should follow this pattern : `Major`.`Minor`.`Revision[-Qualifier]`

- `Revision` is changed after each dependency/plugin/information update/addition.
- `Minor` is changed after each dependency/plugin/information removal or replacement.
- `Major` is changed after each structural change.
- Optional `Qualifier` which can take one of these values :
    - `SNAPSHOT` for in-dev version.
    - `RC-X` for release candidate version. All of its dependencies ou plugin should be at the RC or Release state.
    - Nothing for release version. All of its dependencies ou plugin should be at the Release state.


### Modules
##### Project-Archetype

This artifact make easier the creation of a preconfigured project compatible with the OrbisGIS platform.

##### Commons

Common classes, annotations and utilities for the OrbisGIS projects are grouped in this module.

### Projects guidelines

For more information, see [CONTRIBUTING.md](CONTRIBUTING.md).