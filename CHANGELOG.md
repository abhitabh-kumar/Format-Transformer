# Changelog

All notable changes to Format Transformer are documented in this file.

## [2.0.0] - 2026-07-03

### Added

- Extensible conversion engine with Strategy + Factory architecture
- `Converter` interface and `ConverterFactory` registry
- Internal `JsonNode` tree model for format-agnostic conversion
- Eight output formats:
  - application.properties
  - YAML
  - XML
  - Text
  - CSV
  - Markdown Table
  - Java Map
  - Java Properties Object
- Dynamic **Convert JSON** submenu (right-click) and toolbar popup
- Save converted output next to the source JSON file (same base name, format extension)
- Unsupported format notification
- Tests for all converters, factory, parser, and validator

### Changed

- Plugin renamed from **JSON Format Converter** to **Format Transformer**
- Plugin ID updated to `com.jsonformatter.format-transformer`
- Replaced single-format converter with pluggable converter modules
- Settings renamed to **Format Transformer** (default output basename)
- Welcome notification updated for multi-format workflow

### Removed

- Hardcoded JSON → properties-only conversion path
- Temp-file output strategy (replaced with project-directory writes)

## [1.0.0] - 2026-07-01

### Added

- Initial JSON to application.properties conversion
- Right-click editor action and toolbar button
- JSON validation with strict parsing
- Settings page and welcome notification
- JUnit tests and GitHub Actions build workflow
