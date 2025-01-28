package GameGUI;

import javax.swing.*;
import java.awt.*;
import GameResources.GameCharacter;

public class StatusPanel extends JPanel {

    private JLabel name;
    private JProgressBar hp;
    private JProgressBar mp;


    StatusPanel(GameCharacter character, int screenWidth, float nameAlignment){
        // Use a more predictable layout manager
        setLayout(new BorderLayout());

        // Create a panel for the left side content
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Name label with left alignment
        name = new JLabel(character.getName());
        name.setAlignmentX(nameAlignment);

        if (nameAlignment == Component.LEFT_ALIGNMENT)
            name.setBorder(BorderFactory.createEmptyBorder(0, screenWidth * 3/200, 0, 0));
        else
            name.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, screenWidth * 3/200));
        // Progress bars with left alignment
        hp = createBar(character.getStats().getHealthPoints(), character.getStats().getMaxHealthPoints(), "HP", screenWidth);
        hp.setAlignmentX(nameAlignment);

        mp = createBar(character.getStats().getMagicPoints(), character.getStats().getMaxMagicPoints(), "MP", screenWidth);
        mp.setAlignmentX(nameAlignment);

        // Add vertical spacing
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(name);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(hp);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(mp);

        // Add left padding to the entire left panel
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, screenWidth * 3/100, 0, 0));

        // Add the left panel to the west side of BorderLayout
        add(leftPanel, BorderLayout.WEST);
    }

    private JProgressBar createBar(int current, int max, String name,int screenWidth){
        JProgressBar bar = new JProgressBar(0,max);
        bar.setPreferredSize(new Dimension(screenWidth*35/100,20));
        bar.setMinimumSize(new Dimension(screenWidth*35/100,20));
        bar.setMaximumSize(new Dimension(screenWidth*35/100,20));
        bar.setValue(current);
        bar.setString(name + ": " + current + "/" + max);
        bar.setStringPainted(true);
        return bar;
    }

    public JProgressBar getHp() {
        return hp;
    }

    public JProgressBar getMp() {
        return mp;
    }
}
