package org.folio.md.validator;

import java.util.ArrayList;
import java.util.List;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;
import org.folio.md.validator.service.impl.BackendPermissionNamesValidator;
import org.folio.md.validator.service.impl.EndpointRequiredPermissionsValidator;
import org.folio.md.validator.service.impl.PermissionDefinitionValidator;
import org.folio.md.validator.service.impl.UniquePermissionForEndpointValidator;

public class ValidatorManager {

  private final List<Validator> validators;

  public ValidatorManager() {
    this.validators = new ArrayList<>();
    validators.add(new BackendPermissionNamesValidator());
    validators.add(new PermissionDefinitionValidator());
    validators.add(new UniquePermissionForEndpointValidator());
    validators.add(new EndpointRequiredPermissionsValidator());
  }

  public ValidationContext validate(ValidationContext context) {
    for (var validator : validators) {
      validator.validate(context);
    }

    return context;
  }
}
