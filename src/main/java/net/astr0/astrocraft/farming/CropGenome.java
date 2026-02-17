package net.astr0.astrocraft.farming;

import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CropGenome {

    // =============================
    // ===== CONFIGURABLE VALUES ===
    // =============================
    public static final double BASE_MUTATION_RATE = 0.005;
    public static final double RECESSIVE_MUTATION_MULT = 1.5;
    public static final double HETEROZYGOUS_MUTATION_MULT = 1.0;
    public static final double DOMINANT_MUTATION_MULT = 0.5;
    public static final double DOMINANT_PAIR_BUFF = 1.1;
    public static final double RECESSIVE_PAIR_NERF = 0.9;
    public static final String NBT_KEY = "Genome";

    // --- OPTIMIZATION 1: Global Cache ---
    // Stores unique instances. Thread-safe.
    private static final Map<String, CropGenome> CACHE = new ConcurrentHashMap<>();

    public static final CropGenome DEFAULT = CropGenome.of("FfFfFfFf");

    // =============================
    // ===== IMMUTABLE FIELDS ======
    // =============================
    private final String genome;
    // --- OPTIMIZATION 2: Pre-calculated Stats ---
    private final int growth;
    private final int gain;
    private final int resistance;
    private final double mutationMod;

    // Private constructor forces use of factory
    private CropGenome(String genome) {
        if (genome == null || genome.length() != 8)
            throw new IllegalArgumentException("Genome must be 8 characters.");
        this.genome = genome;

        // Calculate once, store forever
        this.growth = computeLocus(0);
        this.gain = computeLocus(2);
        this.resistance = computeLocus(4);
        this.mutationMod = computeModifier();
    }

    // Factory Method: Use this instead of 'new'
    public static CropGenome of(String genome) {
        return CACHE.computeIfAbsent(genome, CropGenome::new);
    }

    public String genome() { return genome; }

    // ==========================================
    // =========== TIER SYSTEM ==================
    // ==========================================

    public enum Tier {
        F(1), E(2), D(3), C(4), B(5), A(6), S(7), G(0), X(0);

        private final int value;
        // --- OPTIMIZATION 3: Fast Lookup Table ---
        private static final Tier[] LOOKUP = new Tier[128]; // ASCII lookup

        static {
            // Pre-fill lookup table for instant access
            for (int i = 0; i < 128; i++) LOOKUP[i] = F; // Default
            for (Tier t : values()) {
                char c = t.name().charAt(0);
                LOOKUP[c] = t;
                LOOKUP[Character.toLowerCase(c)] = t; // Handle lowercase too
            }
        }

        Tier(int value) { this.value = value; }

        public int value() { return value; }

        public static Tier fromChar(char c) {
            if (c >= 128) return F; // Safety
            return LOOKUP[c];
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
    // =========== STAT ACCESSORS ===============
    // ==========================================
    // These are now O(1) field reads

    public int getGrowth() { return growth; }
    public int getGain() { return gain; }
    public int getResistance() { return resistance; }
    public double getModifierMutationMultiplier() { return mutationMod; }

    // ==========================================
    // =========== CALCULATION LOGIC ============
    // ==========================================
    // (Run once during construction)

    private int computeLocus(int index) {
        char a = genome.charAt(index);
        char b = genome.charAt(index + 1);

        Tier tierA = Tier.fromChar(a);
        Tier tierB = Tier.fromChar(b);

        if (tierA.value() == 0 && tierB.value() == 0) return 0;

        // Homozygous
        if (Character.toUpperCase(a) == Character.toUpperCase(b)) {
            int base = tierA.value();
            if (Character.isUpperCase(a) && Character.isUpperCase(b)) {
                return (int) Math.round(base * DOMINANT_PAIR_BUFF);
            } else {
                return (int) Math.round(base * RECESSIVE_PAIR_NERF);
            }
        }

        // Heterozygous
        Tier dominant = (Character.isUpperCase(a)) ? tierA :
                (Character.isUpperCase(b)) ? tierB : tierA;
        return dominant.value();
    }

    private double computeModifier() {
        char a = genome.charAt(6);
        char b = genome.charAt(7);
        Tier t = Tier.fromChar(a);

        if (t == Tier.X) return 2.0;
        if (t == Tier.G) return 1.5;
        if (Character.isUpperCase(a) && Character.isUpperCase(b)) return 0.75;
        if (Character.isLowerCase(a) && Character.isLowerCase(b)) return 1.25;
        return 1.0;
    }

    // ==========================================
    // =========== OPERATIONS ===================
    // ==========================================

    public CropGenome cross(CropGenome other, Random random) {
        // Same logic, but use 'of()' at the end
        StringBuilder result = new StringBuilder(8);
        for (int i = 0; i < 8; i += 2) {
            char a1 = this.genome.charAt(i + random.nextInt(2));
            char a2 = other.genome.charAt(i + random.nextInt(2));
            result.append(a1).append(a2);
        }
        return CropGenome.of(result.toString()); // Cached return
    }

    public CropGenome mutate(Random random) {
        // Same logic, but use 'of()' at the end
        char[] chars = genome.toCharArray();
        // ... (your existing mutation loop logic) ...

        // We calculate chance based on THIS genome's pre-calculated mod
        // But individual locus chance is still dynamic per pair
        // ...

        return CropGenome.of(new String(chars)); // Cached return
    }

    // ==========================================
    // =========== SERIALIZATION ================
    // ==========================================

    public static CropGenome fromStack(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NBT_KEY)) {
            String g = stack.getTag().getString(NBT_KEY);
            if (g.length() == 8) return CropGenome.of(g); // Cached
        }
        return DEFAULT;
    }

    public ItemStack applyToStack(ItemStack stack) {
        if (stack.isEmpty()) return stack;

        // Fast equality check thanks to Interning (optional but safe)
        if (this.equals(DEFAULT)) {
            if (stack.hasTag()) {
                assert stack.getTag() != null;
                stack.getTag().remove(NBT_KEY);
                if (stack.getTag().isEmpty()) stack.setTag(null);
            }
            return stack;
        }

        stack.getOrCreateTag().putString(NBT_KEY, genome);
        return stack;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Reference check is fast!
        return obj instanceof CropGenome other && other.genome.equals(this.genome);
    }

    @Override
    public int hashCode() {
        return genome.hashCode();
    }
}