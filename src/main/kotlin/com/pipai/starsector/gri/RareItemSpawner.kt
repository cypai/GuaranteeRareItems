package com.pipai.starsector.gri

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial
import com.fs.starfarer.api.util.Misc
import com.pipai.starsector.gri.utils.chooseAmount
import data.scripts.util.MagicSettings

class RareItemSpawner(private val sector: SectorAPI) {

    private val modId = "guarantee-rare-items"
    private val rareItemIds: List<String> = MagicSettings.getList(modId, "rare_items")
    private val salvageLocations: List<String> = MagicSettings.getList(modId, "salvage_locations")

    fun spawnRareItems() {
        val theRareItemIds = availableRareItemIds()
        salvageLocations(sector)
            .chooseAmount(theRareItemIds.size, Misc.random)
            .zip(theRareItemIds)
            .forEach { (salvageable, rareItem) ->
                addRareItem(salvageable, rareItem)
                salvageable.addTag("gri_salvageable")
            }
    }

    private fun availableRareItemIds(): List<String> {
        val loadedIds = Global.getSettings().allSpecialItemSpecs.map { it.id }
        return rareItemIds.filter { loadedIds.contains(it) }
    }

    private fun addRareItem(salvageable: SectorEntityToken, itemId: String) {
        val cargo = Global.getFactory().createCargo(true)
        cargo.addItems(CargoAPI.CargoItemType.SPECIAL, SpecialItemData(itemId, null), 1f)

        BaseSalvageSpecial.addExtraSalvage(cargo, salvageable.memoryWithoutUpdate, -1.0f)
    }

    private fun salvageLocations(sector: SectorAPI): List<CustomCampaignEntityAPI> {
        return sector.allLocations
            .map { it.customEntities }
            .flatten()
            .filter { it.customEntityType in salvageLocations }
    }
}
