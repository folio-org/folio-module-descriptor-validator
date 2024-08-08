package org.folio.md.validator.model;

import java.util.List;
import lombok.Data;

@Data
public class Permission {

  private String permissionName;
  private List<String> subPermissions;
}
