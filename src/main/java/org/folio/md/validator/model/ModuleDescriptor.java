package org.folio.md.validator.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ModuleDescriptor {

  private String id;
  private String name;
  private List<InterfaceDescriptor> provides;
  private List<Permission> permissionSets;

  public List<Permission> getPermissionSets() {
    if (permissionSets == null) {
      permissionSets = new ArrayList<>();
    }
    return permissionSets;
  }

  public List<InterfaceDescriptor> getProvides() {
    if (provides == null) {
      provides = new ArrayList<>();
    }
    return provides;
  }
}
