package org.folio.md.validator.support;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.folio.md.validator.model.ModuleDescriptor;

@UtilityClass
public class TestUtils {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
    .setSerializationInclusion(Include.NON_NULL)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
  private static final ClassLoader CLASS_LOADER = TestUtils.class.getClassLoader();

  @SneakyThrows
  public static <T> T parse(String result, Class<T> type) {
    try {
      return OBJECT_MAPPER.readValue(result, type);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  @SneakyThrows
  public static String readString(String path) {
    var resource = CLASS_LOADER.getResource(path);
    var file = new File(Objects.requireNonNull(resource).toURI());

    return Files.readString(file.toPath(), StandardCharsets.UTF_8);
  }

  public static ModuleDescriptor readModuleDescriptor(String path) {
    return parse(readString(path), ModuleDescriptor.class);
  }
}
