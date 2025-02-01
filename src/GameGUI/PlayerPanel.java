package GameGUI;

import javax.swing.*;
import java.awt.*;
import GameResources.GameCharacter;

public class PlayerPanel extends JPanel {

    private JLabel iconLabel;
    private StatusPanel statusPanel;
    private JPanel panel;
    private GridBagConstraints gbc;

    public PlayerPanel(GameCharacter hero, int screenWidth, int screenHeight){
        super(new GridBagLayout());
        gbc = new GridBagConstraints();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight/3));
        this.setDoubleBuffered(true);
        Image image = new ImageIcon("player_sprite.png").getImage().getScaledInstance(120,124,Image.SCALE_SMOOTH);
        iconLabel = new JLabel(new ImageIcon(image)); // TODO: Make this specific to each hero

        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(iconLabel,gbc);
        statusPanel = new StatusPanel(hero,screenWidth,Component.LEFT_ALIGNMENT);
        panel = new JPanel(new GridBagLayout());
        panel.add(statusPanel);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        add(panel,gbc);
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }
}
