package org.folio.md.validator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class ValidationContext {

  private ModuleDescriptor moduleDescriptor;
  private List<ErrorParameter> errorParameters;

  private List<String> permissionWithSubPermissions;
  private List<String> definedPermissions;

  public ValidationContext(ModuleDescriptor moduleDescriptor) {
    this.moduleDescriptor = moduleDescriptor;
  }

  public List<ErrorParameter> getErrorParameters() {
    if (errorParameters == null) {
      errorParameters = new ArrayList<>();
    }
    return errorParameters;
  }

  public void addErrorParameter(String key, String value) {
    getErrorParameters().add(ErrorParameter.of(key, value));
  }

  public List<String> getPermissionWithSubPermissions() {
    if (permissionWithSubPermissions == null) {
      permissionWithSubPermissions = moduleDescriptor.getPermissionSets()
        .stream()
        .filter(ValidationContext::hasSubPermissions)
        .map(Permission::getPermissionName)
        .filter(Objects::nonNull)
        .toList();
    }
    return permissionWithSubPermissions;
  }

  public List<String> getDefinedPermissions() {
    if (definedPermissions == null) {
      definedPermissions = moduleDescriptor.getPermissionSets()
        .stream()
        .map(Permission::getPermissionName)
        .toList();
    }
    return definedPermissions;
  }

  public boolean hasErrorParameters() {
    return errorParameters != null && !errorParameters.isEmpty();
  }

  private static boolean hasSubPermissions(Permission permission) {
    return permission.getSubPermissions() != null && !permission.getSubPermissions().isEmpty();
  }
}
