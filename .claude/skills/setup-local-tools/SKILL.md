---
name: setup-local-tools
description: Use when setting up a new machine for this repo, checking whether required CLI tools (JDK, Android SDK, Xcode, gh, Docker, Node, firebase-tools) are installed and authenticated, or diagnosing why an agent can't reach GitHub/Firebase via MCP. Triggered by phrases like "set up my environment", "what do I need to install", "onboard me on this repo", "check my dev tools", "set up the firebase cli", "set up gh cli", "why can't you access firebase/github".
---

# Skill: Set Up Local Tools for TruckTrack + AI Agents

> The recommended, from-scratch local setup for building this repo and letting an AI coding agent
> work in it at full capability (GitHub + Firebase MCP, `gh`, Gradle, Xcode). Always install the
> **latest stable** release of each tool unless a specific minimum is called out below — don't
> assume whatever happens to already be on the machine is adequate; verify with the check command.

## What this repo needs

| Tool | Version | Needed for | Check | Platform |
|---|---|---|---|---|
| JDK | 21 (Temurin/Adoptium recommended) | Gradle toolchain — `jvmToolchain(21)` in `build-logic/convention` | `java -version` | all |
| Android SDK | cmdline-tools + platform 37 + build-tools (latest) | `./gradlew :app:android:assembleDebug`, Android Studio | Android SDK path env var set and exists (`ANDROID_HOME` on macOS/Linux, `%LOCALAPPDATA%\Android\Sdk` on Windows) | all |
| Xcode | latest stable from the App Store (15+ minimum) + CLT | Building `app:ios` (`app/ios/iosApp.xcodeproj`) | `xcode-select -p` | **macOS only** — iOS can't be built elsewhere |
| `gh` (GitHub CLI) | latest | Agents running `gh run list`/`gh release create` etc. directly (`analyze-ci-failure`, `release-app` skills) | `gh auth status` | all |
| Docker | latest (Docker Desktop/Engine) | Runs the GitHub MCP server (`docker run ... ghcr.io/github/github-mcp-server`, see `.mcp.json`) | `docker info` | all |
| `GITHUB_PERSONAL_ACCESS_TOKEN` env var | — | Auth for the GitHub MCP server (`.mcp.json` interpolates it into the container) | `echo $GITHUB_PERSONAL_ACCESS_TOKEN` (`echo $env:GITHUB_PERSONAL_ACCESS_TOKEN` on PowerShell) | all |
| Node.js | current LTS (20+) | Runs the Firebase MCP server (`npx -y firebase-tools@latest mcp`, see `.mcp.json`) — an old Node breaks `npx` resolution for modern packages | `node --version` | all |
| `firebase-tools` CLI, logged in | latest (must support the `mcp` subcommand — anything recent does) | Humans/agents running `firebase` commands directly, and the underlying tool the Firebase MCP server wraps | `firebase --version` and `firebase login:list` | all |

## Step 1 — Install

Pick the section for your OS. Everything except Xcode/iOS builds applies to macOS, Linux, and
Windows equally.

### macOS (Homebrew)

```bash
brew install --cask temurin@21          # JDK 21
brew install --cask android-commandlinetools   # or install SDK via Android Studio's SDK Manager
xcode-select --install                  # Xcode itself: install from the App Store first
brew install gh && gh auth login
brew install --cask docker && open -a Docker   # start it once so `docker info` succeeds
brew install node                       # current LTS
npm install -g firebase-tools@latest && firebase login
```

### Linux (Debian/Ubuntu — adapt package manager as needed)

```bash
# JDK 21
sudo apt update && sudo apt install -y openjdk-21-jdk
# or use sdkman: sdk install java 21-tem

# Android SDK — download cmdline-tools from developer.android.com/studio#command-tools,
# unzip, then: sdkmanager "platform-tools" "platforms;android-37" "build-tools;37.0.0"

# GitHub CLI
type -p curl >/dev/null && curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg \
  | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" \
  | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update && sudo apt install -y gh
gh auth login

# Docker Engine — follow docs.docker.com/engine/install/ for your distro, then:
sudo usermod -aG docker $USER   # avoid needing sudo for `docker` commands (re-login after)

# Node.js (current LTS) — via nvm avoids fighting distro package versions
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.1/install.sh | bash
nvm install --lts

# Firebase CLI
npm install -g firebase-tools@latest && firebase login
```

### Windows (PowerShell, winget)

```powershell
winget install EclipseAdoptium.Temurin.21.JDK   # JDK 21
# Android SDK — install via Android Studio's SDK Manager (simplest on Windows)
winget install GitHub.cli
gh auth login
winget install Docker.DockerDesktop            # start Docker Desktop once so `docker info` succeeds
winget install OpenJS.NodeJS.LTS                # current LTS
npm install -g firebase-tools@latest
firebase login
```

Then export the GitHub MCP token as a persistent environment variable:

```bash
# macOS/Linux — add to ~/.zshrc, ~/.bashrc, or ~/.profile
export GITHUB_PERSONAL_ACCESS_TOKEN="<token with repo + workflow scope, from github.com/settings/tokens>"
```

```powershell
# Windows — persists across sessions
[System.Environment]::SetEnvironmentVariable("GITHUB_PERSONAL_ACCESS_TOKEN", "<token>", "User")
```

Both MCP servers (GitHub, Firebase) are already declared in `.mcp.json` — nothing to configure
per-machine beyond the tools/env var above, and `.mcp.json` itself is OS-agnostic (`docker`/`npx`
resolve the same way on every platform once the tools above are installed).

## Step 2 — Verify

Run all of these and confirm each passes before considering the machine ready (skip the
Xcode/iOS-specific ones off macOS):

```bash
java -version                # 21
gh auth status                # logged in
docker info >/dev/null 2>&1 && echo "docker: ok" || echo "docker: not running"
node --version                # 20+
firebase --version
firebase login:list            # shows an authenticated account
xcode-select -p               # macOS only — points at an installed Xcode
```

- [ ] `./gradlew :app:android:assembleDebug` succeeds from the command line
- [ ] (macOS only) `open app/ios/iosApp.xcodeproj` builds and runs in Xcode
- [ ] A fresh Claude Code session shows both `github` and `firebase` MCP servers in its tool list
      (MCP servers load at session start — restart the session after finishing setup)

## Gotchas worth knowing

| Gotcha | Why it happens |
|---|---|
| `./gradlew` fails with "No matching toolchains found for requested specification: {languageVersion=21}" even though JDK 21 is installed | `settings.gradle.kts` doesn't apply the `org.gradle.toolchains.foojay-resolver-convention` plugin, so Gradle only discovers JDKs already on the machine — it won't auto-download one. Point `JAVA_HOME` at the JDK 21 install (env var on macOS/Linux, System Environment Variables on Windows), or add its path to `org.gradle.java.installations.paths` in `gradle.properties` |
| Agent's GitHub MCP tools fail with an auth error even though `gh auth status` is fine | `gh auth login` authenticates the `gh` CLI's own credential store; the GitHub MCP server reads the separate `GITHUB_PERSONAL_ACCESS_TOKEN` env var — they're not the same credential |
| Firebase MCP tool list looks sparse | No `firebase.json` at the repo root yet, so the MCP server has nothing to detect and can only offer project-agnostic tools. Run `firebase init` for the features you actually need (Firestore, Auth, etc.) once this repo integrates Firebase |
| A newly edited `.mcp.json` doesn't show new tools in the running session | MCP servers are only loaded at session start — start a new Claude Code session after any `.mcp.json` or auth change |
