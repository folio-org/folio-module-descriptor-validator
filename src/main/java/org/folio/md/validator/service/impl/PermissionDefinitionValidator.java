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
    if (ctx.getDefinedPermissions().contains(permission)) {
      return;
    }
    switch (permission) {
      case "remote-storage.pub-sub-handlers.log-record-event.post":
      case "audit.pub-sub-handlers.log-record-event.post":
        // Allow mod-pubsub to define the mod-audit and mod-remote-storage
        // API endpoint permissions. This solves the circular dependency when assigning permissions
        // to its system user when enabling the module for a tenant because mod-audit
        // and mod-remote-storage are not enabled at that time.
        // We allow this exception because https://github.com/folio-org/mod-pubsub is deprecated
        // and will be removed soon.
        return;
      default:
        ctx.addErrorParameter("Permission is not defined in module descriptor", permission);
    }
  }
}
