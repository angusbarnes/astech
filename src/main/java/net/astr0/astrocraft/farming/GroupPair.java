package net.astr0.astrocraft.farming;

public record GroupPair(String first, String second) {
    public GroupPair {
        // Null-safe string handling
        String strA = (first != null) ? first : "";
        String strB = (second != null) ? second : "";

        if (strA.compareTo(strB) > 0) {
            String temp = first;
            first = second;
            second = temp;
        }
    }

    @Override
    public String toString() {
        return "GroupPair [first=" + first + ", second=" + second + "]";
    }
}
