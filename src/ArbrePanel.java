import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ArbrePanel extends JPanel {

    private final ArbreBinaire arbreBinaire;
    private String motDevine;
    private int erreurs;
    int erreursMax = 0;
    // Swing components
    private JTextField textField;
    private JLabel motLabel;
    private JButton validerButton;
    private String motADeviner="";
	private ArrayList lettresProposees;
	
	// Remplacez Object par JLabel pour errorLabel
    private JLabel errorLabel;
   
    private Image backgroundImage;
    private int score = 0;
    private JLabel scoreLabel;
    private JLabel tentatives;
    

    public ArbrePanel(ArbreBinaire arbreBinaire) {
        this.arbreBinaire = arbreBinaire;
        this.motDevine = "";
        
        this.lettresProposees = new ArrayList<>();

        setLayout(null); // Set layout to null for absolute positioning

        // Load the background image
        backgroundImage = new ImageIcon("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\images\\bg_pendu.jpg").getImage();
        
     // Ajoutez un JLabel pour afficher le score
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setBounds(20, 20, 100, 30); // Ajustez la position et la taille selon vos besoins
        add(scoreLabel);

        // Add your components with absolute positioning
        motLabel = new JLabel();
        motLabel.setBounds(350, 20, 300, 30); // Adjust the position and size as needed
        add(motLabel);

        tentatives = new JLabel("Tentatives: " + erreursMax);
        tentatives.setBounds(500, 20, 100, 30); // Ajustez la position et la taille selon vos besoins
        add(tentatives);

        textField = new JTextField(1);
        
        
        JPanel clavierPanel = new JPanel(new GridLayout(2, 13)); // Créer un panneau pour le clavier avec un GridLayout
        clavierPanel.setBounds(100, 380, 600, 100); // Ajuster la position et la taille selon vos besoins

        // Ajouter des boutons pour chaque lettre de l'alphabet français
        for (char lettre = 'A'; lettre <= 'Z'; lettre++) {
            JButton boutonLettre = new JButton(String.valueOf(lettre));
            boutonLettre.addActionListener(e -> {
                textField.setText(boutonLettre.getText()); // Mettre à jour le champ de texte avec la lettre sélectionnée
                // Ajouter ici toute autre action que vous souhaitez effectuer lors de la sélection d'une lettre
            });
            clavierPanel.add(boutonLettre);
        }

        add(clavierPanel);

        validerButton = new JButton("Valider");
        validerButton.setBounds(350, 330, 80, 30); // Adjust the position and size as needed
        validerButton.addActionListener(e -> {
            try {
                jouerPendu();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        add(validerButton);

        // Add other components similarly
        errorLabel = new JLabel();
        errorLabel.setBounds(200, 280, 550, 30); // Adjust the position and size as needed
        add(errorLabel);

        JButton backButton = new JButton("Back");
        backButton.setBounds(600, 500, 80, 30); // Ajuster la position et la taille selon vos besoins
        backButton.addActionListener(e -> retourMenuPrincipal());
        add(backButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(700, 500, 80, 30); // Adjust the position and size as needed
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        // Set the preferred size of the panel to cover the entire frame
        setPreferredSize(new Dimension(800, 450));
    }
    private void retourMenuPrincipal() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // Obtenez la fenêtre parente
        frame.dispose(); // Fermez la fenêtre actuelle
    }

    public void incrementScore() {
        score++;
        scoreLabel.setText("Score: " + score); // Mettez à jour le texte du label du score
    }
    
    public void jouerPendu() throws IOException {
    	Son soundPlayer = new Son();

        if (motADeviner.isEmpty()) {
        	motADeviner = choisirMotAleatoireAvecLongueur(2, 15, arbreBinaire);
            setMotDevine(afficherMotDevine(motADeviner, new ArrayList<>(), Main.selectedDifficulty));
        }
        System.out.println("mot :"+motADeviner);
        char lettreProposee = getLettreProposee();

        Color customGreen = Color.green;
        Color customRed = new Color(254, 27, 0);;
        if (lettresProposees.contains(lettreProposee)) {
            setErrorLabel("Vous avez déjà proposé cette lettre. Réessayez.",customRed);
            return;
        }

        lettresProposees.add(lettreProposee);

        // Clear the text field
        textField.setText("");
        if (motADeviner.contains(String.valueOf(lettreProposee))) {
            
            setMotDevine(afficherMotDevine(motADeviner, lettresProposees, Main.selectedDifficulty));
            
            if(getMotADeviner().equals(motADeviner)){
            	

            	// Modifiez votre ligne de code pour utiliser le StyledDocument
            	setErrorLabel("Félicitations ! Vous avez deviné le mot : " + motADeviner,customGreen);
            	
            	soundPlayer.loadSound("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\sons\\applaudi.wav");
                soundPlayer.playSound();
                incrementScore();
                resetJeu(Main.selectedDifficulty);
                
            
            }else {
            	setErrorLabel("Bravo la lettre "+lettreProposee+" existe!",customGreen);}
            soundPlayer.loadSound("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\sons\\tick.wav");
            soundPlayer.playSound();
            
        } else {
        	erreursMax--;
        	erreurs++;
        	tentatives.setText("Tentatives: " + erreursMax);
            setErrorLabel("Lettre erronée.\n Vous reste "+ erreursMax+" essais",customRed);            
            soundPlayer.loadSound("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\sons\\aah.wav");
            soundPlayer.playSound();
            setErreurs(erreurs);
            System.out.println(erreurs);
            setMotDevine(afficherMotDevine(motADeviner, lettresProposees, Main.selectedDifficulty));

            
			if (erreursMax==0) {
                setErrorLabel("Désolé, vous avez épuisé tous vos essais. Le mot était : " + motADeviner,customRed);
                soundPlayer.loadSound("C:\\Users\\meddh\\eclipse-workspace\\Jeu_du_pendu\\sons\\game_over.wav");
                soundPlayer.playSound();
                disableInput();
            }
        }
    }
 // Méthode pour initialiser un nouveau jeu avec un nouveau mot
    private void initialiserNouveauJeu() {
        motADeviner = choisirMotAleatoireAvecLongueur(2, 15, arbreBinaire);
        setMotDevine(afficherMotDevine(motADeviner, new ArrayList<>(), Main.selectedDifficulty));
    }
    public void initialiserJeu(String difficulty) throws IOException {
        int minLongueurMot = 0;
        int maxLongueurMot = 0;

        switch (difficulty) {
            case "Facile":
                erreursMax = 5;
                minLongueurMot = 2;
                maxLongueurMot = 4;
                break;
            case "Moyen":
                erreursMax = 4;
                minLongueurMot = 5;
                maxLongueurMot = 8;
                break;
            case "Difficile":
                erreursMax = 3;
                minLongueurMot = 9;
                maxLongueurMot = 15;
                break;
            default:
                erreursMax = 6;
        }


        // Initialisez le nombre de tentatives en fonction de la difficulté
        tentatives.setText("Tentatives: " + erreursMax);
        if (motADeviner.isEmpty()) {
            motADeviner = choisirMotAleatoireAvecLongueur(minLongueurMot, maxLongueurMot, arbreBinaire);
            setMotDevine(afficherMotDevine(motADeviner, new ArrayList<>(), Main.selectedDifficulty));
        }

        try {
            jouerPendu();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetJeu(String difficulty) {
        int minLongueurMot = 0;
		int maxLongueurMot = 0;

		switch (difficulty) {
		    case "Facile":
		        minLongueurMot = 2;
		        maxLongueurMot = 4;
		        break;
		    case "Moyen":
		        minLongueurMot = 5;
		        maxLongueurMot = 8;
		        break;
		    case "Difficile":
		        minLongueurMot = 9;
		        maxLongueurMot = 15;
		        break;
		    default:
		        // Gérez une valeur inattendue (peut-être affichez un message d'erreur)
		        minLongueurMot = 4; // Valeur par défaut
		        maxLongueurMot = 6;
		}
		
		// Réinitialisez la lettre aléatoire à chaque reset
	    premierAffichage = true;

		motADeviner = choisirMotAleatoireAvecLongueur(minLongueurMot, maxLongueurMot, arbreBinaire);
		setMotDevine(afficherMotDevine(motADeviner, new ArrayList<>(), Main.selectedDifficulty));
		erreurs = 0;
		lettresProposees.clear();
		setErreurs(erreurs);
		setErrorLabel("",Color.black);  // Clear any previous error messages

		// Enable the input fields and buttons
		textField.setEnabled(true);
		validerButton.setEnabled(true);

		// Repaint the panel
		repaint();
    }



    public void setMotDevine(String motDevine) {
        this.motDevine = motDevine;
        motLabel.setText("Mot : " + motDevine);
        repaint();
    }
 // Méthode pour obtenir le mot à deviner (vous pouvez la mettre à jour selon vos besoins)
    private String getMotADeviner() {
        return motDevine;
    }

 // Méthode pour désactiver le champ de saisie et le bouton Valider
    private void disableInput() {
        textField.setEnabled(false);
        validerButton.setEnabled(false);
    }
 // Méthode pour afficher un message d'erreur
    private void setErrorLabel(String errorMessage, Color color) {
        // Ajustez le texte du label d'erreur
        errorLabel.setText(errorMessage);
        errorLabel.setForeground(color);
        Font font = errorLabel.getFont();
        errorLabel.setFont(new Font(font.getName(), Font.PLAIN, 26));

        // Rafraîchissez le panneau pour rendre le JLabel visible
        revalidate();
        repaint();

        // Optionnel : Utilisez un Timer pour effacer automatiquement le message après quelques secondes
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorLabel.setText(""); // Effacez le texte du label d'erreur
                revalidate();
                repaint();
            }
        });
        timer.setRepeats(false); // N'exécutez le Timer qu'une seule fois
        timer.start();
    }

    private String choisirMotAleatoireAvecLongueur(int minLongueur, int maxLongueur, ArbreBinaire arbre) {
        String motAleatoire = arbre.MotAleatoire(minLongueur, maxLongueur, arbre.getRacine(), "");
        motAleatoire = motAleatoire.toLowerCase();
        if (motAleatoire.isEmpty()) {
            // Gérer le cas où aucun mot ne correspond à la longueur spécifiée
            // Vous pouvez ajouter un message d'erreur ou choisir une autre stratégie
            return "";
        }

        System.out.println("mot a deviner " + motAleatoire);
        return motAleatoire;
    }

    public void setErreurs(int erreurs) {
        this.erreurs = erreurs;
    }

    // Ajoutez une méthode pour obtenir la lettre saisie dans le champ de texte
    public char getLettreProposee() {
        return textField.getText().toLowerCase().charAt(0);
    }
    
    // Stocker la lettre à la position aléatoire
    private static List<Character> lettresAleatoires = new ArrayList<>();
    // Vérifier si c'est le premier affichage
    private static boolean premierAffichage = true;
    private static int nombreLettresAleatoires;

 // ...

 // Méthode pour afficher l'état actuel du mot avec les lettres devinées
 private static String afficherMotDevine(String motADeviner, List<Character> lettresProposees, String difficulty) {
     StringBuilder affichage = new StringBuilder();

     // Si c'est le premier affichage, choisir des positions aléatoires dans le mot et stocker les lettres
     if (premierAffichage) {
         // Réinitialisez la liste des lettres aléatoires
         lettresAleatoires.clear();

         // Déterminez le nombre de lettres aléatoires en fonction du niveau de difficulté
         switch (difficulty) {
             case "Facile":
                 nombreLettresAleatoires = 1;
                 break;
             case "Moyen":
                 nombreLettresAleatoires = 2;
                 break;
             case "Difficile":
                 nombreLettresAleatoires = 3;
                 break;
             default:
                 // Gérez une valeur inattendue (peut-être affichez un message d'erreur)
                 nombreLettresAleatoires = 1; // Valeur par défaut
         }

         for (int i = 0; i < nombreLettresAleatoires; i++) {
             int positionAleatoire = (int) (Math.random() * motADeviner.length());
             lettresAleatoires.add(motADeviner.charAt(positionAleatoire));
         }

         premierAffichage = false;
     }

     // Parcourir le mot
     for (int i = 0; i < motADeviner.length(); i++) {
         char lettre = motADeviner.charAt(i);

         // Si la lettre est dans les lettres devinées ou c'est l'une des lettres aléatoires, afficher la lettre
         if (lettresProposees.contains(lettre) || lettresAleatoires.contains(lettre)) {
             affichage.append(lettre);
         } else {
        	 affichage.append(" ");
             affichage.append("_");
         }
     }

     return affichage.toString();
 }

   
    private void afficherPendu(Graphics2D g2d, int erreurs, int erreursMax) {
    	// Définir une épaisseur de trait pour les éléments en gras
        Stroke boldStroke = new BasicStroke(3); // Modifiez 3 selon l'épaisseur souhaitée
        
        // Appliquer l'épaisseur de trait pour les éléments en gras
        g2d.setStroke(boldStroke);
        
        // Dessiner le pendu en fonction du nombre d'erreurs
        int largeur = getWidth();
        int hauteur = getHeight();

        int x = largeur / 2;
        int y = hauteur /3;

        int taillePendu = Math.min(largeur, hauteur) / 10;
        
     
        if (erreurs >= 1) {
        	// Dessiner le support vertical
            g2d.drawLine(x - taillePendu, y + taillePendu, x - taillePendu, y - taillePendu * 2);
            // Dessiner la poutre horizontale
            g2d.drawLine(x - taillePendu, y - taillePendu * 2, x + taillePendu, y - taillePendu * 2);
        }

        if (erreurs >= 2) {
            // Dessiner la corde
        	g2d.drawLine(x + taillePendu, y - taillePendu * 2, x + taillePendu, y - taillePendu * 2 + taillePendu / 4);
        }

        if (erreurs >= 3 && erreursMax==0) {
        	// Dessiner la tête
            g2d.drawOval(x + taillePendu -10 , y - taillePendu * 2 + taillePendu / 4,
                    taillePendu / 4, taillePendu / 4);
         // Dessiner le corps
            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, x + taillePendu ,
                    y - 50);
         // Dessiner les bras
            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, x +20,
                    y - taillePendu * 2 + taillePendu / 4);

            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, (int) (x + taillePendu * 1.5),
                    y - taillePendu * 2 + taillePendu / 4);
         // Dessiner les jambes
            g2d.drawLine(x + taillePendu , y - 50 , x +25 ,
                    y  );
            g2d.drawLine(x + taillePendu , y - 50 , (int) (x + taillePendu * 1.5),
                    y  );
        }
        if (erreurs >= 3) {
        	// Dessiner la tête
            g2d.drawOval(x + taillePendu -10 , y - taillePendu * 2 + taillePendu / 4,
                    taillePendu / 4, taillePendu / 4);
        }

        if (erreurs >= 4) {
        	// Dessiner le corps
            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, x + taillePendu ,
                    y - 50);
        }
        if (erreurs >= 4 && erreursMax==0) {
        	// Dessiner le corps
            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, x + taillePendu ,
                    y - 50);
         // Dessiner les bras
            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, x +20,
                    y - taillePendu * 2 + taillePendu / 4);

            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, (int) (x + taillePendu * 1.5),
                    y - taillePendu * 2 + taillePendu / 4);
         // Dessiner les jambes
            g2d.drawLine(x + taillePendu , y - 50 , x +25 ,
                    y  );
            g2d.drawLine(x + taillePendu , y - 50 , (int) (x + taillePendu * 1.5),
                    y  );
        }

        if (erreurs >= 5 && erreursMax==0) {
        	// Dessiner les bras
            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, x +20,
                    y - taillePendu * 2 + taillePendu / 4);

            g2d.drawLine(x + taillePendu , y - taillePendu * 2 + taillePendu / 2, (int) (x + taillePendu * 1.5),
                    y - taillePendu * 2 + taillePendu / 4);
         // Dessiner les jambes
            g2d.drawLine(x + taillePendu , y - 50 , x +25 ,
                    y  );
            g2d.drawLine(x + taillePendu , y - 50 , (int) (x + taillePendu * 1.5),
                    y  );
            
        }

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        // Continue with your existing drawing code...
        Graphics2D g2d = (Graphics2D) g;
        afficherPendu(g2d, erreurs, erreursMax);
    }   
}