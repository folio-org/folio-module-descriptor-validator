package org.folio.md.validator.service.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.folio.md.validator.model.RoutingEntry;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.Validator;

public class UniquePermissionForEndpointValidator implements Validator {

  @Override
  public void validate(ValidationContext ctx) {
    var moduleDescriptor = ctx.getModuleDescriptor();
    moduleDescriptor.getProvides().stream()
      .flatMap(endpoint -> endpoint.getHandlers().stream())
      .flatMap(UniquePermissionForEndpointValidator::groupByPermission)
      .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toSet())))
      .forEach((permission, endpoints) -> addErrorParameterIfPermNotUnique(ctx, permission, endpoints));
  }

  private static void addErrorParameterIfPermNotUnique(ValidationContext ctx, String perm, Set<String> endpoints) {
    if (endpoints.size() > 1) {
      ctx.addErrorParameter("Permission protects more than one endpoint", perm + " " + endpoints);
    }
  }

  private static Stream<SimpleEntry<String, String>> groupByPermission(RoutingEntry handler) {
    return handler.getPermissionsRequired().stream()
      .map(permission -> new SimpleEntry<>(permission, extractPathAndMethods(handler)));
  }

  private static String extractPathAndMethods(RoutingEntry handler) {
    return handler.getPathPattern() + " " + handler.getMethods();
  }
}
