package org.folio.md.validator;

import java.util.List;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;

public class ValidatorManager {

  private final List<Validator> validators;

  public ValidatorManager(List<Validator> validators) {
    this.validators = validators;
  }

  public ValidationContext validate(ValidationContext context) {
    for (var validator : validators) {
      validator.validate(context);
    }

    return context;
  }
}
