package GameResources;

public class Statistics {
    private int maxHealthPoints;
    private int healthPoints;
    private int maxMagicPoints;
    private int magicPoints;
    private int strength;
    private int defence;
    private int agility;
    private int luck;

    public Statistics(int healthPoints, int magicPoints, int strength, int defence, int agility, int luck){
        this.healthPoints = healthPoints;
        this.maxHealthPoints = healthPoints;
        this.magicPoints = magicPoints;
        this.maxMagicPoints = magicPoints;
        this.strength = strength;
        this.defence = defence;
        this.agility = agility;
        this.luck = luck;
    }

    public Statistics createCopy(){
        return new Statistics(healthPoints, magicPoints, strength, defence, agility, luck);
    }

    @Override
    public String toString() {
        return healthPoints + ", " + magicPoints + ", " + strength + ", " + defence + ", " + agility + ", " + luck;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMagicPoints() {
        return magicPoints;
    }

    public int getStrength() {
        return strength;
    }

    public int getDefence() {
        return defence;
    }

    public int getAgility() {
        return agility;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public int getMaxMagicPoints() {
        return maxMagicPoints;
    }

    public int getLuck() {
        return luck;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setMagicPoints(int magicPoints) {
        this.magicPoints = magicPoints;
    }

    public void decreaseHealthPoints(int amount){
        if (healthPoints - amount <= 0)
            healthPoints = 0;
        else
            healthPoints -= amount;
    }

    public void decreaseMagicPoints(int amount){
        if (magicPoints - amount <= 0)
            magicPoints = 0;
        else
            magicPoints -= amount;
    }

    public void increaseHealthPoints(int amount){
        if (healthPoints + amount >= maxHealthPoints)
            healthPoints = maxHealthPoints;
        else
            healthPoints += amount;
    }

    public void increaseMagicPoints(int amount){
        if (magicPoints + amount >= maxMagicPoints)
            magicPoints = maxMagicPoints;
        else
            magicPoints += amount;
    }
}
