package GameResources;

import GameGUI.ProgressBarHandler;
import GameResources.GameCharacter.ActionType;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BattleActionWorker extends SwingWorker<Object, BattleActionWorker.BattleUpdate> {
    static class BattleUpdate {
        final String message;
        final boolean toActionMenu;
        final boolean toTransition;
        final GameCharacter target;
        final int value;
        final ProgressBarHandler.ProgressBars barType;

        BattleUpdate(String message, boolean toActionMenu, boolean toTransition) {
            this(message, toActionMenu, toTransition, null, 0, null);
        }

        BattleUpdate(String message, GameCharacter target, int value, ProgressBarHandler.ProgressBars barType) {
            this(message, false, false, target, value, barType);
        }

        private BattleUpdate(String message, boolean toActionMenu, boolean toTransition, GameCharacter target, int value, ProgressBarHandler.ProgressBars barType) {
            this.message = message;
            this.toActionMenu = toActionMenu;
            this.toTransition = toTransition;
            this.target = target;
            this.value = value;
            this.barType = barType;
        }
    }

    private final int TIMEOUT = 1250;
    private final CardLayout cardLayout;
    private final JPanel buttonPanel;
    private final JTextPane textPane;
    private final GameCharacter hero;
    private final GameCharacter enemy;
    private final Lock lock;
    private final Condition actionsReady;
    private volatile boolean isHeroActionReady;
    private volatile boolean isEnemyActionReady;
    private final Random random;
    private volatile boolean finish;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Thread transition;
    private boolean blockedAttack = false;

    public BattleActionWorker(JTextPane textPane, CardLayout cardLayout, JPanel buttonPanel, GameCharacter hero, GameCharacter enemy) {
        this.lock = new ReentrantLock();
        this.actionsReady = lock.newCondition();
        this.buttonPanel = buttonPanel;
        this.textPane = textPane;
        this.cardLayout = cardLayout;
        this.hero = hero;
        this.enemy = enemy;
        this.random = new Random();
        transition = new Thread(() -> {
            try {
                Thread.sleep(300);
                transition();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected Object doInBackground() throws Exception {
        while (!finish) {
            lock.lock();
            System.out.println("lock obtained in doInBackground");
            try {
                while (!isHeroActionReady || !isEnemyActionReady) {
                    System.out.println("Entered wait");
                    actionsReady.await();
                }

                System.out.println("exited wait");

                publish(new BattleUpdate("", false,true));

                // Process the battle round
                processBattleRound();

                // Reset states
                isHeroActionReady = false;
                isEnemyActionReady = false;
                blockedAttack = false;

                hero.setBlocked(false);
                enemy.setBlocked(false);

                if (!finish) // Signal transition
                    executor.execute(transition);

            } finally {
                lock.unlock();
                System.out.println("lock released in doInBackground");
            }
        }
        Thread.sleep(1250);
        System.exit(0);
        return null;
    }

    @Override
    protected void process(List<BattleUpdate> updates) {
        for (BattleUpdate update : updates) {
            if (!update.message.isEmpty()) {
                textPane.setText(update.message);
            }

            if (update.target != null) {
                ProgressBarHandler.setValue(update.target, update.value, update.barType);
            }

            if (update.toActionMenu) {
                SwingUtilities.invokeLater(() -> {
                    cardLayout.show(buttonPanel, "Action");
                    textPane.setText("What will you do?");
                });
            }
            if (update.toTransition){
                System.out.println("transitioning");
                cardLayout.show(buttonPanel,"Transition");
                textPane.setText("");
            }
        }
    }

    private void processBattleRound() {
        // Process actions based on speed
        GameCharacter firstActor = determineFirstActor();
        GameCharacter secondActor = (firstActor == hero) ? enemy : hero;

        // Process first action
        processAction(firstActor);

        // Check if battle should continue
        if (!isGameFinished()) {
            // Process second action
            processAction(secondActor);
            isGameFinished();
        }
    }

    private GameCharacter determineFirstActor() {
        int heroSpeed = hero.getStats().getAgility();
        int enemySpeed = enemy.getStats().getAgility();

        if (heroSpeed == enemySpeed) {
            return (random.nextBoolean()) ? hero : enemy;
        }
        return (heroSpeed > enemySpeed) ? hero : enemy;
    }

    private void processAction(GameCharacter actor) {
        switch (actor.getAction()) {
            case ATTACK:
                attack(actor,(actor == hero) ? enemy : hero);
                break;
            case FIRE:
                useMagic(actor,(actor == hero) ? enemy : hero,actor.getFire());
                break;
            case BLIZZARD:
                useMagic(actor,(actor == hero) ? enemy : hero,actor.getBlizzard());
                break;
            case THUNDER:
                useMagic(actor,(actor == hero) ? enemy : hero,actor.getThunder());
                break;
            case BLOCK:
                block(actor);
                break;
            case HEALTH:
                heal(actor);
                break;
            case MP:
                gainMp(actor);
                System.out.println("exited gainMp");
                break;
            default:
                throw new IllegalArgumentException("Illegal Enemy Action Argument");
        }
    }

    private boolean isGameFinished() {
        // Check if either character has been defeated
        if (hero.getStats().getHealthPoints() <= 0) {
            System.out.println(hero.getName() + " has been defeated!!!");
            publish(new BattleUpdate(hero.getName() + " has been defeated!!!", false, false));
            finish = true;
            return true;
        }
        if (enemy.getStats().getHealthPoints() <= 0) {
            System.out.println(enemy.getName() + " has been defeated!!!");
            publish(new BattleUpdate(enemy.getName() + " has been defeated!!!", false, false));
            finish = true;
            return true;
        }
        return false;
    }

    private void attack(GameCharacter attacker, GameCharacter target){
        float crit = attacker.getCritical();
        float damage = attacker.getStats().getStrength() * crit;

        if (target.getBlocked())
            damage -= target.getStats().getDefence();

        if (damage <= 0){
            blockedAttack = true;
            System.out.println(attacker.getName() + "'s attack was blocked!");
            publish(new BattleUpdate(target.getName() + " completely blocked " + attacker.getName() + "'s attack!", false, false));
        }else {
            target.getStats().decreaseHealthPoints((int) damage);
            if (crit == 1.5f) {
                System.out.println(attacker.getName() + "'s attack was critical!");
                publish(new BattleUpdate(attacker.getName() + " attacked!\nIt was critical!!!",
                        target, target.getStats().getHealthPoints(),
                        ProgressBarHandler.ProgressBars.HP));
            } else {
                System.out.println(attacker.getName() + " attacked!");
                publish(new BattleUpdate(attacker.getName() + " attacked!",
                        target, target.getStats().getHealthPoints(),
                        ProgressBarHandler.ProgressBars.HP));
            }
        }

        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
            throw new RuntimeException(e);
        }
    }

    private void useMagic(GameCharacter attacker, GameCharacter target, Magic magic){
        float crit = attacker.getCritical();
        float damage = magic.getStrength() * crit;
        if (target.getBlocked())
            damage -= target.getStats().getDefence();

        if (damage <= 0){
            blockedAttack = true;
            System.out.println(attacker.getName() + "'s " + magic.getName() + " was blocked!");
            publish(new BattleUpdate(target.getName() + " completely blocked " + attacker.getName() + "'s " + magic.getName() + "!",
                    false, false));
        }else {
            target.getStats().decreaseHealthPoints((int) damage);

            if (crit == 1.5f) {
                System.out.println(attacker.getName() + "'s " + magic.getName() + " was critical!");
                publish(new BattleUpdate(attacker.getName() + " used " + magic.getName() + "!\nIt was critical!!!",
                        target, target.getStats().getHealthPoints(), ProgressBarHandler.ProgressBars.HP));
            } else {
                System.out.println(attacker.getName() + " used " + magic.getName() + "!");
                publish(new BattleUpdate(attacker.getName() + " used " + magic.getName() + "!",
                        target, target.getStats().getHealthPoints(), ProgressBarHandler.ProgressBars.HP));
            }
        }
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
            throw new RuntimeException(e);
        }
        decreaseMp(attacker,magic);
    }

    private void decreaseMp(GameCharacter attacker, Magic magic){
        attacker.getStats().decreaseMagicPoints(magic.getCost());
        publish(new BattleUpdate("", attacker, attacker.getStats().getMagicPoints(), ProgressBarHandler.ProgressBars.MP));
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
            throw new RuntimeException(e);
        }
    }

    private void heal(GameCharacter healer){
        healer.getStats().increaseHealthPoints(5);
        System.out.println(healer.getName() + " used a health potion!");
        publish(new BattleUpdate(healer.getName() + " used a health potion!",
                healer, healer.getStats().getHealthPoints(), ProgressBarHandler.ProgressBars.HP));
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
            throw new RuntimeException(e);
        }
    }

    private void gainMp(GameCharacter potionUser){
        potionUser.getStats().increaseMagicPoints(5);
        System.out.println(potionUser.getName() + " used a magic potion!");
        publish(new BattleUpdate(potionUser.getName() + " used a magic potion!",
                potionUser, potionUser.getStats().getMagicPoints(), ProgressBarHandler.ProgressBars.MP));
        try {
            System.out.println("entered try block");
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
            throw new RuntimeException(e);
        }
        System.out.println("exited try block");
        System.out.println("exiting gainMp");
    }

    private void block(GameCharacter blocker){
        if (blockedAttack)
            return;
        System.out.println(blocker.getName() + " blocked!");
        publish(new BattleUpdate(blocker.getName() + " blocked!",false, false));
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
            throw new RuntimeException(e);
        }
    }

    public void signalHeroActionReady(ActionType action) {
        lock.lock();
        System.out.println("entered hero signal");
        try {
            if (!isHeroActionReady) {
                if (action == ActionType.BLOCK)
                    hero.setBlocked(true);

                hero.setAction(action);
                isHeroActionReady = true;
                if (isEnemyActionReady) {
                    System.out.println("signaled from hero");
                    actionsReady.signal();
                }
            }
        } finally {
            System.out.println("lock released by hero");
            lock.unlock();
        }
    }

    public void signalEnemyActionReady(ActionType action) {
        lock.lock();
        System.out.println("entered enemy signal");
        try {
            if (!isEnemyActionReady) {
                if (action == ActionType.BLOCK)
                    enemy.setBlocked(true);

                enemy.setAction(action);
                isEnemyActionReady = true;
                if (isHeroActionReady) {
                    System.out.println("signaled from enemy");
                    actionsReady.signal();
                }
            }
        } finally {
            System.out.println("lock released by enemy");
            lock.unlock();
        }
    }

    public boolean canEnemyAct() {
        lock.lock();
        System.out.println("lock obtained by canEnemyAct");
        try {
            return !isEnemyActionReady;
        } finally {
            lock.unlock();
            System.out.println("lock released by canEnemyAct");
        }
    }

    public void transition() throws InterruptedException {
        System.out.println("entered transition func");
        publish(new BattleUpdate("", true, false));
        System.out.println("exited transition func");
    }
}