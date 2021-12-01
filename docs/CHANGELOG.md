## Changelog for v1.1.0

+ Set `updatePolicy` of `oss-sonatype` snapshot to `true`
+ `CI build` workflow now check the `docs/CHANGELOG.md` update.
+ Improve base configuration of plugins and build.

### Sdk upgrade
+ Switch from java `8` to java `11`
+ Upgrade groovy from `3.0.7` to `3.0.9`

### Dependencies and plugins upgrade
#### Update dependencies :
  + commons-compress : 1.20 -> 1.21
  + commons-io : 2.7 -> 2.11.0
  + groovy : 3.0.7 -> 3.0.9
  + jackson-core : 2.11.0 -> 2.13.0
  + junit : 5.7.0-M1 -> 5.8.1
  + osgi-service-jdbc : 1.0.0 -> 1.0.1
  + picocli : 4.5.2 -> 4.6.2
  + postgis-jdbc : 2.5.0 -> 2021.1.0
  + slf4j : 1.7.30 -> 1.7.32
  + smile : 2.5.3 -> 2.6.0
  + xstream : 1.4.11.1 -> 1.4.18
  + h2 : 1.4.200 -> 2.0.202
  + junit-jupiter-api : 5.8.1 -> 5.8.2
  + junit-jupiter-engine : 5.8.1 -> 5.8.2
  + junit-jupiter-params : 5.8.1 -> 5.8.2
#### Update plugins :
  + gmaven : 1.9.0 -> 1.13.0
  + jacoco : 0.8.6 -> 0.8.7
  + maven-archetype-plugin : 3.1.2 -> 3.2.0
  + maven-bundle : 4.2.1 -> 5.1.2
  + maven-enforcer : 3.0.0-M3 -> 3.0.0
  + maven-javadoc : 3.2.0 -> 3.3.1
  + maven-project-info-reports : 3.1.0 -> 3.1.2
  + maven-release : X -> 3.0.0-M4
  + maven-resources : 3.1.0 -> 3.2.0
  + maven-site : 3.9.0 -> 3.9.1
  + maven-surefire : 3.0.0-M4 -> 3.0.0-M5
  + maven-surefire-report-plugin : 3.0.0-M4 -> 3.0.0-M5
  + version-maven : 2.7 -> 2.8.1