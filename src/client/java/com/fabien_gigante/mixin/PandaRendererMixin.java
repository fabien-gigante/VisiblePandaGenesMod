package com.fabien_gigante.mixin;

import net.minecraft.client.renderer.entity.PandaRenderer;
import net.minecraft.client.renderer.entity.state.PandaRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.panda.Panda;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fabien_gigante.PandaRenderStateExt;
import com.mojang.datafixers.util.Pair;


@Mixin(PandaRenderer.class)
public class PandaRendererMixin {
	@Unique
	private static final Map<Pair<Panda.Gene, Panda.Gene>, Identifier> TEXTURES, BABY_TEXTURES;

	@Overwrite
	public PandaRenderState createRenderState() { return new PandaRenderStateExt(); }

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	public void extractRenderStateExt(Panda panda, PandaRenderState pandaRenderState, float f, CallbackInfo ci) {
		PandaRenderStateExt state = (PandaRenderStateExt)pandaRenderState;
		state.mainGene = panda.getMainGene();
		state.hiddenGene = panda.getHiddenGene();
	}

	@Overwrite
	public Identifier getTextureLocation(PandaRenderState pandaRenderState) {
		PandaRenderStateExt state = (PandaRenderStateExt)pandaRenderState;
		var textures = state.isBaby ? BABY_TEXTURES : TEXTURES;
		return textures.getOrDefault(Pair.of(state.mainGene, state.hiddenGene), textures.get(Pair.of(Panda.Gene.NORMAL, Panda.Gene.NORMAL)));
	}

	static {
		TEXTURES = new java.util.HashMap<>();
		BABY_TEXTURES = new java.util.HashMap<>();
		for(boolean isBaby : new boolean[]{false, true})
			for (Panda.Gene mainGene : Panda.Gene.values())
				for (Panda.Gene hiddenGene : Panda.Gene.values()) {
					String textureName = mainGene.toString().toLowerCase() + "_" + hiddenGene.toString().toLowerCase()
						+ "_panda" + (isBaby ? "_baby" : "") + ".png";
					Identifier textureId = Identifier.withDefaultNamespace("textures/entity/panda/genome/" + textureName);
					(isBaby ? BABY_TEXTURES : TEXTURES).put(Pair.of(mainGene, hiddenGene), textureId);
				}
	}
}