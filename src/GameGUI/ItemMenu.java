package GameGUI;

import GameResources.BattleActionWorker;
import GameResources.GameCharacter.ActionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemMenu extends JPanel {

    private enum ItemButtons {
        HP("Health Potion"), MP("Magic Potion"), BACK;
        private String potionName;
        private static final ItemButtons[] buttons = {HP, MP, BACK};
        private static final ActionListener[] actionListeners = {
                new ActionListener(){// HEALTH
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        worker.signalHeroActionReady(ActionType.HEALTH);
                    }
                },
                new ActionListener(){// MP
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        worker.signalHeroActionReady(ActionType.MP);
                    }
                },
                new ActionListener(){// BACK
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cardLayout.show(buttonPanel,"Action");
                    }
                }
        };
        ItemButtons(){}
        ItemButtons(String potionName){this.potionName = potionName;}

        private static ItemButtons getButton(int i){
            return buttons[i];
        }

        private String getName(){
            return potionName;
        }
    }
    private static CardLayout cardLayout;
    private static JPanel buttonPanel;
    private final JButton[] buttons;
    private static BattleActionWorker worker;
    public ItemMenu(int screenWidth, int screenHeight, CardLayout cardLayout, JPanel buttonPanel){
        System.out.println("GameGUI.ItemMenu");
        ItemMenu.cardLayout = cardLayout;
        ItemMenu.buttonPanel = buttonPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 5, 5);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        setPreferredSize(new Dimension(screenWidth*9/20,screenHeight/3));
        setMaximumSize(new Dimension(screenWidth*9/20,screenHeight/3));
        setMinimumSize(new Dimension(screenWidth*9/20,screenHeight/3));

        buttons = new JButton[3];

        for(int i = 0; i < buttons.length-1; i++){
            buttons[i] = new JButton();
            buttons[i].addActionListener(ItemMenu.ItemButtons.actionListeners[i]);
            buttons[i].setText(ItemButtons.getButton(i).getName() + " ( Gain 5 " + ItemButtons.getButton(i).name() + " )");

            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridheight = 1;
            gbc.weighty = 0.0;

            add(buttons[i], gbc);
        }
        buttons[2] = new JButton();
        buttons[2].addActionListener(ItemMenu.ItemButtons.actionListeners[2]);
        buttons[2].setText("⏎");
        buttons[2].setFont(new Font(buttons[2].getFont().getName(), Font.BOLD,20));

        buttons[2].setPreferredSize(new Dimension(50, 20));

        gbc.gridy = buttons.length - 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(buttons[2], gbc);

        gbc.gridy = buttons.length;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(new JPanel(), gbc);
    }

    public void setWorker(BattleActionWorker worker) {
        ItemMenu.worker = worker;
    }
}
