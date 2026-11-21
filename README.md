# Sentinel Armor (Voidwarden Fabric mod)

The existing post-netherite Voidwarden armor has been visually replaced with the Sentinel design. This is a replacement, not a second armor set.

- Existing item IDs are retained: `voidwarden_helmet`, `voidwarden_chestplate`, `voidwarden_leggings`, `voidwarden_boots`.
- Existing Warden nugget drop, ingot recipe, smithing upgrade, armor stats, and full-set effects remain attached to those items.
- In-game names are Sentinel Crown, Sentinel Cuirass, Sentinel Greaves, and Sentinel Sabatons.
- The replacement includes the supplied Sentinel flat armor textures, item icons, detailed 3D armor geometry, and five-segment spring-driven cape.
- The 3D ornaments and cape layer are registered for player avatars. Other living entities use the flat equipment textures.

## Target

Minecraft 26.3 · Fabric Loader 0.19.5 · Java 25

## Build

The GitHub Actions workflow generates the legacy base assets, assembles the Fabric project, overlays the supplied Sentinel textures, and runs `gradle build`. Successful builds publish the mod JAR as the `sentinel-armor-minecraft-26.3` workflow artifact.

## Source asset regeneration

The Sentinel source archive included generator scripts and a spec. The committed PNGs are the source-of-truth for this integration; the old Voidwarden `gen.py` generator must not be run after the Sentinel overlay step in the build workflow.
