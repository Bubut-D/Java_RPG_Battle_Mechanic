package GameResources;

import java.util.Random;
import GameResources.GameCharacter.ActionType;


public class EnemyDecisionMaker implements Runnable {
    private BattleActionWorker worker;
    private Random random;
    private Magic fire;
    private Magic blizzard;
    private Magic thunder;
    private Statistics statistics;


    private static int decisionCount = 0;


    public EnemyDecisionMaker(BattleActionWorker worker, GameCharacter enemy) {
        this.worker = worker;
        fire = enemy.getFire();
        blizzard = enemy.getBlizzard();
        thunder = enemy.getThunder();
        statistics = enemy.getStats();
        random = new Random();
    }

    @Override
    public void run() {
        try{
            while (!worker.canEnemyAct()) {
                Thread.sleep(50);  // Short sleep to prevent busy waiting
            }

            decisionCount++;
            System.out.println("Enemy making decision #" + decisionCount);

            while (true) {
                ActionType action = ActionType.getActionTypeByIndex(random.nextInt(ActionType.getLength()));

                if (action == ActionType.FIRE && statistics.getMagicPoints() < fire.getCost())
                    continue;
                else if (action == ActionType.BLIZZARD && statistics.getMagicPoints() < blizzard.getCost())
                    continue;
                else if (action == ActionType.THUNDER && statistics.getMagicPoints() < thunder.getCost())
                    continue;

                System.out.println("Enemy decision #" + decisionCount + " made: " + action);
                worker.signalEnemyActionReady(action);
                break;
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.out.println("Error in enemy decision #" + decisionCount + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
