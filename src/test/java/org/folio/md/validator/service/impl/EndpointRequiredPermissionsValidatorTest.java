package org.folio.md.validator.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.folio.md.validator.support.TestUtils.readModuleDescriptor;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class EndpointRequiredPermissionsValidatorTest {

  private final Validator validator = new EndpointRequiredPermissionsValidator();

  @ParameterizedTest
  @CsvSource({
    "json/endpoint-required-perms/md-valid.json, 0",
    "json/endpoint-required-perms/md-non-valid-multiple-permissions.json, 1",
    "json/endpoint-required-perms/md-non-valid-protected-by-sub-permission.json, 1",
    "json/endpoint-required-perms/md-non-valid-multiple-and-sub-permissions.json, 2"
  })
  void validate(String filePath, int expectedErrorCount) {
    var moduleDescriptor = readModuleDescriptor(filePath);
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(expectedErrorCount);
  }
}
