🗃️**Detailed Changelog: [1.7.0 --> 1.8.0](https://github.com/UltimatChamp/BetterGrassify/compare/1.7.0%2bfabric.1.21.6...1.8.0%2bfabric.1.21.8)**

---

### 🛠️Changes

- Marked as compatible with `1.21.6-1.21.8`.
  - As already mentioned before, the support for `1.21-1.21.5` has been dropped.
- Improved config file structure and migrated over to 'Cloth Config'.
  - The `Whitelist` options are no longer hidden as config-only options, and added a small note explaining the user about the difference between both options.
  - Your existing options will be reset.
- Improvements to the `Excluded` and `Whitelisted Blocks` options. You can now specify many more properties/predicates like `chain[axis = y]`, instead of just the boolean ones.
- Added a separate category for BetterGrassify in Sodium's Options Screen for clarity.
- Removed 'Jankson' as a bundled dependency, decreasing the mod size by ~50%, which also means no comments in config files.
- Improved project structure, updated dependencies and fixed many minor bugs.

---

- Migrated to [**Crowdin**](https://crowdin.com/project/bettergrassify). You can now contribute your translations with more convenience there.
- Also, some translations have been changed and many added.
