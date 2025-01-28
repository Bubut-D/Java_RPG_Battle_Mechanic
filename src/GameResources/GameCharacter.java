package GameResources;

import java.util.Random;

public class GameCharacter {
    public enum ActionType{
        ATTACK, FIRE, BLIZZARD, THUNDER, BLOCK, HEALTH, MP;
        private static ActionType[] actionTypes= {ATTACK, FIRE, BLIZZARD, THUNDER, BLOCK, HEALTH, MP};

        public static ActionType getActionTypeByIndex(int index){
            return actionTypes[index];
        }
        public static int getLength(){
            return actionTypes.length;
        }
    }
    private transient ActionType action;// 1 -> attack | 2 -> magic | 3 -> block | 4 -> item
    private transient Random random;
    private transient boolean blocked = false;

    private String genre;
    private String name;
    private Statistics stats;
    private Magic fire;
    private Magic blizzard;
    private Magic thunder;


    public GameCharacter(String genre, String name, Statistics stats, Magic fire, Magic blizzard, Magic thunder){
        random = new Random();
        this.genre = genre;
        this.name = name;
        this.stats = stats;
        this.fire = fire;
        this.blizzard = blizzard;
        this.thunder = thunder;
        System.out.println(ActionType.values()[0]);
    }

    public GameCharacter createCopy(){
        GameCharacter copy = new GameCharacter(genre, name, stats.createCopy(), fire.createCopy(), blizzard.createCopy(), thunder.createCopy());
        return copy;
    }

    @Override
    public String toString() {
        return genre + ", " + name + ": " + stats + ", " + fire + ", " + blizzard + ", " + thunder;
    }

    public String getGenre() {
        return genre;
    }
    public String getName() {
        return name;
    }
    public Statistics getStats() {
        return stats;
    }
    public Magic getFire() {
        return fire;
    }
    public Magic getBlizzard() {
        return blizzard;
    }
    public Magic getThunder() {
        return thunder;
    }
    public ActionType getAction() {
        return action;
    }
    public void setAction(ActionType action) {
        this.action = action;
    }
    public float getCritical() {
        if (stats.getLuck() >= random.nextInt(101))
            return 1.5f;
        return 1f;
    }
    public void setBlocked(boolean value){
        blocked = value;
    }
    public boolean getBlocked(){
        return blocked;
    }
}
