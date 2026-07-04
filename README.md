# Format Transformer

[![Build](https://github.com/jsonformatter/format-transformer/actions/workflows/build.yml/badge.svg)](https://github.com/jsonformatter/format-transformer/actions/workflows/build.yml)
[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](CHANGELOG.md)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Convert JSON into developer-friendly formats.**

Format Transformer is a lightweight IntelliJ IDEA plugin that converts JSON into multiple output formats using an extensible conversion engine. Fully offline — no AI, no cloud, no API calls.

| | |
|---|---|
| **Plugin ID** | `com.jsonformatter.format-transformer` |
| **Version** | 2.0.0 |
| **Platform** | IntelliJ IDEA 2026.1+ (Community Edition supported) |
| **Language** | Kotlin |
| **Build** | Gradle 9.3+, JDK 21 |

---

## Features

- **8 output formats (v1):** application.properties, YAML, XML, Text, CSV, Markdown, Java Map, Java Properties
- **Extensible architecture:** Strategy + Factory pattern — add new formats without modifying existing converters
- **Nested JSON support:** objects, arrays, booleans, numbers, strings, null, unlimited depth
- **Non-destructive:** opens converted output in a new tab; original JSON is never modified
- **Project-aware output:** saves next to the source JSON file with the matching extension
- **Validation & notifications:** invalid JSON, empty files, missing editor, I/O errors
- **Settings page:** configure default output basename for unsaved files
- **Welcome guide:** shown once on first install
- **Offline-first:** everything runs locally inside the IDE

---

## Supported Output Formats

| Format | Menu label | Extension | Example output |
|--------|------------|-----------|----------------|
| Properties | application.properties | `.properties` | `database.host=localhost` |
| YAML | YAML | `.yaml` | `database:` / `  host: localhost` |
| XML | XML | `.xml` | `<database><host>localhost</host></database>` |
| Text | Text | `.txt` | `database.host = localhost` |
| CSV | CSV | `.csv` | `Key,Value` + flattened rows |
| Markdown | Markdown | `.md` | `\| Key \| Value \|` table |
| Java Map | Java Map | `.java` | `Map<String, Object> map = ...` |
| Java Properties | Java Properties | `.java` | `Properties props = new Properties();` |

---

## Requirements

- **Runtime:** IntelliJ IDEA 2026.1 or later
- **Development:** JDK 21, Gradle 9.3+ (wrapper included)

---

## Installation

### From JetBrains Marketplace

1. Open IntelliJ IDEA
2. Go to **Settings → Plugins → Marketplace**
3. Search for **Format Transformer**
4. Click **Install** and restart the IDE

### From Disk (Development Build)

```bash
./gradlew buildPlugin
```

Install the generated ZIP from `build/distributions/` via **Settings → Plugins → ⚙ → Install Plugin from Disk...**

---

## Usage

1. Open a JSON file in the editor
2. Right-click → **Convert JSON** → select a format
3. Or click **Convert JSON** in the main toolbar and pick a format
4. The converted file opens in a new editor tab and is saved next to your JSON file

### Output file location

| Source file | Selected format | Output file |
|-------------|-----------------|-------------|
| `src/application.json` | YAML | `src/application.yaml` |
| `src/application.json` | application.properties | `src/application.properties` |
| `src/config.json` | XML | `src/config.xml` |
| Unsaved / untitled buffer | any | `{project-root}/{default-basename}.{extension}` |

The original JSON file is **never modified**.

### Settings

**Settings → Tools → Format Transformer**

- **Default output basename** — fallback name when the JSON file has no filename (default: `output`)

---

## Examples

**Input JSON:**

```json
{
  "database": {
    "host": "localhost",
    "port": 3306
  },
  "users": [
    { "name": "John" },
    { "name": "Alex" }
  ],
  "debug": true,
  "optional": null
}
```

### application.properties

```properties
database.host=localhost
database.port=3306
users[0].name=John
users[1].name=Alex
debug=true
optional=
```

### YAML

```yaml
database:
  host: localhost
  port: 3306
users:
  - name: John
  - name: Alex
debug: true
optional: null
```

### XML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<root>
  <database>
    <host>localhost</host>
    <port>3306</port>
  </database>
  <users>
    <name>John</name>
  </users>
  <users>
    <name>Alex</name>
  </users>
  <debug>true</debug>
  <optional/>
</root>
```

### Text

```text
database.host = localhost
database.port = 3306
users[0].name = John
users[1].name = Alex
debug = true
optional =
```

### CSV

```csv
Key,Value
database.host,localhost
database.port,3306
users[0].name,John
users[1].name,Alex
debug,true
optional,
```

### Markdown

```markdown
| Key | Value |
| --- | --- |
| database.host | localhost |
| database.port | 3306 |
| users[0].name | John |
| users[1].name | Alex |
| debug | true |
| optional |  |
```

### Java Map

```java
Map<String, Object> map = new LinkedHashMap<>();
map.put("database.host", "localhost");
map.put("database.port", 3306);
map.put("users[0].name", "John");
map.put("users[1].name", "Alex");
map.put("debug", Boolean.TRUE);
map.put("optional", null);
```

### Java Properties

```java
Properties props = new Properties();
props.setProperty("database.host", "localhost");
props.setProperty("database.port", "3306");
props.setProperty("users[0].name", "John");
props.setProperty("users[1].name", "Alex");
props.setProperty("debug", "true");
props.setProperty("optional", "");
```

---

## Architecture

Clean Architecture with a generic conversion pipeline:

```
Editor Reader
     ↓
Validator (strict JSON)
     ↓
Parser (Gson → JsonNode tree)
     ↓
Converter Factory (registry)
     ↓
Converter Interface (Strategy)
     ↓
  ┌──────────────────────────────────────────────┐
  │ Properties │ YAML │ XML │ CSV │ Markdown │ … │
  └──────────────────────────────────────────────┘
     ↓
Editor Writer (save + open tab)
```

### Design patterns

| Pattern | Usage |
|---------|--------|
| **Strategy** | Each output format implements `Converter` |
| **Factory** | `ConverterFactory` resolves converter by format ID |
| **Service** | `FormatConversionService` orchestrates the pipeline |
| **Singleton** | `ConverterFactory`, `NotificationService` (where appropriate) |

### Project structure

```
src/main/kotlin/com/jsonformatter/
├── actions/           # Convert JSON menu & toolbar group
├── converter/         # Converter interface + format implementations
│   ├── properties/
│   ├── yaml/
│   ├── xml/
│   ├── csv/
│   ├── markdown/
│   ├── text/
│   └── java/
├── factory/           # ConverterFactory registry
├── parser/            # JsonParserService, JsonTreeParser, JsonNode
├── validator/         # JsonValidator
├── editor/            # EditorReader, EditorWriter
├── notification/      # IntelliJ notifications
├── settings/          # Plugin settings page
├── service/           # FormatConversionService
├── welcome/           # First-run welcome notification
├── util/              # Shared helpers (flatten, escape, etc.)
└── constants/         # PluginConstants, FormatIds
```

---

## Adding a New Format

1. Create a converter class implementing `Converter`:

```kotlin
class TomlConverter : Converter {
    override val formatId = "toml"
    override val formatName = "TOML"
    override val extension = "toml"

    override fun convert(node: JsonNode): String {
        // conversion logic
    }
}
```

2. Register it in `ConverterFactory.registerConverters()`:

```kotlin
TomlConverter(),
```

3. The new format appears automatically in the **Convert JSON** menu — no changes to actions or services required.

4. Add JUnit tests under `src/test/kotlin/com/jsonformatter/converter/`.

---

## Error Handling

| Condition | Notification |
|-----------|--------------|
| Invalid JSON | **Invalid JSON** |
| Empty editor | **Empty File** |
| No active editor | **No Active Editor** |
| Unsupported format | **Unsupported Format** |
| I/O failure | **I/O Error** |
| Unexpected error | **Unexpected Error** |

---

## Development

### Setup

```bash
git clone <repository-url>
cd format-transformer
./gradlew build
```

Ensure JDK 21 is available:

```bash
java -version
```

### Commands

```bash
./gradlew build        # compile, test, assemble
./gradlew test         # run JUnit tests
./gradlew buildPlugin  # build installable ZIP
./gradlew runIde       # launch sandbox IDE with plugin loaded
```

The distributable plugin ZIP is written to:

```
build/distributions/format-transformer-2.0.0.zip
```

### Testing in the sandbox

1. Run `./gradlew runIde`
2. A separate IntelliJ window opens with the plugin pre-loaded
3. Create or open a JSON file and use **Convert JSON**
4. Close the sandbox window to stop the Gradle task

### Test coverage

Tests are located under `src/test/kotlin/com/jsonformatter/`:

- Converter tests (all 8 formats)
- `ConverterFactory` registry tests
- `JsonValidator` tests
- `JsonTreeParser` tests
- Output filename utility tests

---

## CI/CD

GitHub Actions workflow (`.github/workflows/build.yml`) runs on push/PR:

- `./gradlew buildPlugin`
- `./gradlew test`
- Uploads the plugin ZIP as a build artifact

---

## Screenshots

<!-- Add screenshots of the Convert JSON submenu, toolbar popup, and converted output tabs -->

---

## Roadmap

- [ ] TOML output format
- [ ] Environment variable export format
- [ ] Custom output filename templates
- [ ] JSON Schema aware key ordering
- [ ] Format selection memory (remember last used format)

---

## FAQ

**Does this modify my JSON file?**  
No. A new file is created and opened in a separate editor tab.

**Where is the output saved?**  
In the same directory as the source JSON file, using the same base name with the selected format extension (e.g. `app.json` → `app.yaml`).

**Are temp files created?**  
No. Output is written directly into your project directory next to the source JSON file.

**Can I add new formats?**  
Yes. Implement the `Converter` interface and register the class in `ConverterFactory`.

**Does it work offline?**  
Yes. No network, AI, or cloud services are used.

**Which IntelliJ versions are supported?**  
IntelliJ IDEA 2026.1 and later (build `261+`).

---

## Contributing

Contributions are welcome. Please open an issue before large changes.

1. Fork the repository
2. Create a feature branch (`feature/my-format-converter`)
3. Add tests for new converters or bug fixes
4. Run `./gradlew build` and ensure all tests pass
5. Open a pull request with a clear description

---

## License

MIT — see [LICENSE](LICENSE).

Copyright (c) 2026 Format Transformer Team
