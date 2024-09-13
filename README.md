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
### Non-project usage
To validate module descriptor without integrating it into the project, you can use the following command:

```shell
mvn org.folio:folio-module-descriptor-validator:${folio-module-descriptor-validator.version}:validate -DmoduleDescriptorFile=/path/to/module-descriptor.json
```
## Validation rules
The plugin checks the module descriptor for the following issues:
- Permission name: Checks if the permission name is in the correct format. More details [here](https://folio-org.atlassian.net/wiki/spaces/FOLIJET/pages/156368925/Permissions+naming+convention).
- Permission uniqueness: Check if the permission protects the only one endpoint.
- Endpoint required permissions: `permissionsRequired` array should contain no more than one permission. Also `permissionsRequired` shouldn't contain permission set.
- Permission definition: Checks if the permission is defined in the `permissionSets` section.

## Helpful scripts
- [module descriptor batch validator](scripts/module-descriptor-batch-validator/README.md)
