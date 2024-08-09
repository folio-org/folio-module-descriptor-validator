package org.folio.md.validator;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@UnitTest
class ModuleDescriptorValidatorTest {

  private final ModuleDescriptorValidator mdValidator = new ModuleDescriptorValidator();

  @AfterEach
  void afterEach() {
    cleanProperties(mdValidator);
  }

  @Test
  void execute_positive() {
    mdValidator.failOnInvalidDescriptor = true;
    mdValidator.moduleDescriptroFile = new File("src/test/resources/json/valid-md-template.json");

    assertDoesNotThrow(mdValidator::execute);
  }

  @Test
  void execute_negative_moduleDescriptorFileNotFound() {
    mdValidator.failOnInvalidDescriptor = true;
    mdValidator.moduleDescriptroFile = new File("wrong-file.json");

    assertThatThrownBy(mdValidator::execute)
      .isInstanceOf(MojoExecutionException.class)
      .hasMessage("Module descriptor file is not found");
  }

  @Test
  void execute_positive_moduleDescriptorFileNotFound() {
    mdValidator.failOnInvalidDescriptor = false;
    mdValidator.moduleDescriptroFile = new File("wrong-file.json");

    assertThatNoException().isThrownBy(mdValidator::execute);
  }

  @Test
  void execute_negative_nonParsableModuleDescriptor() {
    mdValidator.failOnInvalidDescriptor = true;
    mdValidator.moduleDescriptroFile = new File("src/test/resources/json/non-parsable-md-template.json");

    assertThatThrownBy(mdValidator::execute)
      .isInstanceOf(MojoExecutionException.class)
      .hasMessage("Failed to parse module descriptor file: src/test/resources/json/non-parsable-md-template.json");
  }

  @Test
  void execute_positive_nonParsableModuleDescriptor() {
    mdValidator.failOnInvalidDescriptor = false;
    mdValidator.moduleDescriptroFile = new File("src/test/resources/json/non-parsable-md-template.json");

    assertThatNoException().isThrownBy(mdValidator::execute);
  }

  private static void cleanProperties(ModuleDescriptorValidator mdValidator) {
    mdValidator.moduleDescriptroFile = null;
    mdValidator.failOnInvalidDescriptor = false;
  }
}
