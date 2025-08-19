# 📍 Waypoints & Holograms

This guide explains how to configure waypoints and holograms used by quests. These elements can be defined globally and referenced by name, or configured directly inside a quest.

## Global configuration

Both features have a dedicated file in the plugin's data folder:

- `holograms.yml`
- `waypoints.yml`

### `holograms.yml`

```yml
holograms:
  - name: EXAMPLE_1
    location: "world,320.8,10,-9.5"
    text: "%img_pnj_talk%"
    scale: 2
    billboard: VERTICAL
    see-through: true
```

#### Fields

- **`name`** *(required)* – Identifier used by quests to reference this hologram.
- **`location`** or **`locations`** *(required)* – One location string or a list of locations. Each location uses the format `world,x,y,z` and may include `yaw,pitch`.
- **`text`** or **`texts`** – Lines shown in the hologram. Default: `"Hummm, you need to add a text !"`.
- **`billboard`** – Orientation of the text. Default: `CENTER`. Other options: `FIXED`, `HORIZONTAL`, `VERTICAL`.
- **`scale`** – Size of the hologram. Accepts a single value (`2`) or comma‑separated values (`"1,2,1"`). Default: `1` for all axes.
- **`translation-x`**, **`translation-y`**, **`translation-z`** – Offset from the base location. Default: `0` for each axis.
- **`brightness-block`**, **`brightness-sky`** – Light levels used to illuminate the hologram. Default: `15` for both.
- **`shadow-radius`** – Size of the text shadow. Default: `0`.
- **`shadow-strength`** – Darkness of the text shadow. Default: `1.0`.
- **`visibility-distance`** – Maximum distance at which the hologram is visible. Default: `-1` (use engine default).
- **`text-background`** – Background color. Use a hex code (`#ff00ff`), a color name (`red`), `transparent`, or `default` to remove the background.
- **`text-alignment`** – Alignment of each line. Default: `CENTER`. Other options: `LEFT`, `RIGHT`.
- **`text-shadow`** – Whether the text has a shadow. Default: `false`.
- **`see-through`** – If `true`, the hologram can be seen through blocks. Default: `false`.

### `waypoints.yml`

```yml
waypoints:
  - name: EXAMPLE_1
    location: "world,320.8,10,-9.5"
    texture: example
    color: red
```

#### Fields

- **`name`** *(required)* – Identifier used by quests to reference this waypoint.
- **`location`** *(required)* – Target location in the format `world,x,y,z` (yaw and pitch are optional).
- **`texture`** – Icon displayed by the waypoint provider. Default: none.
- **`color`** – Color of the beacon beam. Accepts common color names or hex codes. Default: `white`.

## Using in quests

Reference a global definition by name:

```yml
hologram: EXAMPLE_1
waypoint: EXAMPLE_1
```

Or define them inline within a quest:

```yml
hologram:
  location: "world,100,65,200"
  texts:
    - "&aQuest here"

waypoint:
  location: "world,100,65,200"
  texture: example
  color: red
```

## Notes

- These features require a compatible plugin such as **zEssentials** to display holograms and waypoints.
- Automatic updates depend on `update-hologram` and `update-waypoint` settings in `config.yml`.
