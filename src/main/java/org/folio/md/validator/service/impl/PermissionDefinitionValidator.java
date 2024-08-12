package org.folio.md.validator.service.impl;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;

public class PermissionDefinitionValidator implements Validator {

  @Override
  public void validate(ValidationContext ctx) {
    ctx.getModuleDescriptor().getProvides().stream()
      .flatMap(interfaceDescriptor -> interfaceDescriptor.getHandlers().stream())
      .filter(endpoint -> endpoint.getPermissionsRequired() != null)
      .flatMap(endpoint -> endpoint.getPermissionsRequired().stream())
      .forEach(permission -> addErrorParameterIfNotValid(permission, ctx));
  }

  private static void addErrorParameterIfNotValid(String permission, ValidationContext ctx) {
    if (!ctx.getDefinedPermissions().contains(permission)) {
      ctx.addErrorParameter("Permission is not defined in module descriptor", permission);
    }
  }
}
