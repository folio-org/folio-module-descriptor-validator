package org.folio.md.validator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ErrorParameter {

  private String key;
  private String value;
}
