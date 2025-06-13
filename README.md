# Spectral Linter Plugin for JetBrains

<!-- Plugin description -->
This plugin is a wrapper for the tool <a href="https://github.com/stoplightio/spectral">Spectral</a>, a linter for
OpenApi schemas. It is a fork of https://github.com/SchwarzIT/spectral-intellij-plugin

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Spectral Linter"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Install
  it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

This plugin it is required to have the spectral CLI installed on your system. For windows it is recommended to
install the binary without going through Nodejs.

> **Warning** Should you still want to use the spectral package via NodeJs on Windows then
you must check "Use node package on Windows" in the plugin settings. This is necessary for the node packages to
work and will have a performance cost.

If you don't have it installed yet make sure to follow this
guide: [Installing Spectral](https://docs.stoplight.io/docs/spectral/b8391e051b7d8-installation)

_Note: The CLI needs to be in your path and executable from the command line._

## Features

### Automatic linting

Automatic linting of your OpenApi specifications and highlighting in your editor

### Configurable Ruleset

Specify your own [ruleset](https://meta.stoplight.io/docs/spectral/ZG9jOjYyMDc0NA-rulesets) in the plugins
settings, under Preferences -> Tools -> Spectral -> Ruleset.

There you can specify a file on your local machine or just paste the URL of a ruleset available on the internet
e.g.: [Zalando Ruleset](https://raw.githubusercontent.com/baloise-incubator/spectral-ruleset/main/zalando.yml).

Examples:

- Link to a hosted ruleset: `https://raw.githubusercontent.com/baloise-incubator/spectral-ruleset/main/zalando.yml`
- Local ruleset relative to Project base-path: `.spectral.yaml`
- Fully-qualified path: `/Users/user/.spectral.yaml`

### Configurable Included path patterns

Select the files that will be linted. By default, every file called "openapi.json", "openapi.yml" or "openapi.yaml"
within the Project root will be matched for linting by the plugin when it's opened.

You can adjust this in the settings under Preferences -> Tools -> Spectral Linter -> Included path patterns. All paths are
relative to the project's root directory unless absolute.

Examples:

- `openapi.json`: Matches the file called "openapi.json" inside the root directory of the project
- `components/**/*.yaml`: Matches all files inside the project subdirectory "components" that end with ".yaml"
- `/Users/user/code/**/openapi*.yaml`: Matches all YAML files within the absolute path "/Users/mick/code" that start
  with "openapi" and end with ".yaml"

**Note:** Each file must also be recognised by the IDE as a JSON or YAML file - that is with a suitable File Type
association.
If it is detected as a plain text (or any other type) it will be ignored.

### Possibility to use file level overrides
Spectral offers the possibility to add file based overrides as described [here](https://docs.stoplight.io/docs/spectral/293426e270fac-overrides).
These don't work by default. If you want to make use of them, then you need to check "Use file level overrides" in the plugin settings.

> **Warning** If turned on, this has a important caveat; you need to save the file for the linting output to match your changes again.

<!-- Plugin description end -->

## Building the plugin
### Building
```bash
./gradlew verifyPlugin  
```

### Run compatibility tests
```bash
 ./gradlew buildPlugin  
```

### Run UI tests
```bash
./gradlew runIdeForUiTests
```

### Publish new version
```bash
./gradlew publishPlugin  
```

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-gradle-plugin

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html