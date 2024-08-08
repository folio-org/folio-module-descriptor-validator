package org.folio.md.validator.service;

import org.folio.md.validator.model.ValidationContext;

public interface Validator {

  void validate(ValidationContext ctx);
}
