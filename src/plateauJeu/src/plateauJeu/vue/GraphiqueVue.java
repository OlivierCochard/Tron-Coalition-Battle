package plateauJeu.vue;

import entitesJeu.Tron;
import entitesJeu.Mur;
import entitesJeu.Entite;
import plateauJeu.modele.Plateau;
import plateauJeu.controlleur.GeneralControlleur;
import outilsJeu.Vecteur;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphiqueVue extends JFrame {
    private List<Tron> trons;
    private Plateau plateau;
    private GeneralControlleur generalControlleur;
    private GamePanel gamePanel; 
    private int profondeurRecherche; 
    private String ia; 
    private int msEntreAction;

    public GraphiqueVue(Plateau plateau, GeneralControlleur generalControlleur, int profondeurRecherche, String ia, int msEntreAction) {    
        this.trons = plateau.getTrons();
        this.plateau = plateau;
        this.generalControlleur = generalControlleur;
        this.profondeurRecherche = profondeurRecherche;
        this.ia = ia;
        this.msEntreAction = msEntreAction;

        setTitle("Jeu de Trons");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		container.add(gamePanel);
		add(container, BorderLayout.CENTER);

        setVisible(true);
        lancerJeu();
    }
    
    private void lancerJeu() {
		Timer timer = new Timer(msEntreAction, e -> {
			if (plateau.getTermine()) {
				System.out.println("\nPartie terminée !");
				((Timer) e.getSource()).stop();
				return;
			}

			Tron tronCourant = plateau.getTronCourant();
			if (tronCourant.getMort()) {
				plateau.changementTronCourant();
			} else {
				String direction = generalControlleur.obtenirDirectionIA(ia, profondeurRecherche);
				generalControlleur.appliquerDeplacement(new Vecteur(direction), tronCourant);
			}

			gamePanel.repaint();
		});
		timer.start();
	}


    private class GamePanel extends JPanel {
		public GamePanel() {
			int cellSize = 20;
			setPreferredSize(new Dimension(plateau.getLargeur() * cellSize, plateau.getHauteur() * cellSize));
		}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            dessinerPlateau(g);
        }

        private void dessinerPlateau(Graphics g) {
			int cellSize = 20; // Taille d'une case de la grille

			for (int x = 0; x < plateau.getLargeur(); x++) {
				for (int y = 0; y < plateau.getHauteur(); y++) {
					Entite entite = plateau.obtenirEntite(x, y);

					if (entite instanceof Tron) {
						Tron tron = (Tron) entite;

						// Couleur selon la vie du Tron
						if (tron.getMort()) {
							g.setColor(Color.RED);
						} else {
							g.setColor(Color.GREEN);
						}

						// Dessiner le carré du Tron
						g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);

						// Dessiner le nom du Tron au centre
						g.setColor(Color.BLACK); 
						g.setFont(new Font("Arial", Font.BOLD, 12));
						String nom = tron.getRepresentation();
						
						// Calcul pour centrer le texte dans la case
						FontMetrics metrics = g.getFontMetrics();
						int textX = x * cellSize + (cellSize - metrics.stringWidth(nom)) / 2;
						int textY = y * cellSize + ((cellSize - metrics.getHeight()) / 2) + metrics.getAscent();

						g.drawString(nom, textX, textY);
					} 
					else if (entite instanceof Mur) {
						g.setColor(Color.WHITE);
						g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
					} 
					else {
						g.setColor(Color.BLACK);
						g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
					}
				}
			}
		}
    }
}
