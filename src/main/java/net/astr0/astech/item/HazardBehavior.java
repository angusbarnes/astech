package net.astr0.astech.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class HazardBehavior {

    private final Behavior behavior;

    public HazardBehavior(BehaviorType behaviorType) {
        this.behavior = behaviorType.getBehavior();
    }

    // Call the behavior's logic in the update loop
    public void apply(LivingEntity entity, Level level) {
        behavior.execute(entity, level);
    }

    public interface Behavior {
        void execute(LivingEntity entity, Level level);
    }

    public enum BehaviorType {
        HEAT((entity, level) -> {}), // Set entity on fire somehow
        RADIO((entity, level) -> {
            // Modify this as needed for hazardous materials
            // Might be able to tap into mekanism radiation???? We have the API after all
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 200), pAttacker);
        }),
        EXPLOSION((entity, level) -> {
            level.explode(null, entity.position().x +0.5, entity.position().y +0.5, entity.position().z +0.5, 3, true, Level.ExplosionInteraction.NONE);
            stack.setCount(0);

            DamageSource source = level.damageSources().genericKill();
            if (entity instanceof Player player) {
                player.hurt(source, 200f);
            }
        }),
        NONE((entity, level) -> {}),
        SUFFOCATE((entity, level) -> {}); // If we cant make actual suffocation whilst holding, we should just apply wither

        private final Behavior behavior;

        BehaviorType(Behavior behavior) {
            this.behavior = behavior;
        }

        public Behavior getBehavior() {
            return this.behavior;
        }
    }
}
