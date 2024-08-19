## Module Descriptor Batch Validator
This script helps to validate module descriptors for modules gathered in `install.json`.
### Usage
Put `md-validator.sh` file next to `install.json` and run command:
```bash
./md-validator.sh
```
### How it works
Script parse `install.json` file, gets all modules IDs and downloads module descriptors in sequence. Then plugin validates modules descriptors. It is based on non-project usage feature of plugin.
