package org.folio.md.validator.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.folio.md.validator.model.ModuleDescriptor;
import org.folio.md.validator.model.Permission;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.support.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@UnitTest
class BackendPermissionNamesValidatorTest {

  private final BackendPermissionNamesValidator validator = new BackendPermissionNamesValidator();

  @ParameterizedTest
  @MethodSource("permissionsDataSource")
  void validate_parametrized(String permissionName, boolean isValid) {
    var context = new ValidationContext(moduleDescriptor(permissionName));
    validator.validate(context);
    var expected = isValid ? 0 : 1;
    assertThat(context.getErrorParameters()).hasSize(expected);
  }

  static Stream<Arguments> permissionsDataSource() {
    return Stream.of(
      Arguments.of("test.users.item.todo.execute", true),
      Arguments.of("finance-import.budget.item.post", true),
      Arguments.of("users.item.put", true),
      Arguments.of("users.item.patch", true),
      Arguments.of("users.item.delete", true),
      Arguments.of("users.item.execute", true),
      Arguments.of("users.collection.get", true),
      Arguments.of("users.collection.post", true),
      Arguments.of("users.collection.put", true),
      Arguments.of("users.collection.patch", true),
      Arguments.of("users.collection.execute", true),
      Arguments.of("users.collection.delete", true),
      Arguments.of("invoices.bypass-acquisition-units", false)
    );
  }

  private static ModuleDescriptor moduleDescriptor(String permissionName) {
    var md = new ModuleDescriptor();
    var permission = new Permission();
    permission.setPermissionName(permissionName);
    md.setPermissionSets(List.of(permission));
    return md;
  }
}
