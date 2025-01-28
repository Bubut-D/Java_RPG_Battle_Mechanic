package GameResources;

public class Magic {
    private String name;
    private int strength;
    private int cost;

    public Magic(String name, int strength, int cost){
        this.name = name;
        this.strength = strength;
        this.cost = cost;
    }

    public Magic createCopy(){
        return new Magic(name, strength, cost);
    }

    @Override
    public String toString() {
        return name + ": " + strength + ", " + cost;
    }

    public int getCost() {
        return cost;
    }

    public int getStrength() {
        return strength;
    }

    public String getName() {
        return name;
    }
}
