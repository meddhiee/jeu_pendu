import javax.swing.*;
import java.awt.*;

public class PenduGame extends JPanel {
    private final ArbreBinaire arbreBinaire;

    public PenduGame(ArbreBinaire arbreBinaire) {
        this.arbreBinaire = arbreBinaire;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Your drawing logic for the binary tree here
        int largeurTotale = arbreBinaire.afficherArbre(g2d, 10, 50, 380);

        // Adjust the size of the panel based on the total width of the tree
        setPreferredSize(new Dimension(largeurTotale, 800));
    }
    
}

