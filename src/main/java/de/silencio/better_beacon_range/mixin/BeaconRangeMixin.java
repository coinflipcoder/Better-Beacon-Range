package de.silencio.better_beacon_range.mixin;

import de.silencio.better_beacon_range.BeaconConfig;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(BeaconBlockEntity.class)
public class BeaconRangeMixin {

    private static BeaconConfig config = new BeaconConfig();

    static {
        config.loadConfig();
    }

    @Inject(method = "applyPlayerEffects", at = @At("HEAD"), cancellable = true)
    private static void modifyBeaconRange(World world, BlockPos pos, int beaconLevel, @Nullable RegistryEntry<StatusEffect> primaryEffect, @Nullable RegistryEntry<StatusEffect> secondaryEffect, CallbackInfo ci) {
        double base = config.getBase();
        double perLevel = config.getPerLevel();
        double customRange = beaconLevel * perLevel + base;

        ci.cancel();

        applyPlayerEffects(world, pos, beaconLevel, primaryEffect, secondaryEffect, customRange);
    }

    @Unique
    private static void applyPlayerEffects(World world, BlockPos pos, int beaconLevel,
                                           @Nullable RegistryEntry<StatusEffect> primaryEffect,
                                           @Nullable RegistryEntry<StatusEffect> secondaryEffect,
                                           double customRange) {

        if (!world.isClient && primaryEffect != null) {
            int i = 0;
            if (beaconLevel >= 4 && Objects.equals(primaryEffect, secondaryEffect)) {
                i = 1;
            }

            int j = (9 + beaconLevel * 2) * 20;
            Box box = new Box(pos).expand(customRange).stretch(0.0, world.getHeight(), 0.0);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);

            for (PlayerEntity playerEntity : list) {
                playerEntity.addStatusEffect(new StatusEffectInstance(primaryEffect, j, i, true, true));
            }

            if (beaconLevel >= 4 && !Objects.equals(primaryEffect, secondaryEffect) && secondaryEffect != null) {
                for (PlayerEntity playerEntity : list) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(secondaryEffect, j, 0, true, true));
                }
            }
        }
    }
}
