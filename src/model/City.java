package model;

public class City {

    public static final int AIR_QUALITY_GOAL = 75;
    public static final int WASTE_MANAGEMENT_GOAL = 80;
    public static final int HAPPINESS_GOAL = 70;
    public static final int MAX_TURNS = 20;

    private int airQuality;
    private int wasteManagement;
    private int happiness;
    private double budget;
    private int currentTurn;

    public City() {
        this.airQuality = 30;
        this.wasteManagement = 25;
        this.happiness = 50;
        this.budget = 1_000_000.00;
        this.currentTurn = 1;
    }

    public void applyPolicyEffects(Policy policy) {
        if (policy.getCost() > 0) {
           if (this.budget >= policy.getCost()){
                this.budget -= policy.getCost();
           } else {
               return;
           }
        }
        
        this.airQuality = Math.min(100, Math.max(0, this.airQuality + policy.getAirQualityEffect()));
        this.wasteManagement = Math.min(100, Math.max(0, this.wasteManagement + policy.getWasteManagementEffect()));
        this.happiness = Math.min(100, Math.max(0, this.happiness + policy.getHappinessEffect()));
    }

    public void nextTurn() {
        if (this.currentTurn <= MAX_TURNS) {
            this.currentTurn++;
            this.budget += 50_000.00;
        }
    }

    public void addRevenue(double amount) {
        this.budget += amount;
    }

    public void applyHappinessEffect(int effect) {
        this.happiness = Math.min(100, Math.max(0, this.happiness + effect));
    }

    public void applyAirQualityEffect(int effect) {
        this.airQuality = Math.min(100, Math.max(0, this.airQuality + effect));
    }

    public int getAirQuality() {
        return airQuality;
    }

    public int getWasteManagement() {
        return wasteManagement;
    }

    public int getHappiness() {
        return happiness;
    }

    public double getBudget() {
        return budget;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
}