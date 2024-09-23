#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <path_to_module_descriptor_json_file>"
  exit 1
fi

JSON_INPUT="$1"

./mvnw org.folio:folio-module-descriptor-validator:1.0.0:validate -DmoduleDescriptorFile="$JSON_INPUT"
