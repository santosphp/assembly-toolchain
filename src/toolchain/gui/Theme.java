package toolchain.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class Theme {
    public static final Color BACKGROUND = new Color(70, 70, 70);
    public static final Color FOREGROUND = new Color(230, 230, 230); // Soft white

    public static final Color BORDER_COLOR = new Color(200, 200, 200);
    public static final Font MONO_FONT = new Font("Monospaced", Font.PLAIN, 14);
    public static final Font MONO_BOLD = new Font("Monospaced", Font.BOLD, 12);

    public static final int BORDER_THICKNESS = 1;

    // Create a reusable titled border factory method instead of fixed border:
    public static TitledBorder createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, BORDER_THICKNESS),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            MONO_BOLD,
            FOREGROUND
        );
    }
}
