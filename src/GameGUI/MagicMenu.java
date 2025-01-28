package GameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import GameResources.BattleActionWorker;
import GameResources.GameCharacter.ActionType;
import GameResources.GameCharacter;
import GameResources.Magic;

public class MagicMenu extends JPanel{

    private enum MagicButtons {
        FIRE, BLZRD, THNDR, BACK;

        private static final MagicButtons[] buttons = {FIRE, BLZRD, THNDR, BACK};
        private static final ActionListener[] actionListeners = {
                new ActionListener(){// FIRE
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("fire pressed");
                        worker.signalHeroActionReady(ActionType.FIRE);
                    }
                },
                new ActionListener(){// BLIZZARD
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("blizzard pressed");
                        worker.signalHeroActionReady(ActionType.BLIZZARD);
                    }
                },
                new ActionListener(){// THUNDER
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("thunder pressed");
                        worker.signalHeroActionReady(ActionType.THUNDER);
                    }
                },
                new ActionListener(){// BACK
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("back pressed");
                        cardLayout.show(buttonPanel,"Action");
                    }
                }
        };

        private static MagicButtons getButton(int i){
            return buttons[i];
        }

    }
    private static CardLayout cardLayout;
    private static JPanel buttonPanel;
    private final JButton[] buttons;
    private final Magic[] magics;
    static BattleActionWorker worker;
    public MagicMenu(int screenWidth, int screenHeight, GameCharacter character, CardLayout cardLayout, JPanel buttonPanel){
        System.out.println("GameGUI.MagicMenu");
        MagicMenu.cardLayout = cardLayout;
        MagicMenu.buttonPanel = buttonPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 5, 5);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        setPreferredSize(new Dimension(screenWidth*9/20,screenHeight/3));
        setMaximumSize(new Dimension(screenWidth*9/20,screenHeight/3));
        setMinimumSize(new Dimension(screenWidth*9/20,screenHeight/3));

        magics = new Magic[3];
        magics[0] = character.getFire();
        magics[1] = character.getBlizzard();
        magics[2] = character.getThunder();

        buttons = new JButton[4];

        for(int i = 0; i < buttons.length-1; i++){
            buttons[i] = new JButton();
            buttons[i].addActionListener(MagicButtons.actionListeners[i]);
            buttons[i].setText(magics[i].getName() + " ( MP: " + magics[i].getCost() + " )");

            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridheight = 1;
            gbc.weighty = 0.0;

            add(buttons[i], gbc);
        }
        buttons[3] = new JButton();
        buttons[3].addActionListener(MagicButtons.actionListeners[3]);
        buttons[3].setText("⏎");
        buttons[3].setFont(new Font(buttons[3].getFont().getName(), Font.BOLD,20));

        buttons[3].setPreferredSize(new Dimension(50, 20));


        gbc.gridy = buttons.length - 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridheight = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(buttons[3], gbc);

        gbc.gridy = buttons.length;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        add(new JPanel(), gbc);
    }

    public void setWorker(BattleActionWorker worker) {
        MagicMenu.worker = worker;
    }
}
