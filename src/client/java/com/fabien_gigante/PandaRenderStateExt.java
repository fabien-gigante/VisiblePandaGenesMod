package com.fabien_gigante;

import net.minecraft.client.renderer.entity.state.PandaRenderState;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.entity.animal.panda.Panda.Gene;

public class PandaRenderStateExt extends PandaRenderState {
    public Panda.Gene mainGene = Gene.NORMAL;
    public Panda.Gene hiddenGene = Gene.NORMAL;
}