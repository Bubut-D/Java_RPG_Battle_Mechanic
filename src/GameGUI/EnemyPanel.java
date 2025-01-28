package GameGUI;

import javax.swing.*;
import java.awt.*;
import GameResources.GameCharacter;

public class EnemyPanel extends JPanel{
    private JLabel iconLabel;
    private StatusPanel statusPanel;
    private JPanel panel;
    private GridBagConstraints gbc;

    public EnemyPanel(GameCharacter enemy, int screenWidth, int screenHeight){
        super(new GridBagLayout());
        gbc = new GridBagConstraints();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight/3));
        this.setDoubleBuffered(true);
        statusPanel = new StatusPanel(enemy,screenWidth,Component.RIGHT_ALIGNMENT);
        panel = new JPanel(new GridBagLayout());
        panel.add(statusPanel);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        add(panel,gbc);
        Image image = new ImageIcon("enemy_sprite.png").getImage().getScaledInstance(110,113,Image.SCALE_SMOOTH);
        iconLabel = new JLabel(new ImageIcon(image)); // TODO: Make this specific to each enemy
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.gridwidth = 2;
        add(iconLabel,gbc);
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }
}
