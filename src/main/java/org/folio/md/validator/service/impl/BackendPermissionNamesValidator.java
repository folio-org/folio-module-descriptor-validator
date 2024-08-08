package org.folio.md.validator.service.impl;

import static org.folio.common.utils.permission.PermissionUtils.extractPermissionData;
import static org.folio.common.utils.permission.PermissionUtils.hasNoRequiredFields;

import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;

public class BackendPermissionNamesValidator implements Validator {

  @Override
  public void validate(ValidationContext ctx) {
    var moduleDescriptor = ctx.getModuleDescriptor();
    moduleDescriptor.getPermissionSets().forEach(permission -> {
      var permissionData = extractPermissionData(permission.getPermissionName());
      if (hasNoRequiredFields(permissionData)) {
        ctx.addErrorParameter("Permission name is not valid", permission.getPermissionName());
      }
    });
  }
}
