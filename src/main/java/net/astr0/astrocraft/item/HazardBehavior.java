package net.astr0.astrocraft.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


//TODO: Rework hazard behaviours to bring all equipment and entities checks inside
// This will reduce code duplication and allow for more dynamic conditions per behaviour
public class HazardBehavior {

    private final Behavior behavior;
    private final BehaviorType type;

    public HazardBehavior(BehaviorType behaviorType) {
        this.behavior = behaviorType.getBehavior();
        type = behaviorType;
    }

    // Call the behavior's logic in the update loop
    public void apply(ItemStack stack, LivingEntity entity, Level level) {
        behavior.execute(stack, entity, level);
    }

    public boolean hasBehaviour() {
        return type != BehaviorType.NONE;
    }

    public interface Behavior {
        void execute(ItemStack stack, LivingEntity entity, Level level);
    }

    public enum BehaviorType {
        HEAT((stack, entity, level) -> {
            entity.setSecondsOnFire(3);
            entity.setSharedFlagOnFire(true);
        }), // Set entity on fire somehow
        RADIO((stack, entity, level) -> {
            // Modify this as needed for hazardous materials
            // Might be able to tap into mekanism radiation???? We have the API after all
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 2000), null);
        }),
        EXPLOSION((stack, entity, level) -> {

            CompoundTag tag = stack.getTag();

            if(tag == null)  {
                tag = new CompoundTag();
                stack.setTag(tag);
            }

            if(tag.contains("danger_ttl")) {
                int count = tag.getInt("danger_ttl");

                // We run all the other logic on both sides, so we can display a count,
                // but the explosion, damage and item removal should happen server side only
                if(count <= 0) {
                    level.explode(null, entity.position().x +0.5, entity.position().y +0.5, entity.position().z +0.5, 3, true, Level.ExplosionInteraction.NONE);
                    stack.setCount(0);

                    DamageSource source = level.damageSources().genericKill();
                    if (entity instanceof Player player) {
                        player.hurt(source, 200f);
                    }
                }
                tag.putInt("danger_ttl", count - 1);

            } else {
                tag.putInt("danger_ttl", 4);
            }
        }),
        NONE((stack, entity, level) -> {}),
        SUFFOCATE((stack, entity, level) -> entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200), null)), // If we cant make actual suffocation whilst holding, we should just apply wither
        FREEZE((stack, entity, level) -> {
            entity.setIsInPowderSnow(true);
        }),
        EXTREME((stack, entity, level) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER,200));
            entity.addEffect(new MobEffectInstance(MobEffects.POISON,2000));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION,400));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,80));
            entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS,200));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,600));
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION,600));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,200));
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,5));
        });

        private final Behavior behavior;

        BehaviorType(Behavior behavior) {
             this.behavior = behavior;
        }

        public Behavior getBehavior() {
            return this.behavior;
        }
    }
}
