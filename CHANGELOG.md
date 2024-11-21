🗃️**Detailed Changelog: [1.3.1 --> 1.3.2](https://github.com/UltimatChamp/BetterGrassify/compare/1.3.1+fabric.1.21.3...1.3.2+fabric.1.21.3)**

<hr>

### 🛠️Changes

- Added support for blockstates (block properties), as attributes, to excluded/whitelisted blocks. [[See **#37**]](https://github.com/UltimatChamp/BetterGrassify/issues/37#issuecomment-2489936492)
  - For example,
    - `lantern[hanging]` or `lantern[hanging=true]`
    - `lantern[hanging=false]`
- Added `soul_wall_torch`, `lantern[hanging]` and `soul_lantern[hanging]` to `Excluded Blocks`.
- Fixed the new `Whitelisted Tags` and `Whitelisted Blocks` options not working. 😅
- Fixed **Better Snow** being applied to `tall ferns`, `sugarcanes`, `farmlands` and other non-full-cube blocks.  [**[#38]**](https://github.com/UltimatChamp/BetterGrassify/issues/38)
