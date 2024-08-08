package org.folio.md.validator.model;

import java.util.List;
import lombok.Data;

@Data
public class RoutingEntry {

  private String pathPattern;
  private List<String> methods;
  private List<String> permissionsRequired;

  public List<String> getPermissionsRequired() {
    if (permissionsRequired == null) {
      permissionsRequired = List.of();
    }
    return permissionsRequired;
  }
}
