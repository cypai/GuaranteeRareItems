package com.pipai.starsector.gri

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI
import com.fs.starfarer.api.campaign.SectorAPI
import com.fs.starfarer.api.impl.campaign.procgen.DropGroupRow
import com.fs.starfarer.api.util.Misc
import com.fs.starfarer.api.util.WeightedRandomPicker
import com.pipai.starsector.gri.utils.chooseAmount

class RareItemSpawner(private val sector: SectorAPI) {
    val logger = Global.getLogger(this.javaClass)
    val rare_item_entities = listOf("station_research_remnant", "station_mining_remnant", "orbital_habitat_remnant")

    fun spawnRareItems() {
        val rareItemIds = allRareItemIds()
        val entities = sector.validRareItemEntities()
        logger.info("Found ${entities.size} total valid entities.")
        val chosenSalvageLocations = entities.chooseAmount(rareItemIds.size, Misc.random)
        chosenSalvageLocations.forEach {
            it.addTag("gri_loc")
        }
    }

    private fun allRareItemIds(): List<String> {
        return DropGroupRow.getPicker("rare_tech").items
            .filter { it.isSpecialItem }
            .map { it.specialItemId }
    }

    private fun SectorAPI.validRareItemEntities(): List<CustomCampaignEntityAPI> {
        return this.allLocations
            .map { it.customEntities }
            .flatten()
            .filter { it.customEntityType in rare_item_entities }
    }
}
