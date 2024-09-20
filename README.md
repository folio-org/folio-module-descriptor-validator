# folio-module-descriptor-validator

Copyright (C) 2023-2024 The Open Library Foundation

## Description
Maven plugin for validating FOLIO module descriptors. We made it as simple as possible, so it can be easily integrated into any FOLIO module project.
It checks the module descriptor template placed in FOLIO modules for issues and ensures it adheres to FOLIO common approach.

## Table of content:
- [Features](#features)
- [Usage](#usage)
  - [Non-project usage](#non-project-usage)
- [Validation rules](#validation-rules)
- [Helpful scripts](#helpful-scripts)

## Features
- Validation: Automatically checks module descriptor for common issues and ensures it adheres to FOLIO standards.
- Detailed Reports: Provides comprehensive reports on validation results, highlighting any issues found.
- Fail Fast: Stops the build process if any issues are detected, ensuring that only valid module descriptors are deployed. (optional)
- Non-project usage: Can be used as a standalone tool to validate module descriptors.

## Usage
Add the FOLIO plugin repository to your project's `pom.xml` file:

```xml
  <pluginRepositories>
    <pluginRepository>
      <id>folio-nexus</id>
      <name>FOLIO Maven repository</name>
      <url>https://repository.folio.org/repository/maven-folio</url>
    </pluginRepository>
  </pluginRepositories>
```

Add the following plugin configuration to your project's `pom.xml` file (inside `<build><plugins>` section) to enable validation of the module descriptor during the build process:

```xml
      <plugin>
        <groupId>org.folio</groupId>
        <artifactId>folio-module-descriptor-validator</artifactId>
        <version>${folio-module-descriptor-validator.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>validate</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```
This is the simplest configuration that will validate the module descriptor during the build process.
By default, the plugin will fail the build if any issues are detected. Default path to module descriptor: `${project.basedir}/descriptors/ModuleDescriptor-template.json`.

To disable the fail-fast mode or add custom path to module descriptor file, add the following configuration:

```xml
      <plugin>
        <groupId>org.folio</groupId>
        <artifactId>folio-module-descriptor-validator</artifactId>
        <version>${folio-module-descriptor-validator.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>validate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <failOnInvalidDescriptor>false</failOnInvalidDescriptor>
          <moduleDescriptorFile>${project.basedir}/path/to/module-descriptor.json</moduleDescriptorFile>
        </configuration>
      </plugin>
```

Example 'pom.xml' file:
```xml
<project>
  ...
  <properties>
    ...
    <folio-module-descriptor-validator.version>1.0.0</folio-module-descriptor-validator.version>
    ...
  </properties>
  ...
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>org.folio</groupId>
        <artifactId>folio-module-descriptor-validator</artifactId>
        <version>${folio-module-descriptor-validator.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>validate</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
  </build>
  ...
  <pluginRepositories>
    <pluginRepository>
      <id>folio-nexus</id>
      <name>FOLIO Maven repository</name>
      <url>https://repository.folio.org/repository/maven-folio</url>
    </pluginRepository>
  </pluginRepositories>
  ...
</project>
```

### Non-project usage
If the project has the `<pluginRepository>` dependency shown above, you can use the following command to validate without adding the `<plugin>` entry to `pom.xml`:

```shell
mvn org.folio:folio-module-descriptor-validator:${folio-module-descriptor-validator.version}:validate -DmoduleDescriptorFile=/path/to/module-descriptor.json
```
Please replace `${folio-module-descriptor-validator.version}` with the latest version of the plugin, and `/path/to/module-descriptor.json` with the path to the module descriptor file if needed. By default, the plugin will look for the module descriptor in `${project.basedir}/descriptors/ModuleDescriptor-template.json`.

Example:
```shell
mvn org.folio:folio-module-descriptor-validator:1.0.0:validate
```

NOTE: Non-project use is needed for local tests, using scripts, or creating reports. It is also suitable for validating UI module descriptors. For everyday work with modules that contain module descriptor, it is recommended to add a plugin to the `pom.xml` to track and prevent issues with MD.

## Validation rules
The plugin checks the module descriptor for the following issues:
- Permission name: Checks if the permission name is in the correct format. More details [here](https://folio-org.atlassian.net/wiki/spaces/FOLIJET/pages/156368925/Permissions+naming+convention).
- Permission uniqueness: Check if the permission protects the only one endpoint.
- Endpoint required permissions: `permissionsRequired` array should contain no more than one permission. Also `permissionsRequired` shouldn't contain permission set.
- Permission definition: Checks if the permission is defined in the `permissionSets` section.

## Helpful scripts
- [module descriptor batch validator](scripts/module-descriptor-batch-validator/README.md)

## Issue tracker

See project [MVNMDVAL](https://folio-org.atlassian.net/browse/MVNMDVAL)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).
