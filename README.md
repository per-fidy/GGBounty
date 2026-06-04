# GGBounty
Paper 26.1.2 / Java 23 plugin project for GGBounty.

## Build
Run:
  mvn -DskipTests package

Output jar:
  target/GGBounty-1.0.0.jar

## Commands and Usage
- /rep
  View your current reputation and current rank.
- /bounty list
  Open the bounty target GUI.
- /bounty accept <player>
  Accept a bounty contract for the specified player.
- /bounty claim
  Claim your bounty reward after completing a contract.
- /bountyboard create <high|low>
  Create a hologram bounty board at your current location.
  - high: shows top reputation players
  - low: shows low reputation players
- /bountyboard delete
  Remove the nearest bounty board you are standing near.
- /repadmin <set|add> <player> <amount>
  Admin command to modify a player's reputation.
  Permission: ggbounty.admin

## Permissions
- ggbounty.admin
  Full admin control over reputation changes.
- ggbounty.board.manage
  Allows managing hologram bounty boards.

## Features included
- modular listeners, commands, managers, GUI, and config
- reputation and rank tracking with UUID persistence
- bounty contracts and claim handling
- hologram bounty boards with multiple boards supported
- GitHub Actions workflow to build and upload the plugin jar

## GitHub Actions
The workflow in .github/workflows/build-plugin.yml builds the plugin on every push to main and uploads the generated jar as an artifact.
