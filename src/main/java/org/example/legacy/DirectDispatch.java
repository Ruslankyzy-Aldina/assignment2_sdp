package org.example.legacy;

public final class DirectDispatch {
    public static final class RoadPlanner { public int cost(int kg) { return 40 + 2 * kg; } }
    public static final class AirPlanner { public int cost(int kg) { return 200 + 8 * kg; } }
    public static final class SeaPlanner { public int cost(int kg) { return 100 + kg; } }
    public static final class RoadLabel { public String encode(int cost) { return "ROAD|" + cost; } }
    public static final class AirLabel { public String encode(int cost) { return "AIR/" + cost; } }
    public static final class SeaLabel { public String encode(int cost) { return "SEA:" + cost; } }
    public static String dispatch(String family, int kg) {
        if (family.equals("road")) return new RoadLabel().encode(new RoadPlanner().cost(kg));
        if (family.equals("air")) return new AirLabel().encode(new AirPlanner().cost(kg));
        if (family.equals("sea")) return new SeaLabel().encode(new SeaPlanner().cost(kg));
        throw new IllegalArgumentException("Unknown transport: " + family);
    }
    public static int replacementQuote(String family, int kg) {
        if (family.equals("road")) return new RoadPlanner().cost(kg);
        if (family.equals("air")) return new AirPlanner().cost(kg);
        if (family.equals("sea")) return new SeaPlanner().cost(kg);
        throw new IllegalArgumentException("Unknown transport: " + family);
    }
    public static String accidentalMix(int kg) {
        return new AirLabel().encode(new RoadPlanner().cost(kg));
    }
}
