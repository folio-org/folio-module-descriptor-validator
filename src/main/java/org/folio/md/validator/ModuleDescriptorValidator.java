package org.folio.md.validator;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.apache.maven.plugins.annotations.LifecyclePhase.COMPILE;
import static org.apache.maven.plugins.annotations.ResolutionScope.RUNTIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.folio.md.validator.model.ModuleDescriptor;
import org.folio.md.validator.model.ValidationContext;

@Mojo(name = "validate-module-descriptor", defaultPhase = COMPILE, requiresDependencyResolution = RUNTIME,
  requiresProject = false)
public class ModuleDescriptorValidator extends AbstractMojo {

  private static final String DEFAULT_MODULE_DESCRIPTOR_FILE =
    "${project.basedir}/descriptors/ModuleDescriptor-template.json";

  private final ValidatorManager validator = new ValidatorManager();
  private final ObjectMapper objectMapper = initializeObjectMapper();

  @Parameter(property = "moduleDescriptorFile", defaultValue = DEFAULT_MODULE_DESCRIPTOR_FILE)
  private File moduleDescriptroFile;

  @Parameter(property = "failOnInvalidJson", defaultValue = "true")
  private boolean failOnInvalidJson;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (moduleDescriptroFile == null || !moduleDescriptroFile.exists()) {
      handleFailure("Module descriptor file is not found");
    }

    var moduleDescriptor = parseModuleDescriptorFile(moduleDescriptroFile);
    if (moduleDescriptor == null) {
      return;
    }

    var validated = validator.validate(new ValidationContext(moduleDescriptor));
    if (validated.hasErrorParameters()) {
      handleFailure(asJson(validated.getErrorParameters()));
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
    if (failOnInvalidJson) {
      throw new MojoExecutionException(message);
    } else {
      getLog().warn(message);
    }
  }

  private void handleFailure(String message, Throwable cause) throws MojoExecutionException {
    if (failOnInvalidJson) {
      throw new MojoExecutionException(message, cause);
    } else {
      getLog().warn(message, cause);
    }
  }

  private static ObjectMapper initializeObjectMapper() {
    var objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(NON_NULL);
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }
}
