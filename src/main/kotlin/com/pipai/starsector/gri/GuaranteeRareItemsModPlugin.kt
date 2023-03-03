package com.pipai.starsector.gri

import com.fs.starfarer.api.BaseModPlugin
import com.fs.starfarer.api.Global

class GuaranteeRareItemsModPlugin : BaseModPlugin() {
    override fun onNewGameAfterProcGen() {
        Global.getLogger(this.javaClass).info("Guaranteeing rare items...")
        RareItemSpawner(Global.getSector()).spawnRareItems()
    }
}
