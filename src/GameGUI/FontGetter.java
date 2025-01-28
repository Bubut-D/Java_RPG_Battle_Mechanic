package GameGUI;

import java.awt.*;
import java.io.File;

public class FontGetter {
    public static Font getFont(String resource){
        String filePath = PlayerUI.class.getClassLoader().getResource(resource).getPath();

        if (filePath.contains("%20")){
            filePath = filePath.replaceAll("%20"," ");
        }

        try {
            File fontFile = new File(filePath);

            if (fontFile == null || !fontFile.exists()) {
                System.out.println("Font file not found. Using default font.");
                // Optional: Set a fallback font
                return new Font("Arial", Font.PLAIN, 15);
            } else {
                return Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(15f);
            }
        } catch (Exception e) {
            System.out.println("Error when getting font. Using default font.");
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 15);
        }
    }
}
