package com.example.mixin;

import net.minecraft.network.protocol.game.PacketPlayInChat;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerConnection.class)
public abstract class MixinPlayerConnection {
    @Shadow public abstract CraftPlayer getCraftPlayer();

    @Inject(method = "a(Lnet/minecraft/network/protocol/game/PacketPlayInChat;)V", at = @At("HEAD"))
    public void onChat(PacketPlayInChat packet, CallbackInfo ci) {
        this.getCraftPlayer().sendMessage("You said: " + packet.b());
    }
}
