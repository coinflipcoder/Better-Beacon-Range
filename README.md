# Better Beacon Range

Makes the Minecraft beacon effect range configurable.

By default, this mod uses the vanilla values.

This can be changed in the config file: `config/better_beacon_range.json`

Value | Default | Type | Description
--- | --- | --- | ---
base | 10.0 | double | base range
perLevel | 10.0 | double | added range per beacon level
belowInfinite | false | boolean | infinite effect range below

The calculation is the following: `range = beaconLevel * perLevel + base;`

Download this mod on [Modrinth](https://modrinth.com/project/better-beacon-range-was-taken)!
