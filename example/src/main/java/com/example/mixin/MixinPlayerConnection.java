package com.example.mixin;

import net.minecraft.server.v1_15_R1.PacketPlayInChat;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerConnection.class)
public abstract class MixinPlayerConnection {
    @Shadow public abstract CraftPlayer getPlayer();

    @Inject(method = "a(Lnet/minecraft/server/v1_15_R1/PacketPlayInChat;)V", at = @At("HEAD"))
    public void onChat(PacketPlayInChat packet, CallbackInfo ci) {
        this.getPlayer().sendMessage("You said: " + packet.b());
    }
}
