package net.astr0.astrocraft.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class EnergySword extends SwordItem {

    // Unique UUID for the reach modifier to avoid conflicts
    private static final UUID REACH_MODIFIER_UUID = UUID.fromString("78322558-fd3b-479c-8a7d-d3093919869a");

    private final Multimap<Attribute, AttributeModifier> lightningAttributeModifiers;

    public EnergySword(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties.durability(5));

        // Build the attribute modifiers including the custom Entity Reach
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        // Add existing sword attributes (Damage and Speed)
        builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND));

        // Add Reach Distance (+2.0 blocks over the default 3.0, totaling 5.0)
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(REACH_MODIFIER_UUID, "Reach modifier", 2.5, AttributeModifier.Operation.MULTIPLY_TOTAL));

        this.lightningAttributeModifiers = builder.build();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof net.minecraft.world.entity.player.Player player) {
            // 1. Check if the attack is fully charged (Stamina mechanic)
            float attackStrength = player.getAttackStrengthScale(0.5f);

            if (attackStrength >= 0.95f) { // Trigger only if 90% or higher charged
                if (!player.level().isClientSide()) {
                    ServerLevel level = (ServerLevel) player.level();

                    // 2. Determine if it's a Critical Hit
                    // Vanilla crit: falling, not on ground, not on ladder, not in water
                    boolean isCrit = player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater();

                    if (isCrit) {
                        // Flurry of lightning (3 bolts around the target)
                        for (int i = 0; i < 3; i++) {
                            double offsetX = (level.random.nextDouble() - 0.5) * 3.0;
                            double offsetZ = (level.random.nextDouble() - 0.5) * 3.0;
                            spawnLightning(level, target.blockPosition().offset((int)offsetX, 0, (int)offsetZ));
                        }
                    } else {
                        // Standard single bolt
                        spawnLightning(level, target.blockPosition());
                    }

//                    // 3. Optional: Add an Ender-Pearl style cooldown (e.g., 4 seconds)
//                    // This prevents the lightning from triggering again even if they swing
//                    player.getCooldowns().addCooldown(this, 80);
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    private void spawnLightning(ServerLevel level, BlockPos pos) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(bolt);
        }
    }

    /**
     * Ensures our reach modifier is only active when held in the main hand.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.lightningAttributeModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
