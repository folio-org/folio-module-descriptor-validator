#!/bin/bash

JSON_FILE="install.json"
BASE_URL="https://folio-registry.dev.folio.org/_/proxy/modules"
PLUGIN_GROUP_ID="org.folio"
PLUGIN_ARTIFACT_ID="folio-module-descriptor-validator"
PLUGIN_VERSION="1.0.1-SNAPSHOT"
PLUGIN_GOAL="validate"

REPORT_FILE="validation_report.txt"

ERROR_KEYS_FILE="error_keys.tmp"

rm -f "$REPORT_FILE" "$ERROR_KEYS_FILE"

ids=$(jq -r '.[].id' "$JSON_FILE")

for id in $ids; do
  FILE_URL="$BASE_URL/$id"
  FILE_PATH="$id.json"

  curl -sS -o "$FILE_PATH" "$FILE_URL"

  echo -n "Validation $FILE_PATH started... "

  if [ $? -eq 0 ]; then

    ERROR_OUTPUT=$(mvn "$PLUGIN_GROUP_ID":"$PLUGIN_ARTIFACT_ID":"$PLUGIN_VERSION":"$PLUGIN_GOAL" -q -DmoduleDescriptorFile="$FILE_PATH" 2>&1)

    rm -f "$FILE_PATH"
    echo -ne "\rValidation $FILE_PATH  DONE      "
    echo ""

    echo "$id" >> "$REPORT_FILE"

    if echo "$ERROR_OUTPUT" | grep -q "Module descriptor not valid:"; then

      ERROR_OUTPUT_CLEAN=$(echo "$ERROR_OUTPUT" | sed 's/^\[ERROR\]\s*//')

      ERROR_BLOCK=$(echo "$ERROR_OUTPUT_CLEAN" | sed -n '/Module descriptor not valid:/,/^]\s*$/p')

      ERROR_BLOCK=$(echo "$ERROR_BLOCK" | sed '/Module descriptor not valid:/d')

      while read -r line; do
        if echo "$line" | grep -q '"key"'; then
          key=$(echo "$line" | sed 's/.*"key" *: *"\(.*\)" *,*/\1/')
          read -r value_line
          if echo "$value_line" | grep -q '"value"'; then
            value=$(echo "$value_line" | sed 's/.*"value" *: *"\(.*\)"[ ,]*/\1/')
            echo "$key: $value" >> "$REPORT_FILE"
            echo "$key" >> "$ERROR_KEYS_FILE"
          fi
        fi
      done <<< "$ERROR_BLOCK"

    fi

    echo "----------------------------------------" >> "$REPORT_FILE"

  else
    echo "Failed to download $FILE_URL"
  fi
done

echo "All operations completed."

if [ -f "$ERROR_KEYS_FILE" ]; then
  echo "Summary of errors by type:" >> "$REPORT_FILE"
  sort "$ERROR_KEYS_FILE" | uniq -c | while read count key; do
    echo "$key: $count" >> "$REPORT_FILE"
  done
  rm -f "$ERROR_KEYS_FILE"
fi
