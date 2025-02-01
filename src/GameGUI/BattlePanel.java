package GameGUI;

import javax.swing.*;
import java.awt.*;
import GameResources.*;

public class BattlePanel extends JPanel{
    private EnemyPanel enemyPanel;
    private PlayerPanel playerPanel;
    private PlayerUI ui;
    GameCharacter hero;
    GameCharacter enemy;
    BattleActionWorker worker;

    public BattlePanel(GameCharacter hero, GameCharacter enemy, int screenWidth, int screenHeight){
        System.out.println("BattlePanel");
        this.hero = hero;
        this.enemy = enemy;
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        enemyPanel = new EnemyPanel(enemy,screenWidth,screenHeight);
        add(enemyPanel);
        playerPanel = new PlayerPanel(hero, screenWidth, screenHeight);
        add(playerPanel);
        ui = new PlayerUI(screenWidth,screenHeight,hero);
        worker = new BattleActionWorker(ui.getTextPane(),ui.getCardLayout(),ui.getButtonPanel(),hero,enemy);
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

}
