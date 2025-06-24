package plateauJeu.controlleur;

import plateauJeu.controlleur.GeneralControlleur;
import plateauJeu.modele.Plateau;
import plateauJeu.vue.TerminalVue;
import entitesJeu.Tron;
import outilsJeu.Vecteur;


import java.util.*;

public class TerminalControlleur {
	private Plateau plateau;
	private TerminalVue terminalVue;
	private GeneralControlleur generalControlleur;
	private boolean choixIa;
	private int profondeurRecherche;
	private String ia;

	public TerminalControlleur(Plateau plateau, TerminalVue terminalVue, GeneralControlleur generalControlleur, boolean choixIa, int profondeurRecherche, String ia){
		this.ia = ia;
		this.choixIa = choixIa;
		this.plateau = plateau;
		this.terminalVue = terminalVue;
	    this.generalControlleur = generalControlleur;
	    this.profondeurRecherche = profondeurRecherche;
		jouer();
	}

	private void jouer(){
		Scanner scanner = new Scanner(System.in);
		
		while (plateau.getTermine() == false){
			Tron tronCourant = plateau.getTronCourant();
			if (tronCourant.getMort()){
				plateau.changementTronCourant();
				continue;
			}
			
			System.out.println("\nC'est à " + tronCourant.getNom() + " de jouer !");
			System.out.println(terminalVue);

			//BOT
			if (choixIa){
				String direction = generalControlleur.obtenirDirectionIA(ia, profondeurRecherche);
			 	System.out.println(tronCourant.getNom() + " : " + direction);
				generalControlleur.appliquerDeplacement(new Vecteur(direction), tronCourant);
			}
			//PLAYER
			else {
				System.out.println("Entrez une direction: [haut, droite, bas, gauche] ?");
				String direction = scanner.nextLine();

				if (direction.equals("haut") || direction.equals("droite") || direction.equals("bas") || direction.equals("gauche")){
					generalControlleur.appliquerDeplacement(new Vecteur(direction), tronCourant);
					continue;
				}
				System.out.println("Direction non reconnue : " + direction + "\n");
			}
		}

		System.out.println("\n" + terminalVue + "\nPartie terminée !");
	}
}
