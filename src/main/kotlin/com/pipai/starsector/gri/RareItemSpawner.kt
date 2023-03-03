package com.pipai.starsector.gri

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI
import com.fs.starfarer.api.campaign.SectorAPI

class RareItemSpawner(private val sector: SectorAPI) {
    val logger = Global.getLogger(this.javaClass)
    val RARE_ITEM_ENTITIES = listOf("station_research_remnant", "station_mining_remnant", "orbital_habitat_remnant")

    fun spawnRareItems() {
        allRareItemTypes()
        val entities = sector.validRareItemEntities()
        logger.info("Found ${entities.size} total valid entities.")
    }

    private fun allRareItemTypes() {
        Global.getSettings().allSpecialItemSpecs
            .forEach { logger.info("${it.id} ${it.tags}") }
    }

    private fun SectorAPI.validRareItemEntities(): List<CustomCampaignEntityAPI> {
        return this.allLocations
            .map { it.customEntities }
            .flatten()
            .filter { it.customEntityType in RARE_ITEM_ENTITIES }
    }
}
