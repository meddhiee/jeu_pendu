import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
	public static String selectedDifficulty;
	static String cheminFichier = "C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\Dictionnaire.txt";
	static ArbreBinaire arbre = new ArbreBinaire();
	// Create a SoundPlayer instance and load the sound file
    private static Son soundPlayer = new Son();
	private static JSlider volumeSlider;
	
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Jeu du Pendu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            try {
				initAndPlaySound();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // Initialize and play the sound

            // Load the background image
            ImageIcon backgroundImage = new ImageIcon("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\images\\bg_pendu.jpg");

            JButton commencerButton = new JButton("Commencer");
            JButton detailsButton = new JButton("Details");

            Color customGray = new Color(105, 115, 0);
            commencerButton.setBackground(customGray);

            // Set the foreground (text) color of the button
            commencerButton.setForeground(Color.WHITE);

            // Remove the border around the text in the button
            commencerButton.setFocusPainted(false);

            commencerButton.addActionListener(e -> {
                choisirDifficulte();
            });
            detailsButton.addActionListener(e -> afficherDetails());

            // Create a custom JPanel for displaying the background image
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Draw the background image
                    g.drawImage(backgroundImage.getImage(), 0, 0, this);
                }
            };

            // Set layout to BoxLayout
            backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

            // Add a welcome label with space around it
            JLabel welcomeLabel = new JLabel("Bienvenu au jeu du pendu !");
            welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Change the font of the welcome label
            Font customFont = new Font("Times New Roman", Font.BOLD, 36);
            welcomeLabel.setFont(customFont);
            

            // Add some vertical space between the label and the button
            backgroundPanel.add(Box.createVerticalStrut(40));

            // Add the welcome label to the panel
            backgroundPanel.add(welcomeLabel);

            // Add more space
            backgroundPanel.add(Box.createVerticalStrut(20));

            // Add the button with space around it
            backgroundPanel.add(commencerButton);
            commencerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Add the "Details" button with space around it
            backgroundPanel.add(Box.createVerticalStrut(10));
            backgroundPanel.add(detailsButton);
            detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

         // Add volume control slider
            JPanel volumePanel = new JPanel();
            volumePanel.setOpaque(true);
            volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            volumeSlider.setMajorTickSpacing(25);
            volumeSlider.setMinorTickSpacing(5);
            volumeSlider.setPaintTicks(true);
            volumeSlider.setPaintLabels(true);
            volumeSlider.setVisible(false);
            
            Dimension sliderSize = new Dimension(200, 40);
            volumeSlider.setPreferredSize(sliderSize);
            volumeSlider.setMaximumSize(sliderSize);


            volumeSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    updateVolume();
                }
            });
            backgroundPanel.add(Box.createVerticalStrut(20));
            // Add volume icon with mouse click event
            ImageIcon volumeIcon = new ImageIcon("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\images\\volume.png");
            JLabel volumeLabel = new JLabel(volumeIcon);
            volumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add mouse click event to toggle visibility of the volumeSlider
            volumeLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    toggleVolumeSliderVisibility();
                }
            });

            backgroundPanel.add(volumeLabel);
            backgroundPanel.add(volumeSlider);

            frame.setContentPane(backgroundPanel);

            // Set a specific size for the frame
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void toggleVolumeSliderVisibility() {
        volumeSlider.setVisible(!volumeSlider.isVisible());
    }

    private static void choisirDifficulte() {
        // Utilisez une boîte de dialogue pour permettre à l'utilisateur de choisir la difficulté
        Object[] options = {"Facile", "Moyen", "Difficile"};
        int choix = JOptionPane.showOptionDialog(null,
                "Choisissez la difficulté :",
                "Difficulté",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choix) {
            case 0:
                selectedDifficulty = "Facile";
                break;
            case 1:
                selectedDifficulty = "Moyen";
                break;
            case 2:
                selectedDifficulty = "Difficile";
                break;
            default:
                selectedDifficulty = "Facile";  // Choix par défaut
        }

        // Maintenant, vous pouvez initialiser le jeu avec la difficulté choisie
        initialiserJeu();
    }
    
    private static void initAndPlaySound() throws IOException {
    	
        soundPlayer.loadSound("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\sons\\Jeu.wav");
     // Utilisez un Timer pour jouer le son en boucle
        Timer timer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Jouer le son
                soundPlayer.playSound();
            }
        });

        // Réglez la période du Timer (en millisecondes) pour contrôler la fréquence de répétition du son
        timer.setDelay(60000); // Par exemple, jouez le son toutes les 1000 millisecondes (1 seconde)
        timer.start();
    }
    private static void updateVolume() {
        float volume = volumeSlider.getValue() / 100.0f;
        soundPlayer.setVolume(volume);
    }
    private static void initialiserJeu() {
        ArbreBinaire arbre = new ArbreBinaire();
        

        try {
            lireDictionnaireDepuisFichier(arbre, cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Jeu du Pendu - " + selectedDifficulty);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ArbrePanel arbrePanel = new ArbrePanel(arbre);
            frame.getContentPane().add(arbrePanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Appelez la méthode d'initialisation du jeu dans ArbrePanel avec la difficulté choisie
            try {
				arbrePanel.initialiserJeu(selectedDifficulty);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    private static void afficherArbre() {
    	try {
            lireDictionnaireDepuisFichier(arbre, cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Arbre Binaire");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            PenduGame pendu = new PenduGame(arbre);
            frame.getContentPane().add(pendu);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            
        });
    }
    private static void afficherDetails() {
        // Prompt the user for the access code
        String inputCode = JOptionPane.showInputDialog(null, "Enter le code d'accés :");

        // Check if the input code is correct (for demonstration purposes, using "0000" as the correct code)
        if (inputCode != null && inputCode.equals("0000")) {
            SwingUtilities.invokeLater(() -> {
                JFrame detailsFrame = new JFrame("Options");
                detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JButton voirArbreButton = new JButton("Voir Arbre");
                JButton ajouterMotButton = new JButton("Ajouter Mot");
                JButton supprimerMotButton = new JButton("Supprimer Mot");

                voirArbreButton.addActionListener(e -> afficherArbre());
                ajouterMotButton.addActionListener(e -> ajouterMot());
                supprimerMotButton.addActionListener(e -> supprimerMot());

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

                // Add voirArbreButton with space around it
                JPanel voirArbrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                voirArbrePanel.add(voirArbreButton);
                detailsPanel.add(voirArbrePanel);
                detailsPanel.add(Box.createVerticalStrut(10));

                // Add ajouterMotButton with space around it
                JPanel ajouterMotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                ajouterMotPanel.add(ajouterMotButton);
                detailsPanel.add(ajouterMotPanel);
                detailsPanel.add(Box.createVerticalStrut(10));

                // Add supprimerMotButton with space around it
                JPanel supprimerMotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                supprimerMotPanel.add(supprimerMotButton);
                detailsPanel.add(supprimerMotPanel);

                detailsFrame.getContentPane().add(detailsPanel);
                detailsFrame.setSize(300, 200);
                detailsFrame.setLocationRelativeTo(null);
                detailsFrame.setVisible(true);
            });
        } else {
            // Incorrect code, display a message
            JOptionPane.showMessageDialog(null, "Incorrect access code. Please try again.");
        }
    }
    private static void ajouterMot() {
        // Prompt the user to enter a new word
        String newWord = JOptionPane.showInputDialog(null, "Enter the new word:");

        // Check if the input is not null and not empty
        if (newWord != null && !newWord.trim().isEmpty()) {
            // Add the new word to the dictionary file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier, true))) {
                writer.write(newWord.trim());
                writer.newLine();
                JOptionPane.showMessageDialog(null, "Word added successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to add the word. Please try again.");
            }
        } else {
            // Display a message if the input is null or empty
            JOptionPane.showMessageDialog(null, "Invalid word. Please enter a valid word.");
        }
    }

    private static void lireDictionnaireDepuisFichier(ArbreBinaire arbre, String cheminFichier) throws IOException {
        BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier));
        String ligne;
        while ((ligne = lecteur.readLine()) != null) {
            arbre.ajouterMot(ligne.trim());
        }
        lecteur.close();
    }

    private static void supprimerMot() {
        // Read the words from the dictionary file
        List<String> words = lireMotsDepuisFichier(cheminFichier);

        // Check if there are words in the file
        if (words.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The dictionary is empty.");
            return;
        }

        // Create a JList to display the words
        JList<String> wordsList = new JList<>(words.toArray(new String[0]));
        wordsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Show a dialog with the list of words
        int result = JOptionPane.showConfirmDialog(null, new JScrollPane(wordsList), "Select words to delete",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Check if the user clicked OK
        if (result == JOptionPane.OK_OPTION) {
            // Get the selected words
            int[] selectedIndices = wordsList.getSelectedIndices();
            List<String> selectedWords = new ArrayList<>();
            for (int index : selectedIndices) {
                selectedWords.add(words.get(index));
            }

            // Remove the selected words from the dictionary file
            supprimerMotsDuFichier(cheminFichier, selectedWords);

            JOptionPane.showMessageDialog(null, "Selected words have been deleted.");
        }
    }

    private static List<String> lireMotsDepuisFichier(String cheminFichier) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private static void supprimerMotsDuFichier(String cheminFichier, List<String> motsASupprimer) {
        try (RandomAccessFile file = new RandomAccessFile(cheminFichier, "rw")) {
            long filePointer = 0; // pointer pour garder la position actuelle dans le fichier
            String line;
            while ((line = file.readLine()) != null) {
                if (!motsASupprimer.contains(line.trim())) {
                    // Si le mot n'est pas dans la liste des mots à supprimer, l'écrire à la position actuelle dans le fichier
                    file.seek(filePointer);
                    file.writeBytes(line.trim() + System.lineSeparator()); // Réécrire le mot dans le fichier
                    filePointer = file.getFilePointer(); // Mettre à jour la position du pointeur
                }
            }
            // Tronquer le fichier à la longueur actuelle pour supprimer les éventuelles lignes restantes
            file.setLength(filePointer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static class TextBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.WHITE);  // Couleur du contour
            g2d.setStroke(new BasicStroke(3));  // Largeur du contour
            g2d.drawRoundRect(x, y, width - 1, height - 1, 10, 10);  // Rectangle arrondi pour un contour plus doux
            g2d.dispose();
        }
    }
	   
	    
}
