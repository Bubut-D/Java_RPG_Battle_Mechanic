package TextBasedGame;

import GameResources.GameCharacter;
import GameResources.Magic;
import GameResources.Statistics;

import java.util.ArrayList;

public class GameTextPrinter {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String BRIGHT_GREEN = "\u001B[92m";    // Middle Earth (forest theme)
    private static final String BRIGHT_BLUE = "\u001B[94m";     // Caribbean (ocean theme)
    private static final String BRIGHT_CYAN = "\u001B[96m";     // Cyberpunk (neon theme)
    private static final String BRIGHT_YELLOW = "\u001B[93m";   // Mythological (divine theme)
    private static final String BRIGHT_RED = "\u001B[91m";      // Doom (hell theme)

    private static String getGenreColor(String genre) {
        return switch (genre) {
            case "Middle Earth" -> BRIGHT_GREEN;
            case "Caribbean" -> BRIGHT_BLUE;
            case "Cyberpunk" -> BRIGHT_CYAN;
            case "Mythological" -> BRIGHT_YELLOW;
            case "Doom" -> BRIGHT_RED;
            default -> RESET;
        };
    }

    public static void printCharacterTable(ArrayList<GameCharacter> characters) {
        // Define column widths
        int idWidth = 4;
        int genreWidth = 15;
        int nameWidth = 15;
        int statsWidth = 8;
        int magicGroupWidth = 16;  // Width for combined strength/cost
        int id = 1;

        // Print main header
        printMainTableDivider(idWidth, genreWidth, nameWidth, statsWidth, magicGroupWidth);
        System.out.printf("| %-2s " + "| %-" + (genreWidth-2) + "s | %-" + (nameWidth-2) + "s | %-6s | %-6s | %-6s | %-6s | %-6s | %-6s | %-14s | %-14s | %-14s |\n",
                "ID", "Genre", "Name", "HP", "MP", "STR", "DEF", "AGI", "LUCK",
                "Fire", "Blizzard", "Thunder");

        // Print subheader for magic attributes
        printMagicSubheader(idWidth, genreWidth, nameWidth, statsWidth, magicGroupWidth);

        // Print each character's data
        for (GameCharacter character : characters) {
            String genre = character.getGenre();
            String colorCode = getGenreColor(genre);
            Statistics stats = character.getStats();
            Magic fire = character.getFire();
            Magic blizzard = character.getBlizzard();
            Magic thunder = character.getThunder();

            // Format magic attributes as "STR/COST"
            String fireStr = String.format("%3d / %-3d", fire.getStrength(), fire.getCost());
            String blizzStr = String.format("%3d / %-3d", blizzard.getStrength(), blizzard.getCost());
            String thunderStr = String.format("%3d / %-3d", thunder.getStrength(), thunder.getCost());

            System.out.printf("| %-2d " + "| %s%-" + (genreWidth-2) + "s%s | %s%-" + (nameWidth-2) + "s%s | %s%6d%s | %s%6d%s | %s%6d%s | %s%6d%s | %s%6d%s | %s%6d%s | %s%-14s%s | %s%-14s%s | %s%-14s%s |\n",
                    id++,
                    colorCode, genre, RESET,
                    colorCode, character.getName(), RESET,
                    colorCode, stats.getHealthPoints(), RESET,
                    colorCode, stats.getMagicPoints(), RESET,
                    colorCode, stats.getStrength(), RESET,
                    colorCode, stats.getDefence(), RESET,
                    colorCode, stats.getAgility(), RESET,
                    colorCode, stats.getLuck(), RESET,
                    colorCode, fireStr, RESET,
                    colorCode, blizzStr, RESET,
                    colorCode, thunderStr, RESET
            );
        }
        printMainTableDivider(idWidth, genreWidth, nameWidth, statsWidth, magicGroupWidth);
    }

    private static void printMainTableDivider(int idWidth, int genreWidth, int nameWidth, int statsWidth, int magicGroupWidth) {
        System.out.print("+");
        printDash(idWidth);
        System.out.print("+");
        printDash(genreWidth);
        System.out.print("+");
        printDash(nameWidth);
        System.out.print("+");
        for (int i = 0; i < 6; i++) {
            printDash(statsWidth);
            System.out.print("+");
        }
        for (int i = 0; i < 3; i++) {
            printDash(magicGroupWidth);
            System.out.print("+");
        }
        System.out.println();
    }

    private static void printMagicSubheader(int idWidth, int genreWidth, int nameWidth, int statsWidth, int magicGroupWidth) {
        // Print divider
        printMainTableDivider(idWidth, genreWidth, nameWidth, statsWidth, magicGroupWidth);

        // Print stats section placeholder
        String statsPlaceholder = String.format("| %-2s " + "| %-" + (genreWidth-2) + "s | %-" + (nameWidth-2) + "s |", "", "", "");
        for (int i = 0; i < 6; i++) {
            statsPlaceholder += String.format(" %-6s |", "");
        }

        // Print magic subheader
        System.out.printf("%s %-6s %-7s | %-6s %-7s | %-6s %-7s |\n",
                statsPlaceholder,
                "STR /", "COST",
                "STR /", "COST",
                "STR /", "COST");

        printMainTableDivider(idWidth, genreWidth, nameWidth, statsWidth, magicGroupWidth);
    }

    private static void printDash(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print("-");
        }
    }

    public static void printBattleStarted(){
        System.out.println();
        System.out.println(BRIGHT_RED + "!!! The Battle Has Started !!!" + RESET);
        System.out.println();
    }

    public static void printStatusTable(GameCharacter hero, GameCharacter enemy){
        System.out.print("+");
        printDash(30);
        System.out.print("+");
        printDash(30);
        System.out.print("+\n");

        System.out.printf("| %s%-28s%s | %s%-28s%s |\n", getGenreColor(hero.getGenre()), hero.getName(), RESET, getGenreColor(enemy.getGenre()), enemy.getName(), RESET);
        System.out.printf(  "| %sHP%s : %s" + BarManager.getBar(hero.getStats().getHealthPoints(),hero.getStats().getMaxHealthPoints()) + "%s %3d/%-3d " +
                            "| %sHP%s : %s" + BarManager.getBar(enemy.getStats().getHealthPoints(),enemy.getStats().getMaxHealthPoints()) + "%s %3d/%-3d |\n",
                            BRIGHT_GREEN, RESET, BRIGHT_GREEN, RESET, hero.getStats().getHealthPoints(),hero.getStats().getMaxHealthPoints(),
                            BRIGHT_GREEN, RESET, BRIGHT_GREEN, RESET, enemy.getStats().getHealthPoints(),enemy.getStats().getMaxHealthPoints());

        System.out.printf(  "| %sMP%s : %s" + BarManager.getBar(hero.getStats().getMagicPoints(),hero.getStats().getMaxMagicPoints()) + "%s %3d/%-3d " +
                            "| %sMP%s : %s" + BarManager.getBar(enemy.getStats().getMagicPoints(),enemy.getStats().getMaxMagicPoints()) + "%s %3d/%-3d |\n",
                            BRIGHT_BLUE, RESET, BRIGHT_BLUE, RESET, hero.getStats().getMagicPoints(),hero.getStats().getMaxMagicPoints(),
                            BRIGHT_BLUE, RESET, BRIGHT_BLUE, RESET, enemy.getStats().getMagicPoints(),enemy.getStats().getMaxMagicPoints());

        System.out.print("+");
        printDash(30);
        System.out.print("+");
        printDash(30);
        System.out.print("+\n");
    }

    public static void printActionTable(){
        System.out.print(BRIGHT_YELLOW);
        System.out.print("+");
        printDash(11);
        System.out.print("+");
        printDash(10);
        System.out.print("+");
        printDash(10);
        System.out.print("+");
        printDash(9);
        System.out.print("+");
        printDash(9);
        System.out.print("+\n");

        System.out.println("| 1. Strike | 2. GameResources.Magic | 3. Block | 4. Item | 0. Flee |");

        System.out.print("+");
        printDash(11);
        System.out.print("+");
        printDash(10);
        System.out.print("+");
        printDash(10);
        System.out.print("+");
        printDash(9);
        System.out.print("+");
        printDash(9);
        System.out.print("+\n");

        System.out.println("What do you want to do? (1, 2, 3, 4, 0): ");

        System.out.print(RESET);
    }

    public static void printMagicTable(GameCharacter hero){
        System.out.print(BRIGHT_YELLOW);
        System.out.print("+");
        printDash(26);
        System.out.print("+");
        printDash(30);
        System.out.print("+");
        printDash(29);
        System.out.print("+");
        printDash(12);
        System.out.print("+\n");

        System.out.printf("| 1. Fire ( Cost = %2d MP ) | 2. Blizzard ( Cost = %2d MP ) | 3. Thunder ( Cost = %2d MP ) | 0. Go Back |\n", hero.getFire().getCost(), hero.getBlizzard().getCost(), hero.getThunder().getCost());

        System.out.print("+");
        printDash(26);
        System.out.print("+");
        printDash(30);
        System.out.print("+");
        printDash(29);
        System.out.print("+");
        printDash(12);
        System.out.print("+\n");

        System.out.println("What magic do you want to cast? (1, 2, 3, 0): ");

        System.out.print(RESET);
    }
}