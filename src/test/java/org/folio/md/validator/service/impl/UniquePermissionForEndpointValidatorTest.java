package org.folio.md.validator.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.folio.md.validator.support.TestUtils.readModuleDescriptor;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class UniquePermissionForEndpointValidatorTest {

  private final Validator validator = new UniquePermissionForEndpointValidator();

  @Test
  void validate_positive() {
    var moduleDescriptor = readModuleDescriptor("json/unique-permission/md-valid.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).isEmpty();
  }

  @Test
  void validate_negative() {
    var moduleDescriptor = readModuleDescriptor("json/unique-permission/md-non-valid.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(1);
  }
}
