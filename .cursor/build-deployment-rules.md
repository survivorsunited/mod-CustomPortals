# Build & Deployment Rules - Custom Portals Mod

## Build System
- Use Gradle with Fabric Loom
- Configuration centralized in `gradle.properties`
- PowerShell scripts in `/scripts/` directory for automation
- Always test builds before deployment

## PowerShell Scripts
- `build.ps1` - Main build script with optional server start
- `scripts/start-server.ps1` - Advanced server management (if exists)
- `scripts/update-modrinth.ps1` - Update Modrinth project description (if exists)
- `scripts/check-release.ps1` - Check release status (if exists)

## Version Management
- Update version in `gradle.properties`
- Version format: `{mod_version}+{minecraft_version}` (e.g., `3.11.0+1.21.10`)
- Create git tags for releases
- GitHub Actions handles automated publishing (if configured)

## Environment Variables
- `MODRINTH_TOKEN` - For Modrinth API access (if publishing)
- `PROJECT_ID` - Modrinth project identifier (if publishing)
- Set in GitHub repository secrets for CI/CD

## Testing Workflow
1. Build mod: `.\build.ps1`
2. Test locally: `.\build.ps1 -StartServer` (if available)
3. Run full build: `.\gradlew build`
4. Create release tag for deployment

## File Patterns to Ignore
- `/build/` - Gradle build output
- `/run/` - Development server files
- `/run1/`, `/run2/` - Test client directories
- `/test-server/` - Test server instance (if exists)
- `.gradle/` - Gradle cache

## Release Process
1. Update version in gradle.properties
2. Update changelog/release notes
3. Build and test thoroughly
4. Create git tag with version
5. GitHub Actions handles the rest (if configured)

## Build Configuration
- Java 21 required
- Minecraft version: 1.21.10+
- Fabric Loader: 0.17.3+
- Fabric API: 0.136.0+
- Uses Cardinal Components and YACL dependencies
