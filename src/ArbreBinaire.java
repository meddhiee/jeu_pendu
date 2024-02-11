import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import java.awt.*;

public class ArbreBinaire {
	Noeud racine;

	public ArbreBinaire() {
		this.racine = null; // Racine initialisée à null
	}

	public Noeud getRacine() {
        return racine;
    }
	
	public Noeud ajouterMot(String Mot) {
	    racine = AjouterMot(racine, Mot, 0);
	    return racine;
	}

	private Noeud AjouterMot(Noeud root, String Mot, int pos) {
	    if (pos == Mot.length()) {
	    	if (root == null) {
	            return new Noeud('\0');}
	         else {
	            root.filsDroit = new Noeud('\0');
	            return root;
	         }}
	    if (root == null) {
	        root = new Noeud(Mot.charAt(pos));
	        root.filsGauche = AjouterMot(root.filsGauche, Mot, pos +1);
	    } else {
	    	if (Mot.charAt(pos) > root.caractere) {
	            if (root.filsDroit == null || root.filsDroit.caractere > Mot.charAt(pos)) {
	                Noeud newNode = new Noeud(Mot.charAt(pos));
	                newNode.filsDroit = root.filsDroit;
	                root.filsDroit = newNode;
	                newNode.filsGauche= AjouterMot(newNode.filsGauche, Mot, pos +1 );
	            } else {
	            	root.filsDroit=AjouterMot(root.filsDroit, Mot, pos);
	            }
	        } else if (Mot.charAt(pos) < root.caractere) {
	            Noeud newNode = new Noeud(Mot.charAt(pos));
	            newNode.filsDroit = root;
	            root = newNode;
	            newNode.filsGauche=AjouterMot(newNode.filsGauche, Mot, pos +1);
	        } else {
	        	root.filsGauche= AjouterMot(root.filsGauche, Mot, pos +1 );
	        }
	    }
	    return root;
	}

	public int NombreFils(Noeud racine, int nombre, Direction direction) {
        if (racine == null) {
            return (direction == Direction.GAUCHE) ? -1 : nombre;
        } else if (direction == Direction.DROIT) {
            return NombreFils(racine.filsDroit, nombre + 1, direction);
        } else if (racine.caractere == '\0') {
            return nombre;
        } else {
            return NombreFils(racine.filsGauche, nombre + 1, direction);
        }
    }

    public enum Direction {
        GAUCHE,
        DROIT
    }
    
    public Noeud NoeudAleatoire(Noeud noeud, int longueurMin, int longueurMax, String motFinal, Direction direction) {
        int indiceAleatoire = NombreFils(noeud, 0, Direction.DROIT);
        List<Noeud> noeudsCompatibles = new ArrayList<>();
        Noeud p = noeud;

        while (indiceAleatoire > 0) {
            int longueurAttendue = NombreFils(p, 0, Direction.GAUCHE) + motFinal.length();
            if (longueurAttendue >= longueurMin && longueurAttendue <= longueurMax) {
                noeudsCompatibles.add(p);
            }
            p = p.filsDroit;
            indiceAleatoire--;
        }

        if (!noeudsCompatibles.isEmpty()) {
            return noeudsCompatibles.get(new Random().nextInt(noeudsCompatibles.size()));
        } else {
            return null;
        }
    }
    public String MotAleatoire(int longueurMin, int longueurMax, Noeud racine, String motFinal) {
        if (racine == null) {
            return motFinal;
        } else if (racine.caractere == '\0') {
            return motFinal;
        } else {
        	Noeud noeudAleatoire = NoeudAleatoire(racine, longueurMin, longueurMax, motFinal, Direction.GAUCHE);
            if (noeudAleatoire != null) {
                motFinal += noeudAleatoire.caractere;
                return MotAleatoire(longueurMin, longueurMax, noeudAleatoire.filsGauche, motFinal);
            } else {
                return "";
            }
        }
    }

    public int afficherArbre(Graphics2D g, int x, int y, int xOffset) {
		return afficherArbre(racine, g, x + 750, y, xOffset, 0);
	}

	private int afficherArbre(Noeud root, Graphics2D g, int x, int y, int xOffset, int niveau) {
		if (root != null) {
			int diametre = 30;
			int rayon = diametre / 2;

			// Dessiner le nœud
			g.setColor(Color.BLACK);
			g.drawOval(x - rayon, y - rayon, diametre, diametre);
			g.drawString(String.valueOf(root.caractere), x - 5, y + 5);

			int largeurGauche = 0;
			int largeurDroit = 0;

			// Dessiner la ligne vers le fils gauche
			if (root.filsGauche != null) {
				int xGauche = x - xOffset / (1 << niveau);
				int yGauche = y + 50;
				g.drawLine(x, y, xGauche, yGauche);
				largeurGauche = afficherArbre(root.filsGauche, g, xGauche, yGauche, xOffset, niveau + 1);
			}

			// Dessiner la ligne vers le fils droit
			if (root.filsDroit != null) {
				int xDroit = x + xOffset / (1 << niveau);
				int yDroit = y + 50;
				g.drawLine(x, y, xDroit, yDroit);
				largeurDroit = afficherArbre(root.filsDroit, g, xDroit, yDroit, xOffset, niveau + 1);
			}

			// Retourner la largeur totale de cet arbre
			return largeurGauche + largeurDroit;
		}

		// Si le nœud est null, la largeur est nulle
		return 0;
	}



}
