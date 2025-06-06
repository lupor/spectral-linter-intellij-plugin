<!-- @formatter:off -->
<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## 0.0.12 - 2025-06-06

### Removed

- Removed upper bound for supported builds

## 0.0.11 - 2025-04-30

### Changed

- Bumped dependencies and upgraded to latest platform version
- Moved to new Kotlin UI dsl

### Removed

- Removed unused tasks
- Dropped compatibility with version lower than 241

## 0.0.10 - 2025-02-19

### Fixed

- Fixed mac compatibility
- Fixed windows compatibility binary installation methods

## 0.0.9 - 2025-02-19

### Added

- Option to toggle workaround for Windows users that want to use the node package instead of the binary installation
- Option to toggle support for file level overrides in spectral rulesets
- Sections in the readme detailing the new options and the caveats

### Fixed

- Fixed the way commands are constructed and ensure that the environment is properly loaded on windows

## 0.0.8 - 2025-02-18

### Changed

- Added basic handling for silent errors
- Fixed issues with file level overrides in rulesets
- Fixed parsing issues due to paths array containing elements other than strings
- Added better messages for file level issues

## 0.0.7 - 2025-02-12

### Changed

- Broadened version compatibility to spread the changes to the unlucky souls that downloaded version 3

## 0.0.6 - 2025-02-10

### Changed

- Reduced noise in the output
- Changed annotations to be only on the first line of the violation and not on one level down
- Added misc fixes from the original repository

## 0.0.5 - 2025-02-10

### Fixed

- Fixed release workflow and config
- First version to be released publicly

## 0.0.4 - 2024-02-10

### Fixed

- Cleaned up config
- Excluded EAP from verification

### Added

- Debugged and setup publishing
- Changed how top level rules are displayed. Moved them to the top of the file

## 0.0.3 - 2024-12-02

### Added

- Debugged and setup publishing
- Changed how top level rules are displayed. Moved them to the top of the file

## 0.0.2 - 2024-12-02

### Fixed

- Fixed bugs and compatibility issues
- Set up builds

## 0.0.1 - 2024-11-29

### Created

- Forked from https://github.com/SchwarzIT/spectral-intellij-plugin
- Upgraded to gradle v2 plugin: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
