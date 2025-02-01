package GameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import GameResources.BattleActionWorker;
import GameResources.EnemyDecisionMaker;
import GameResources.GameCharacter;

public class PlayerUI extends JPanel {
    private JPanel textPanel;
    private CardLayout cardLayout;
    private JPanel buttonPanel;
    private ActionMenu actionMenu;
    private MagicMenu magicMenu;
    private ItemMenu itemMenu;
    private JPanel transitionPanel;
    private JTextPane textPane;
    public BattleActionWorker worker;
    private EnemyDecisionMaker enemyDecisionMaker;

    public PlayerUI(int screenWidth, int screenHeight, GameCharacter hero){
        cardLayout = new CardLayout();
        buttonPanel = new JPanel(cardLayout);
        textPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(screenWidth,screenHeight/3));
        System.out.println("GameGUI.PlayerUI");

        textPane = new JTextPane();
        textPane.setFont(FontGetter.getFont("pokemon_font.ttf"));
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textPane.setText("!!!The Battle Has Started!!!");
        textPane.setEditable(false);
        textPane.setFocusable(false);
        textPane.setPreferredSize(new Dimension(textPanel.getWidth()*7/10, textPanel.getHeight()*7/10));

        textPanel.setPreferredSize(new Dimension(screenWidth/2,screenHeight/3));
        textPanel.add(textPane, BorderLayout.CENTER);
        actionMenu = new ActionMenu(screenWidth,screenHeight,cardLayout,buttonPanel);
        magicMenu = new MagicMenu(screenWidth,screenHeight, hero, cardLayout, buttonPanel);
        itemMenu = new ItemMenu(screenWidth,screenHeight,cardLayout,buttonPanel);
        transitionPanel = new JPanel();
        transitionPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentHidden(ComponentEvent e) {
                new Thread(enemyDecisionMaker).start();
            }
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentResized(ComponentEvent e) {}
        });
        buttonPanel.add(transitionPanel,"Transition");
        buttonPanel.add(actionMenu,"Action");
        buttonPanel.add(magicMenu,"Magic");
        buttonPanel.add(itemMenu, "Item");
        add(textPanel,BorderLayout.WEST);
        add(new JSeparator(JSeparator.VERTICAL));
        add(buttonPanel,BorderLayout.EAST);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setWorker(BattleActionWorker worker, GameCharacter enemy) {
        this.worker = worker;
        actionMenu.setWorker(worker);
        magicMenu.setWorker(worker);
        itemMenu.setWorker(worker);
        enemyDecisionMaker = new EnemyDecisionMaker(worker,enemy);
        new Thread(() -> {
            try {
                Thread.sleep(2500);
                worker.transition();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}