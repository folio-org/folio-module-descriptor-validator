package org.folio.md.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.folio.md.validator.support.TestUtils.readModuleDescriptor;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;
import org.folio.md.validator.service.impl.EndpointRequiredPermissionsValidator;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class EndpointRequiredPermissionsValidatorTest {

  private final Validator validator = new EndpointRequiredPermissionsValidator();

  @Test
  void validate_positive() {
    var moduleDescriptor = readModuleDescriptor("json/endpoint-required-perms/md-valid.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(0);
  }

  @Test
  void validate_negative_multiplePermissions() {
    var moduleDescriptor = readModuleDescriptor("json/endpoint-required-perms/md-non-valid-multiple-permissions.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(1);
  }

  @Test
  void validate_negative_protectedBySubPermission() {
    var moduleDescriptor = readModuleDescriptor(
      "json/endpoint-required-perms/md-non-valid-protected-by-sub-permission.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(1);
  }

  @Test
  void validate_negative_protectedByMultipleAndSubPermissions() {
    var moduleDescriptor = readModuleDescriptor(
      "json/endpoint-required-perms/md-non-valid-multiple-and-sub-permissions.json");
    var ctx = new ValidationContext(moduleDescriptor);

    validator.validate(ctx);

    assertThat(ctx.getErrorParameters()).hasSize(2);
  }
}
