package model;

public class Policy {

    private final String name;
    private final double cost;
    private final int airQualityEffect;
    private final int wasteManagementEffect;
    private final int happinessEffect;

    public Policy(String name, double cost, int airQualityEffect, int wasteManagementEffect, int happinessEffect) {
        this.name = name;
        this.cost = cost;
        this.airQualityEffect = airQualityEffect;
        this.wasteManagementEffect = wasteManagementEffect;
        this.happinessEffect = happinessEffect;
    }

   public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public int getAirQualityEffect() {
        return airQualityEffect;
    }

    public int getWasteManagementEffect() {
        return wasteManagementEffect;
    }

    public int getHappinessEffect() {
        return happinessEffect;
    }
}
