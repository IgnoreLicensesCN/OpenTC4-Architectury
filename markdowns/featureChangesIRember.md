* use mekanism_recommend_resources to mine resource and some outer blocks to gather resource or avoid them makes trouble. *i may have to add tags well one day.*
* blocks below will no longer be able to activate beacon:
* * //tile.blockCosmeticSolid.0.name=Obsidian Totem
* * //tile.blockCosmeticSolid.1.name=Obsidian Tile
* * //tile.blockCosmeticSolid.2.name=Paving Stone of Travel
* * //tile.blockCosmeticSolid.3.name=Paving Stone of Warding
* * 
* * //tile.blockCosmeticSolid.5.name=Tallow Block
* *   //tile.blockCosmeticSolid.6.name=Arcane Stone Block
* *   //tile.blockCosmeticSolid.7.name=Arcane Stone Bricks
* *   //tile.blockCosmeticSolid.8.name=Charged Obsidian Totem
* *   //tile.blockCosmeticSolid.9.name=Golem Fetter
* *   //tile.blockCosmeticSolid.10.name=Active Golem Fetter
* *   //tile.blockCosmeticSolid.11.name=Ancient Stone
* *   //tile.blockCosmeticSolid.12.name=Ancient Rock
* *   //tile.blockCosmeticSolid.13.name=Ancient Stone
* *   //tile.blockCosmeticSolid.14.name=Crusted Stone
* *   //tile.blockCosmeticSolid.15.name=Ancient Stone Pedestal
* creating arcane table with wand will no longer place wand inside automatically. also for ink well and Research table
* inkwell can work with unbreaking enchantment(if there is enchantment for some reason)
* IRepairable renamed to IRepairEnchantable which can be enchanted Repair,and any item with Repair enchantment can be repaired
* IRepairableExtended renamed to IRepairable which can be called doRepair to repair(even without enchantment).However,if want to repair as component,you should impl IInventoryTickableComponent and call doRepair.