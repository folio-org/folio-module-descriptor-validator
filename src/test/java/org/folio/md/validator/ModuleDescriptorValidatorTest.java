package org.folio.md.validator;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class ModuleDescriptorValidatorTest {

  private final ModuleDescriptorValidator mdValidator = new ModuleDescriptorValidator();

  @AfterEach
  void afterEach() {
    cleanProperties(mdValidator);
  }

  @ParameterizedTest
  @CsvSource({
    "src/test/resources/json/wrong-file.json, true, Module descriptor file is not found",
    "src/test/resources/json/wrong-file.json, false, null",
    "src/test/resources/json/non-parsable-md-template.json, true, "
      + "Failed to parse module descriptor file: src/test/resources/json/non-parsable-md-template.json",
    "src/test/resources/json/non-parsable-md-template.json, false, null"
  })
  void execute(String filePath, boolean shouldThrow, String message) {
    mdValidator.failOnInvalidDescriptor = shouldThrow;
    mdValidator.moduleDescriptroFile = new File(filePath);

    if (mdValidator.failOnInvalidDescriptor) {
      assertThatThrownBy(mdValidator::execute)
        .isInstanceOf(MojoExecutionException.class)
        .hasMessage(message);
    } else {
      assertThatNoException().isThrownBy(mdValidator::execute);
    }
  }

  @Test
  void execute_positive() {
    mdValidator.failOnInvalidDescriptor = true;
    mdValidator.moduleDescriptroFile = new File("src/test/resources/json/valid-md-template.json");

    assertDoesNotThrow(mdValidator::execute);
  }

  private static void cleanProperties(ModuleDescriptorValidator mdValidator) {
    mdValidator.moduleDescriptroFile = null;
    mdValidator.failOnInvalidDescriptor = false;
  }
}
