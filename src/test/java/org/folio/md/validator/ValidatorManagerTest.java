package org.folio.md.validator;

import static org.mockito.Mockito.verify;

import java.util.List;
import org.folio.md.validator.model.ModuleDescriptor;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class ValidatorManagerTest {

  @Mock private Validator backendPermissionNamesValidator;
  @Mock private Validator permissionDefinitionValidator;
  @Mock private Validator uniquePermissionForEndpointValidator;
  @Mock private Validator endpointRequiredPermissionsValidator;

  private ValidatorManager validatorManager;

  @BeforeEach
  void setUp() {
    validatorManager = new ValidatorManager(List.of(
      backendPermissionNamesValidator,
      permissionDefinitionValidator,
      uniquePermissionForEndpointValidator,
      endpointRequiredPermissionsValidator
    ));
  }

  @Test
  void validate_shouldCallAllValidators() {
    ValidationContext context = new ValidationContext(new ModuleDescriptor());

    validatorManager.validate(context);

    verify(backendPermissionNamesValidator).validate(context);
    verify(permissionDefinitionValidator).validate(context);
    verify(uniquePermissionForEndpointValidator).validate(context);
    verify(endpointRequiredPermissionsValidator).validate(context);
  }
}
