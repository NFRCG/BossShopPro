This will act as a basic overview of plugin changes, and reasoning. As mentioned in the README, these are opinionated
changes. Not everyone will agree and that's fine.

v2.1.0: First release

Is this release usable (Yes/No): No

- Removed ServerPinging implementation: Out of scope for plugin. Could be addon in the future.
- Removed unused classes: Plugins should never ship with dead/unused code.
- Implemented Configurate: Serialization from Configurate helps to read complex objects from config easily. Ideal for
  Inventory plugin.
- Changed configuration options from UpperCamelCase -> snake-case: This is a breaking change, but required to implement
  best practices.
- Added support for resource pack sounds via Adventure. Old impl was theoretically limited to sounds in minecraft
  namespace.
- Added support for choosing source of sounds. See: //TODO: link
- Sounds use keys instead of Bukkit enum names. For MC names, see: //TODO: link
- MaxLoreLine config option removed: Lore modification is already slow, adding additional logic to
  shift around lore lines adds even more overhead. Onus is on users to solve this.
- Remove requirement to have SilkSpawners installed for usage of spawners.
- Remove LangUtils support. Outdated plugin.
- [NOT YET CHANGED] Shops will have to be in shops subfolder: Creates easier path to shop registration. No discerning
  between configs and shops.
- [NOT YET CHANGED] Users will have to specify points plugin to use: This is a short term change. One goal of the
  project is to add better support for multiple point types.
- [NOT YET CHANGED] Transaction logging will use a flat file database: Opens up possibility of running basic analytics
  on shop transactions.
- [NOT YET CHANGED] BugFinder.yml will be replaced with a text file: Error logging will take on more of a "log" format.
  This is a compromise in comparison to just ripping out the feature, which is still being considered.
- [NOT YET CHANGED] ClassManager will be removed in favor of Dependency Injection(Guice): Monolithic classes like the
  ClassManager create untestable and non-modular code.