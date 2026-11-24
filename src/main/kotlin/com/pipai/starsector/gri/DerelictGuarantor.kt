package com.pipai.starsector.gri

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI
import com.fs.starfarer.api.campaign.SectorAPI
import com.fs.starfarer.api.campaign.SectorEntityToken
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial
import com.fs.starfarer.api.util.Misc
import org.magiclib.util.MagicSettings

class DerelictGuarantor(private val sector: SectorAPI) {

    private val logger = Global.getLogger(this.javaClass)

    private val modId = "guarantee-rare-items"
    private val hullVariantIds: List<String> = MagicSettings.getList(modId, "hull_variant_ids")

    fun guaranteeRecoveries() {
        hullVariantIds.forEach { variant ->
            val derelicts = derelictLocations(sector, variant)
            if (derelicts.isEmpty()) {
                logger.warn("Could not find any derelicts for variant ${variant}.")
            } else {
                val derelict = derelicts.random()
                makeRecoverable(derelict)
                derelict.addTag("gri_recoverable")
            }
        }
    }

    fun makeRecoverable(entity: SectorEntityToken) {
        val creator = SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null)
        val specialData = creator.createSpecial(entity, null) as ShipRecoverySpecial.ShipRecoverySpecialData
        specialData.storyPointRecovery = true
        Misc.setSalvageSpecial(entity, specialData)
    }

    private fun derelictLocations(sector: SectorAPI, variant: String): List<CustomCampaignEntityAPI> {
        return sector.allLocations.flatMap { it.customEntities }
            .filter { it.customPlugin is DerelictShipEntityPlugin }
            .filter {
                val plugin = it.customPlugin as DerelictShipEntityPlugin
                plugin.data.ship.variantId == variant
            }
    }
}
