package GameGUI;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import GameResources.*;
public class BattlePanel extends JPanel implements PropertyChangeListener {
    private EnemyPanel enemyPanel;
    private PlayerPanel playerPanel;
    private PlayerUI ui;
    GameCharacter hero;
    GameCharacter enemy;
    BattleActionWorker worker;

    private static boolean[][] changedBar;//         Player | Enemy
                                         // Health:    []      []
                                         // MP:        []      []

    public BattlePanel(GameCharacter hero, GameCharacter enemy, int screenWidth, int screenHeight){
        System.out.println("BattlePanel");
        this.hero = hero;
        this.enemy = enemy;
        changedBar = new boolean[2][2];
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        enemyPanel = new EnemyPanel(enemy,screenWidth,screenHeight);
        add(enemyPanel);
        playerPanel = new PlayerPanel(hero, screenWidth, screenHeight);
        add(playerPanel);
        ui = new PlayerUI(screenWidth,screenHeight,hero);
        worker = new BattleActionWorker(ui.getTextPane(),ui.getCardLayout(),ui.getButtonPanel(),hero,enemy);
        worker.addPropertyChangeListener(this);
        worker.execute();
        ui.setWorker(worker,enemy);
        ProgressBarHandler.setProgressBars(new GameCharacter[]{hero,enemy},new JProgressBar[]{playerPanel.getStatusPanel().getHp(),playerPanel.getStatusPanel().getMp(),enemyPanel.getStatusPanel().getHp(),enemyPanel.getStatusPanel().getMp()});
        add(ui);
    }

    public static void main(String[] args) {
        Statistics stats1  = new Statistics(100,50,40,30,20,10);
        Statistics stats2  = new Statistics(120,35,25,15,50,20);

        Magic fire = new Magic("Fire Breath",5,1);
        Magic blizzard = new Magic("Blizzard Breath",4,2);
        Magic thunder = new Magic("Thunder Breath",3,3);

        GameCharacter hero = new GameCharacter("Pokemon", "Charizard", stats1, fire, blizzard, thunder);
        GameCharacter enemy = new GameCharacter("Pokemon", "Dark Charizard", stats2, fire, blizzard, thunder);
        BattlePanel panel =  new BattlePanel(hero, enemy, 450,570);

        JFrame frame = new JFrame("Pokémon");
        frame.add(panel);
        frame.setSize(450,570);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"progress".equals(evt.getPropertyName()))
            return;

        JProgressBar bar;

        try {
            if (changedBar[0][0]){
                System.out.println("hero health changed");
                bar = playerPanel.getStatusPanel().getHp();
                bar.setString("HP: " + evt.getNewValue() + "/" + hero.getStats().getMaxHealthPoints());
            }
            else if (changedBar[0][1]) {
                System.out.println("enemy health changed");
                bar = enemyPanel.getStatusPanel().getHp();
                bar.setString("HP: " + evt.getNewValue() + "/" + enemy.getStats().getMaxHealthPoints());
            }
            else if (changedBar[1][0]) {
                System.out.println("hero mp changed");
                bar = playerPanel.getStatusPanel().getMp();
                bar.setString("MP: " + evt.getNewValue() + "/" + hero.getStats().getMaxMagicPoints());
            }
            else if (changedBar[1][1]) {
                System.out.println("enemy health changed");
                bar = enemyPanel.getStatusPanel().getHp();
                bar.setString("MP: " + evt.getNewValue() + "/" + enemy.getStats().getMaxMagicPoints());
            }
            else
                throw new Exception("Invalid bar status change!");

            bar.setValue((Integer) evt.getNewValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void clearChangedBar(){
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                changedBar[i][j] = false;
    }
    public static void setChangedBar(int row, int collumn){
        clearChangedBar();
        changedBar[row][collumn] = true;
    }
}
