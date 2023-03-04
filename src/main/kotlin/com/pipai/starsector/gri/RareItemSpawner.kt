package com.pipai.starsector.gri

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.*
import com.fs.starfarer.api.impl.campaign.procgen.DropGroupRow
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial
import com.fs.starfarer.api.util.Misc
import com.pipai.starsector.gri.utils.chooseAmount

class RareItemSpawner(private val sector: SectorAPI) {
    val rare_item_entities = listOf("station_research_remnant", "station_mining_remnant", "orbital_habitat_remnant")

    fun spawnRareItems() {
        val rareItemIds = allRareItemIds()
        val entities = sector.validRareItemEntities()
        entities.chooseAmount(rareItemIds.size, Misc.random)
            .zip(rareItemIds)
            .forEach { (salvageable, rareItem) ->
                addRareItem(salvageable, rareItem)
//                salvageable.addTag("gri_salvageable")
            }
    }

    private fun addRareItem(salvageable: SectorEntityToken, item: DropGroupRow) {
        val cargo = Global.getFactory().createCargo(true)
        cargo.addItems(CargoAPI.CargoItemType.SPECIAL, SpecialItemData(item.specialItemId, item.specialItemData), 1f)

        BaseSalvageSpecial.addExtraSalvage(cargo, salvageable.memoryWithoutUpdate, -1.0f)
    }

    private fun allRareItemIds(): List<DropGroupRow> {
        return DropGroupRow.getPicker("rare_tech").items
            .filter { it.isSpecialItem }
    }

    private fun SectorAPI.validRareItemEntities(): List<CustomCampaignEntityAPI> {
        return this.allLocations
            .map { it.customEntities }
            .flatten()
            .filter { it.customEntityType in rare_item_entities }
    }
}
