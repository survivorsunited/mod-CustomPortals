# Custom Portals Mod Tests

This directory contains automated tests for the Custom Portals mod.

## Test Scripts

### Minecraft 1.21.11 game tests

```powershell
.\gradlew.bat build runGameTest --no-daemon
```

Uses the default 1.21.11 target with Java 21 and the checked-in Gradle wrapper.
The development-only test mod verifies stationary item transfer, stack count,
arrival cooldown, and real dropper output through linked portals. It is not
included in the distributable mod JAR. Tests use an isolated world under
`build/run/gameTest/` and shut down automatically.

On Windows hosts where Java reports `Unable to establish loopback connection`,
set `JAVA_TOOL_OPTIONS=-Djdk.net.unixdomain.tmpdir=<short-existing-local-directory>`
for the build process. This avoids long temporary socket paths without changing
the mod or the host's global Java settings.

### 003-validate-item-teleportation.ps1

Runs the item transfer and overlap-check method bodies against small Minecraft
doubles. Covers immediate transfer, stationary/boundary overlap, inactive and
unlinked portals, destination restrictions, client isolation, and arrival cooldown.
These checks do not replace an in-game test of Mixin injection, dropper behavior,
or Minecraft's item copying/merging during dimension changes.

```powershell
.\tests\003-validate-item-teleportation.ps1 -JavaHome $env:JAVA_HOME
```

Requires a JDK 21 or newer. Generated harness sources are retained in the system
temporary directory for diagnosis; tracked files are not modified.

### 001-validate-server-startup.ps1
**Purpose**: Validates server startup and world loading  
**Tests**:
- Mod builds successfully
- Server starts without crashing
- World loads successfully
- No portal-related errors
- No PersistentState errors
- No Mixin errors

**Usage**:
```powershell
.\tests\001-validate-server-startup.ps1 -MinecraftVersion "1.21.8" -TimeoutSeconds 180
```

**Exit Codes**:
- `0`: Test passed
- `1`: Test failed

## Running Tests

### Local Testing
```powershell
# Run all tests
.\tests\001-validate-server-startup.ps1

# Run for specific Minecraft version
.\tests\001-validate-server-startup.ps1 -MinecraftVersion "1.21.8"

# Run with custom timeout
.\tests\001-validate-server-startup.ps1 -TimeoutSeconds 300
```

### CI/CD Pipeline
Tests are automatically executed in the GitHub Actions pipeline:
- Runs after build completes
- Tests each Minecraft version (1.21.5-1.21.10)
- Pipeline fails if any test fails

## Test Results

Test results are saved in isolated folders under `tests/test-output/`:
- Each test has its own subfolder: `tests/test-output/{test-name}/`
- Example for test 001: `tests/test-output/001-validate-server-startup/`
  - `{version}-{timestamp}.log` - Test execution log
  - `{version}-{timestamp}-server.log` - Server output log
- **No overlap**: Each test's artifacts are completely isolated in its own folder

## Test Requirements

### Prerequisites
- Java 21 installed
- PowerShell 7+ available
- Build scripts in `scripts/` folder
- Mod built successfully

### Test Environment
- Each test uses isolated server instance
- Tests clean up after execution (optional - can disable for debugging)
- Server logs preserved for failure analysis

## Adding New Tests

1. Create test script: `tests/###-test-name.ps1`
2. Follow naming convention: `###-descriptive-name.ps1`
3. Use existing scripts from `scripts/` folder
4. Implement proper exit codes (0 = pass, 1 = fail)
5. Log results to `tests/results/`
6. Update this README

## Troubleshooting

### Test Fails: Server Won't Start
- Check Java version (must be 21)
- Verify mod JAR exists in `build/libs/`
- Check server logs in `tests/results/`

### Test Fails: World Won't Load
- Check for portal errors in server logs
- Verify PersistentState is working
- Check for Mixin conflicts

### Test Times Out
- Increase timeout: `-TimeoutSeconds 300`
- Check server resource usage
- Verify network connectivity for downloads

