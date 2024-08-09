package org.folio.md.validator.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class InterfaceDescriptor {

  private String id;
  private String version;
  private List<RoutingEntry> handlers;
  private List<String> scope;

  public List<RoutingEntry> getHandlers() {
    if (handlers == null) {
      handlers = new ArrayList<>();
    }
    return handlers;
  }
}
