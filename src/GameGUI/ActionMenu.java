package GameGUI;

import GameResources.BattleActionWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GameResources.GameCharacter.ActionType;

public class ActionMenu extends JPanel {

    private enum ActionButtons{
        FLEE, ATTACK, MAGIC, ITEMS, BLOCK;

        private static final ActionButtons[] buttons = {FLEE, ATTACK, MAGIC, ITEMS, BLOCK};
        private static final ActionListener[] actionListeners = {
                new ActionListener(){// FLEE
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                },
                new ActionListener(){// ATTACK
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("attack pressed");
                        worker.signalHeroActionReady(ActionType.ATTACK);
                    }
                },
                new ActionListener(){// MAGIC
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("magic pressed");
                        cardLayout.show(buttonPanel,"Magic");
                    }
                },
                new ActionListener(){// ITEMS
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("item pressed");
                        cardLayout.show(buttonPanel,"Item");
                    }
                },
                new ActionListener(){// BLOCK
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("block pressed");
                        worker.signalHeroActionReady(ActionType.BLOCK);
                    }
                }
        };

        private static ActionButtons getButton(int i){
            return buttons[i];
        }

    }

    private static CardLayout cardLayout;
    private static JPanel buttonPanel;
    private final SpringLayout layout;
    private final JButton[] buttons;
    private static BattleActionWorker worker;

    public ActionMenu(int screenWidth, int screenHeight, CardLayout cardLayout, JPanel buttonPanel){
        ActionMenu.cardLayout = cardLayout;
        ActionMenu.buttonPanel = buttonPanel;
        System.out.println("GameGUI.ActionMenu");
        setPreferredSize(new Dimension(screenWidth*9/20,screenHeight/3));
        setMaximumSize(new Dimension(screenWidth*9/20,screenHeight/3));
        setMinimumSize(new Dimension(screenWidth*9/20,screenHeight/3));
        layout = new SpringLayout();
        this.setLayout(layout);

        buttons = new JButton[5];
        for(int i = 0; i< buttons.length; i++){
            buttons[i] = new JButton(ActionButtons.getButton(i).name());
            buttons[i].addActionListener(ActionButtons.actionListeners[i]);
            if (i == 0) {
                buttons[i].setPreferredSize(new Dimension((int)(screenWidth*0.11), (int)(screenHeight*0.05)));
                buttons[i].setText("⏎");
                buttons[i].setFont(new Font(buttons[i].getFont().getName(), Font.BOLD,20));
            }else
                buttons[i].setPreferredSize(new Dimension((int)(screenWidth*0.18),(int)(screenHeight*0.075)));
            add(buttons[i]);
        }

        layout.putConstraint(SpringLayout.WEST, buttons[ActionButtons.ATTACK.ordinal()],7,SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, buttons[ActionButtons.ATTACK.ordinal()],5,SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, buttons[ActionButtons.MAGIC.ordinal()],5,SpringLayout.EAST, buttons[ActionButtons.ATTACK.ordinal()]);
        layout.putConstraint(SpringLayout.NORTH, buttons[ActionButtons.MAGIC.ordinal()],0,SpringLayout.NORTH, buttons[ActionButtons.ATTACK.ordinal()]);

        layout.putConstraint(SpringLayout.NORTH, buttons[ActionButtons.ITEMS.ordinal()],5,SpringLayout.SOUTH, buttons[ActionButtons.ATTACK.ordinal()]);
        layout.putConstraint(SpringLayout.WEST, buttons[ActionButtons.ITEMS.ordinal()],0,SpringLayout.WEST, buttons[ActionButtons.ATTACK.ordinal()]);

        layout.putConstraint(SpringLayout.WEST, buttons[ActionButtons.BLOCK.ordinal()],5,SpringLayout.EAST, buttons[ActionButtons.ITEMS.ordinal()]);
        layout.putConstraint(SpringLayout.NORTH, buttons[ActionButtons.BLOCK.ordinal()],0,SpringLayout.NORTH, buttons[ActionButtons.ITEMS.ordinal()]);

        layout.putConstraint(SpringLayout.EAST, buttons[ActionButtons.FLEE.ordinal()],3,SpringLayout.EAST, buttons[ActionButtons.BLOCK.ordinal()]);
        layout.putConstraint(SpringLayout.NORTH, buttons[ActionButtons.FLEE.ordinal()],3,SpringLayout.SOUTH, buttons[ActionButtons.BLOCK.ordinal()]);
    }

    public void setWorker(BattleActionWorker worker) {
        ActionMenu.worker = worker;
    }
}
