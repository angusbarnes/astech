package net.astr0.astrocraft.farming;

import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CropGenome {

    // =============================
    // ===== CONFIGURABLE VALUES ===
    // =============================
    public static final double BASE_MUTATION_RATE = 0.50;
    public static final double RECESSIVE_MUTATION_MULT = 1.5;
    public static final double HETEROZYGOUS_MUTATION_MULT = 1.0;
    public static final double DOMINANT_MUTATION_MULT = 0.5;
    public static final double DOMINANT_PAIR_BUFF = 1.1;
    public static final double RECESSIVE_PAIR_NERF = 0.9;
    public static final double HETEROZYGOUS_NERF = 0.9;
    public static final double HETERO_GLOBAL_NERF = 0.95;
    public static final double REGRESSIVE_MUTATION_CHANCE = 0.15;
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
    private final double growth;
    private final double gain;
    private final double resistance;
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

        public static Tier previous(Tier t) {
            return switch (t) {
                case S -> A;
                case A -> B;
                case B -> C;
                case C -> D;
                case D -> E;
                case E -> F;
                default -> t; // F remains F
            };
        }
    }

    // ==========================================
    // =========== STAT ACCESSORS ===============
    // ==========================================
    // These are now O(1) field reads

    public double getGrowth() { return growth; }
    public double getGain() { return gain; }
    public double getResistance() { return resistance; }
    public double getModifierMutationMultiplier() { return mutationMod; }

    // ==========================================
    // =========== CALCULATION LOGIC ============
    // ==========================================
    // (Run once during construction)

    private double computeLocus(int index) {
        char a = genome.charAt(index);
        char b = genome.charAt(index + 1);

        Tier tierA = Tier.fromChar(a);
        Tier tierB = Tier.fromChar(b);

        if (tierA.value() == 0 && tierB.value() == 0) return 0.0;

        boolean isUpperA = Character.isUpperCase(a);
        boolean isUpperB = Character.isUpperCase(b);
        boolean sameTier = Character.toUpperCase(a) == Character.toUpperCase(b);

        // --- CASE 1, 2, 3: SAME TIER (e.g., AA, aa, Aa) ---
        if (sameTier) {
            double base = tierA.value();

            if (isUpperA && isUpperB) {
                return base * DOMINANT_PAIR_BUFF; // Rule 1: BB, AA
            } else if (!isUpperA && !isUpperB) {
                return base * RECESSIVE_PAIR_NERF; // Rule 2: bb, aa
            } else {
                return base; // Rule 3: Bb, aA (Base Case)
            }
        }

        // --- CASE 4, 5, 6: DIFFERENT TIERS (Heterozygous) ---
        double result;

        if (isUpperA && isUpperB) {
            // Rule 4: Both dominant, higher value takes precedence
            result = Math.max(tierA.value(), tierB.value());
        }
        else if (!isUpperA && !isUpperB) {
            // Rule 5: Both recessive, lower value takes precedence
            result = Math.min(tierA.value(), tierB.value());
        }
        else {
            // Rule 6: Mixed dominance, the dominant allele wins
            result = isUpperA ? tierA.value() : tierB.value();
        }

        // Rule 7: Apply flat nerf to all heterozygous pairs
        return result * HETERO_GLOBAL_NERF;
    }

    private double computeModifier() {
        char a = genome.charAt(6);
        char b = genome.charAt(7);

        Tier tierA = Tier.fromChar(a);
        Tier tierB = Tier.fromChar(b);

        // 1. Reserved Tiers (Prioritized)
        // Using OR ensures that if either allele is X/G, the effect triggers.
        if (tierA == Tier.X || tierB == Tier.X) return 2.0; // The "Chaos" Gene
        if (tierA == Tier.G || tierB == Tier.G) return 1.5; // The "Evolution" Gene

        boolean isUpperA = Character.isUpperCase(a);
        boolean isUpperB = Character.isUpperCase(b);

        // 2. Stability vs. Volatility (Homozygous Check)
        // We only apply these if the tiers match (e.g., AA or aa).
        if (Character.toUpperCase(a) == Character.toUpperCase(b)) {
            if (isUpperA && isUpperB) return 0.75;  // Dominant Homozygous: Very Stable
            if (!isUpperA && !isUpperB) return 1.25; // Recessive Homozygous: More Volatile
        }

        // 3. Base Case (Heterozygous or Mixed Dominance)
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

        for (int locus = 0; locus < 4; locus++) {

            int i = locus * 2;
            double chance = getMutationChance(i);

            if (random.nextDouble() < chance) {
                int alleleIndex = i + random.nextInt(2);
                chars[alleleIndex] = mutateAllele(chars[alleleIndex], random);
            }
        }

        return CropGenome.of(new String(chars)); // Cached return
    }

    private double getMutationChance(int index) {
        char a = genome.charAt(index);
        char b = genome.charAt(index + 1);

        boolean isUpperA = Character.isUpperCase(a);
        boolean isUpperB = Character.isUpperCase(b);
        boolean sameTier = Character.toUpperCase(a) == Character.toUpperCase(b);

        double localMultiplier;

        if (sameTier) {
            // CASE: Same Tier (e.g., AA, aa, Aa)
            if (isUpperA && isUpperB) {
                // Dominant Homozygous: The "End Goal" - highly stable.
                localMultiplier = DOMINANT_MUTATION_MULT;
            } else if (!isUpperA && !isUpperB) {
                // Recessive Homozygous: The "Mutation Engine" - highly volatile.
                localMultiplier = RECESSIVE_MUTATION_MULT;
            } else {
                // Mixed Dominance (Aa/aA): The "Stable Hybrid" - baseline.
                localMultiplier = HETEROZYGOUS_MUTATION_MULT;
            }
        } else {
            // CASE: Different Tiers (e.g., AB, ab, Ab)
            // These are transition states. We treat them as neutral baseline.
            localMultiplier = HETEROZYGOUS_MUTATION_MULT;
        }

        // Apply the Global Modifier (from slots 7 & 8) and the Base Rate
        // this.mutationMod is the pre-calculated field from computeModifier()
        return BASE_MUTATION_RATE * localMultiplier * this.mutationMod;
    }

    private char mutateAllele(char allele, Random random) {
        Tier tier = Tier.fromChar(allele);

        // G and X are "immutable" or "perfected" genes that don't shift
        if (tier == Tier.G || tier == Tier.X) return allele;

        boolean isUpper = Character.isUpperCase(allele);
        boolean isRegressive = random.nextDouble() < REGRESSIVE_MUTATION_CHANCE;

        if (isRegressive) {
            // --- REGRESSIVE MUTATION (The Setback) ---
            if (isUpper) {
                // Dominant -> Recessive (e.g., 'A' -> 'a')
                // Stays in same tier but loses the dominance buff.
                return Character.toLowerCase(allele);
            } else {
                // Recessive -> Previous Tier Dominant (e.g., 'b' -> 'A')
                // Drops a tier but gains dominance.
                Tier prev = Tier.previous(tier);
                return Character.toUpperCase(prev.name().charAt(0));
            }
        } else {
            // --- PROGRESSIVE MUTATION (Standard) ---
            if (!isUpper) {
                // Recessive -> Dominant (e.g., 'a' -> 'A')
                return Character.toUpperCase(allele);
            } else {
                // Dominant -> Next Tier Recessive (e.g., 'A' -> 'b')
                Tier next = Tier.next(tier);
                return Character.toLowerCase(next.name().charAt(0));
            }
        }
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