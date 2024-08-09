package org.folio.md.validator.service.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.folio.md.validator.support.TestUtils.readModuleDescriptor;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class PermissionDefinitionValidatorTest {

  private final Validator validator = new PermissionDefinitionValidator();

  @Test
  void validate_positive() {
    var moduleDescriptor = readModuleDescriptor("json/permission-definition/md-valid.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).isEmpty();
  }

  @Test
  void validate_negative_missingPermissionDefinition() {
    var moduleDescriptor =
      readModuleDescriptor("json/permission-definition/md-non-valid-missing-permission-definition.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(1);
  }
}
