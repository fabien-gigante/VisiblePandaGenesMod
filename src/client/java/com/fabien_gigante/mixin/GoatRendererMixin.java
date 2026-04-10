package com.fabien_gigante.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fabien_gigante.GoatRenderStateExt;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.client.renderer.entity.state.GoatRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.goat.Goat;

@Mixin(GoatRenderer.class)
public class GoatRendererMixin {
    @Unique
    private static final Identifier SCREAMING_TEXTURE = Identifier.withDefaultNamespace("textures/entity/goat/screaming_goat.png");
    @Unique
    private static final Identifier SCREAMING_BABY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/goat/screaming_goat_baby.png");

	@Overwrite
	public GoatRenderState createRenderState() { return new GoatRenderStateExt(); }

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	public void extractRenderStateExt(Goat goat, GoatRenderState goatState, float partialTicks, CallbackInfo ci) {
		GoatRenderStateExt state = (GoatRenderStateExt)goatState;
		state.isScreaming = goat.isScreamingGoat();
	}

    @ModifyReturnValue(method = "getTextureLocation", at = @At("RETURN"))
    public Identifier modifyGetTextureLocation(Identifier original, GoatRenderState goatState) {
        GoatRenderStateExt state = (GoatRenderStateExt)goatState;
        return state.isScreaming ? (state.isBaby ? SCREAMING_BABY_TEXTURE : SCREAMING_TEXTURE) : original;
    }
}
