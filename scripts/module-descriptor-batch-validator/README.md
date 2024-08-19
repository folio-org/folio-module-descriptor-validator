## Module Descriptor Batch Validator
This script helps to validate module descriptors for modules gathered in `install.json`.

### Usage
Place the `md-validator.sh` file next to `install.json` and run the command:
```bash
./md-validator.sh
```

### How it works
The script parses the `install.json` file, retrieves all module IDs, and downloads the module descriptors sequentially. Then, the plugin validates the module descriptors. It is based on the non-project usage feature of the plugin.
