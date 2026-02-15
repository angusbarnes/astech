package net.astr0.astrocraft.farming;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class CropGenome {

    // =============================
    // ===== CONFIGURABLE VALUES ===
    // =============================

    public static final double BASE_MUTATION_RATE = 0.005; // 0.5%
    public static final double RECESSIVE_MUTATION_MULT = 1.5;
    public static final double HETEROZYGOUS_MUTATION_MULT = 1.0;
    public static final double DOMINANT_MUTATION_MULT = 0.5;

    public static final double DOMINANT_PAIR_BUFF = 1.1;
    public static final double RECESSIVE_PAIR_NERF = 0.9;

    public static final String NBT_KEY = "Genome";

    public static final CropGenome DEFAULT =
            new CropGenome("FfFfFfFf");

    private final String genome; // Always length 8

    public CropGenome(String genome) {
        if (genome == null || genome.length() != 8)
            throw new IllegalArgumentException("Genome must be 8 characters.");
        this.genome = genome;
    }

    public String genome() {
        return genome;
    }

    // ==========================================
    // =========== TIER SYSTEM ==================
    // ==========================================

    public enum Tier {
        F(1),
        E(2),
        D(3),
        C(4),
        B(5),
        A(6),
        S(7),
        G(0), // reserved
        X(0); // reserved

        private final int value;

        Tier(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static Tier fromChar(char c) {
            char u = Character.toUpperCase(c);
            for (Tier t : values()) {
                if (t.name().charAt(0) == u) return t;
            }
            return F;
        }

        public static Tier next(Tier t) {
            return switch (t) {
                case F -> E;
                case E -> D;
                case D -> C;
                case C -> B;
                case B -> A;
                case A -> S;
                default -> t;
            };
        }
    }

    // ==========================================
    // =========== STAT CALCULATION =============
    // ==========================================

    public int getGrowth() {
        return computeLocus(0);
    }

    public int getGain() {
        return computeLocus(2);
    }

    public int getResistance() {
        return computeLocus(4);
    }

    private int computeLocus(int index) {
        char a = genome.charAt(index);
        char b = genome.charAt(index + 1);

        Tier tierA = Tier.fromChar(a);
        Tier tierB = Tier.fromChar(b);

        if (tierA.value() == 0 && tierB.value() == 0) {
            return 0; // reserved case
        }

        // Homozygous
        if (Character.toUpperCase(a) == Character.toUpperCase(b)) {
            int base = tierA.value();

            if (Character.isUpperCase(a) && Character.isUpperCase(b)) {
                return (int) Math.round(base * DOMINANT_PAIR_BUFF);
            } else {
                return (int) Math.round(base * RECESSIVE_PAIR_NERF);
            }
        }

        // Heterozygous: only dominant contributes
        char dominant = Character.isUpperCase(a) ? a :
                Character.isUpperCase(b) ? b : a;

        Tier t = Tier.fromChar(dominant);
        return t.value();
    }

    // ==========================================
    // =========== CROSSBREED ===================
    // ==========================================

    public CropGenome cross(CropGenome other, Random random) {
        StringBuilder result = new StringBuilder(8);

        for (int i = 0; i < 8; i += 2) {
            char a1 = this.genome.charAt(i + random.nextInt(2));
            char a2 = other.genome.charAt(i + random.nextInt(2));

            result.append(a1).append(a2);
        }

        return new CropGenome(result.toString());
    }

    // ==========================================
    // =========== MUTATION =====================
    // ==========================================

    public CropGenome mutate(Random random) {
        char[] chars = genome.toCharArray();

        for (int locus = 0; locus < 4; locus++) {

            int i = locus * 2;
            double chance = getMutationChance(i);

            if (random.nextDouble() < chance) {
                int alleleIndex = i + random.nextInt(2);
                chars[alleleIndex] = mutateAllele(chars[alleleIndex]);
            }
        }

        return new CropGenome(new String(chars));
    }

    private double getMutationChance(int index) {
        char a = genome.charAt(index);
        char b = genome.charAt(index + 1);

        boolean homozygous =
                Character.toUpperCase(a) == Character.toUpperCase(b);

        if (homozygous) {
            if (Character.isUpperCase(a)) {
                return BASE_MUTATION_RATE * DOMINANT_MUTATION_MULT;
            } else {
                return BASE_MUTATION_RATE * RECESSIVE_MUTATION_MULT;
            }
        }

        return BASE_MUTATION_RATE * HETEROZYGOUS_MUTATION_MULT;
    }

    private char mutateAllele(char allele) {

        Tier tier = Tier.fromChar(allele);

        if (tier == Tier.G || tier == Tier.X)
            return allele;

        boolean isUpper = Character.isUpperCase(allele);

        if (!isUpper) {
            // recessive -> dominant
            return Character.toUpperCase(allele);
        }

        // dominant -> next tier recessive
        Tier next = Tier.next(tier);
        return Character.toLowerCase(next.name().charAt(0));
    }

    // ==========================================
    // =========== MODIFIER LOCUS ===============
    // ==========================================

    public double getModifierMutationMultiplier() {
        char a = genome.charAt(6);
        char b = genome.charAt(7);

        Tier t = Tier.fromChar(a);

        if (t == Tier.X) return 2.0;
        if (t == Tier.G) return 1.5;

        if (Character.isUpperCase(a) && Character.isUpperCase(b))
            return 0.75;

        if (Character.isLowerCase(a) && Character.isLowerCase(b))
            return 1.25;

        return 1.0;
    }

    // ==========================================
    // =========== NBT SERIALIZATION ============
    // ==========================================

    public static CropGenome fromStack(ItemStack stack) {
        if (stack.hasTag() && stack.getTag() != null &&
                stack.getTag().contains(NBT_KEY)) {

            String genome = stack.getTag().getString(NBT_KEY);
            if (genome.length() == 8)
                return new CropGenome(genome);
        }

        return DEFAULT;
    }

    public ItemStack applyToStack(ItemStack stack) {
        if (stack.isEmpty()) return stack;

        if (this.equals(DEFAULT)) {
            if (stack.hasTag() && stack.getTag() != null) {
                stack.getTag().remove(NBT_KEY);
                if (stack.getTag().isEmpty()) {
                    stack.setTag(null);
                }
            }
            return stack;
        }

        CompoundTag root = stack.getOrCreateTag();
        root.putString(NBT_KEY, genome);

        return stack;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CropGenome other &&
                other.genome.equals(this.genome);
    }

    @Override
    public int hashCode() {
        return genome.hashCode();
    }

    @Override
    public String toString() {
        return genome;
    }
}
