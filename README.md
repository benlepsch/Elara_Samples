### Sample code for Elara reader, from ThingMagic

This is being used with the [ThingMagic RAIN Starter Kit](https://www.atlasrfidstore.com/thingmagic-rain-starter-kit/), which has limited functionality so some of the code doesn't work with the board.

### Limitations found (so far)
 - Can't adjust power level of the reader with `SET_RZ_READ_POWER` 
 - Can't enter "HDR" mode for single-tag reading (reader only reads the closest tag)
