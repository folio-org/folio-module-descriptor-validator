package org.folio.md.validator.service.impl;

import org.folio.md.validator.model.RoutingEntry;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;

public class EndpointRequiredPermissionsValidator implements Validator {

  @Override
  public void validate(ValidationContext ctx) {
    ctx.getModuleDescriptor().getProvides().stream()
      .flatMap(interfaceDescriptor -> interfaceDescriptor.getHandlers().stream())
      .forEach(endpoint -> addErrorParameterIfNotValid(endpoint, ctx));
  }

  private static void addErrorParameterIfNotValid(RoutingEntry provided, ValidationContext ctx) {
    var permissionsRequired = provided.getPermissionsRequired();
    if (permissionsRequired == null) {
      return;
    }

    if (permissionsRequired.size() > 1) {
      ctx.addErrorParameter("Single permission is required for endpoint",
        provided.getPathPattern() + " " + provided.getMethods());
    }

    permissionsRequired.stream()
      .filter(perm -> ctx.getPermissionWithSubPermissions().contains(perm))
      .forEach(perm -> ctx.addErrorParameter("Permission with sub-permissions is not allowed for endpoint",
        provided.getPathPattern() + " " + provided.getMethods() + " " + perm));
  }
}
