#!/bin/bash

JSON_FILE="install.json"
BASE_URL="https://folio-registry.dev.folio.org/_/proxy/modules"
PLUGIN_GROUP_ID="org.folio"
PLUGIN_ARTIFACT_ID="folio-module-descriptor-validator"
PLUGIN_VERSION="0.0.1-SNAPSHOT"
PLUGIN_GOAL="validate"

ids=$(jq -r '.[].id' $JSON_FILE)

for id in $ids; do
  FILE_URL="$BASE_URL/$id"
  FILE_PATH="$id.json"

  curl -sS -o $FILE_PATH $FILE_URL

  echo -n "Validation $FILE_PATH started... "

  if [ $? -eq 0 ]; then

    mvn $PLUGIN_GROUP_ID:$PLUGIN_ARTIFACT_ID:$PLUGIN_VERSION:$PLUGIN_GOAL -q -DmoduleDescriptorFile=$FILE_PATH

    rm -f $FILE_PATH
    echo -ne "\rValidation $FILE_PATH  DONE      "
    echo ""
  else
    echo "Failed to download $FILE_URL"
  fi
done

rm -f $TEMP_POM

echo "All operations completed."
