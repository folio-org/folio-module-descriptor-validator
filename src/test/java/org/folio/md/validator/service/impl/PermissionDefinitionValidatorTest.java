package org.folio.md.validator.service.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.folio.md.validator.support.TestUtils.readModuleDescriptor;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class PermissionDefinitionValidatorTest {

  @ParameterizedTest
  @CsvSource(textBlock = """
      md-valid.json, 0
      md-mod-pubsub-names.json, 0
      md-non-valid-missing-permission-definition.json, 1
      """)
  void validate(String file, int expectedErrorCount) {
    var ctx = new ValidationContext(readModuleDescriptor("json/permission-definition/" + file));
    new PermissionDefinitionValidator().validate(ctx);
    assertThat(ctx.getErrorParameters()).hasSize(expectedErrorCount);
  }
}
