package TextBasedGame;

import GameResources.GameCharacter;
import GameResources.JsonCharacterReader;
import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private static ArrayList<GameCharacter> heroes;
    private static ArrayList<GameCharacter> enemies;
    private static GameCharacter hero;
    private static GameCharacter enemy;
    private static Scanner scanner;

    private enum ActionMenu{
        STRIKE, MAGIC, BLOCK, ITEM, FLEE
    }
    private enum MagicMenu{
        FIRE, BLIZZARD, THUNDER, BACK
    }

    static {
        scanner = new Scanner(System.in);
    }

    public static void startGame(){
        chooseHero();
        chooseEnemy();
        wait(500);
        GameTextPrinter.printBattleStarted();
        wait(500);
        GameTextPrinter.printStatusTable(hero,enemy);


    }

    public static void heroAction(GameCharacter hero){
        do{
            GameTextPrinter.printActionTable();
            switch(scanner.nextInt()){
                case 1:; //Attack State
                case 2: ;//GameResources.Magic State
                case 3: ;//Block State
                case 4: ;//Item State
                case 0: ;//Flee State    
            }
            int cost;
            try {
                cost = switch(scanner.nextInt()){
                    case 1 -> hero.getFire().getCost();
                    case 2 -> hero.getBlizzard().getCost();
                    case 3 -> hero.getThunder().getCost();
                    case 0 -> 0;
                    default -> throw new IllegalArgumentException("Illegal Index of GameResources.Magic Menu");
                };
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                continue;
            }

            if (cost == 0)
                continue;



        }while(true);
    }

    public static void chooseHero(){
        heroes = JsonCharacterReader.getFromJson("heroes.json5");
        GameTextPrinter.printCharacterTable(heroes);
        System.out.println("Choose your hero:");
        hero = heroes.get(scanner.nextInt() - 1).createCopy();
    }

    public static void chooseEnemy(){
        enemies = JsonCharacterReader.getFromJson("enemies.json5");
        GameTextPrinter.printCharacterTable(enemies);
        System.out.println("Choose your enemy:");
        enemy = enemies.get(scanner.nextInt() - 1).createCopy();
    }

    public static ArrayList<GameCharacter> getHeroes() {
        return heroes;
    }
    public static ArrayList<GameCharacter> getEnemies() {
        return enemies;
    }

    public static void wait(int ms) {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
}
