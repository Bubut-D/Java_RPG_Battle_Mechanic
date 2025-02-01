package GameGUI;

import GameResources.GameCharacter;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ProgressBarHandler implements Runnable{
    public enum ProgressBars{HP,MP};

    private static Map<GameCharacter,JProgressBar[]> characterBars = new HashMap<>();
    private String barName;
    private JProgressBar progressBar;
    private int progress;

    private ProgressBarHandler(JProgressBar progressBar, int progress, String barName){
        this.progressBar = progressBar;
        this.progress = progress;
        this.barName = barName;
    }

    @Override
    public void run() {
        progressBar.setString(barName + progress + "/" + progressBar.getMaximum());
        progressBar.setValue(progress);
    }

    public static void setValue(GameCharacter character, int progress, ProgressBars barType){
        JProgressBar[] bars = characterBars.get(character);
        JProgressBar bar;
        String barName;
        switch (barType) {
            case HP:
                bar = bars[0];
                barName = "HP: ";
                break;
            case MP:
                bar = bars[1];
                barName = "MP: ";
                break;
            default:
                throw new IllegalArgumentException("Illegal Bar Type!");
        }
        SwingUtilities.invokeLater(new ProgressBarHandler(bar,progress,barName));
    }

    public static void setProgressBars(GameCharacter[] characters, JProgressBar[] bars){
        for (int i = 0; i < 2; i++){
            characterBars.put(characters[i],new JProgressBar[]{bars[i*2],bars[i*2+1]});
        }
    }
}
