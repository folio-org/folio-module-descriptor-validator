package org.folio.md.validator;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.folio.md.validator.model.ModuleDescriptor;
import org.folio.md.validator.model.ValidationContext;
import org.folio.md.validator.service.impl.BackendPermissionNamesValidator;
import org.folio.md.validator.service.impl.EndpointRequiredPermissionsValidator;
import org.folio.md.validator.service.impl.PermissionDefinitionValidator;
import org.folio.md.validator.service.impl.UniquePermissionForEndpointValidator;

@Mojo(name = "validate", defaultPhase = COMPILE, requiresDependencyResolution = RUNTIME, requiresProject = false)
public class ModuleDescriptorValidator extends AbstractMojo {

  private static final String DEFAULT_MODULE_DESCRIPTOR_FILE =
    "${project.basedir}/descriptors/ModuleDescriptor-template.json";

  @Parameter(property = "moduleDescriptorFile", defaultValue = DEFAULT_MODULE_DESCRIPTOR_FILE)
  File moduleDescriptroFile;

  @Parameter(property = "failOnInvalidDescriptor", defaultValue = "true")
  boolean failOnInvalidDescriptor;

  private final ValidatorManager validator = initializeValidator();
  private final ObjectMapper objectMapper = initializeObjectMapper();

  @Override
  public void execute() throws MojoExecutionException {
    if (moduleDescriptroFile == null || !moduleDescriptroFile.exists()) {
      handleFailure("Module descriptor file is not found");
    }

    var moduleDescriptor = parseModuleDescriptorFile(moduleDescriptroFile);
    if (moduleDescriptor == null) {
      return;
    }

    var validated = validator.validate(new ValidationContext(moduleDescriptor));
    if (validated.hasErrorParameters()) {
      handleFailure("Module descriptor not valid: " + asJson(validated.getErrorParameters()));
    }
  }

  @SneakyThrows
  private String asJson(Object object) {
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
  }

  private ModuleDescriptor parseModuleDescriptorFile(File moduleDescriptroFile) throws MojoExecutionException {
    try {
      return objectMapper.readValue(moduleDescriptroFile, ModuleDescriptor.class);
    } catch (Exception e) {
      handleFailure("Failed to parse module descriptor file: " + moduleDescriptroFile.getPath(), e);
      return null;
    }
  }

  private void handleFailure(String message) throws MojoExecutionException {
    if (failOnInvalidDescriptor) {
      throw new MojoExecutionException(message);
    }
    getLog().warn(message);
  }

  private void handleFailure(String message, Throwable cause) throws MojoExecutionException {
    if (failOnInvalidDescriptor) {
      throw new MojoExecutionException(message, cause);
    }
    getLog().warn(message, cause);
  }

  private static ValidatorManager initializeValidator() {
    return new ValidatorManager(List.of(
      new BackendPermissionNamesValidator(),
      new EndpointRequiredPermissionsValidator(),
      new UniquePermissionForEndpointValidator(),
      new PermissionDefinitionValidator()
    ));
  }

  private static ObjectMapper initializeObjectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(NON_NULL);
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }
}
