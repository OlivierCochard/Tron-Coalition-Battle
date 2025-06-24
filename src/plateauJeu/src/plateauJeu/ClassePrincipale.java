package plateauJeu;

import java.util.*;

import plateauJeu.modele.Plateau;
import plateauJeu.modele.placement.TronPlacementStrategie;
import plateauJeu.modele.placement.TronPlacementEquilibre;
import plateauJeu.modele.placement.TronPlacementChaos;
import plateauJeu.modele.placement.TronPlacementOptimale;
import plateauJeu.controlleur.TerminalControlleur;
import plateauJeu.controlleur.GeneralControlleur;
import plateauJeu.vue.GraphiqueVue;
import plateauJeu.vue.TerminalVue;

public class ClassePrincipale{
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);

		//NOMBRE DE JOUEURS
		System.out.println("Combien de joueurs voulez vous: [2..9] ?");
		int nbJoueurs = Integer.parseInt(scanner.nextLine());
		if (nbJoueurs < 2 || nbJoueurs > 9){
            System.out.println("Le nombre (" + nbJoueurs + ") doit etre superieur à 1 et inférieur à 10 !!!");
            return;
        }

		//TAILLE DE CARTE
		int tailleCarteMini = (int) Math.ceil(Math.sqrt(nbJoueurs));
		System.out.println("Quelle taille de carte voulez vous: [" + tailleCarteMini + "..N] ?");
		int tailleCarte = Integer.parseInt(scanner.nextLine());
		if (tailleCarte < tailleCarteMini){
			System.out.println("La taille de carte (" + tailleCarte + ") doit etre superieur à " + tailleCarteMini + " !!!");
            return;
		}

		//MODE PLACEMENT
        System.out.println("Quel mode de placement voulez vous ? Parmi les choix suivant: [equilibre, chaos, optimale] ?");
        String modePlacement = scanner.nextLine();
        if (modePlacement.equals("optimale") == false && modePlacement.equals("equilibre") == false && modePlacement.equals("chaos") == false){
            System.out.println("Le mode (" + modePlacement + ") n'existe pas !!!");
            return;
        }

        //MODE CONTROLE
        /*
        System.out.println("Voulez vous controler les trons ? Parmi les choix suivant: [oui, non] ?");
        String modeControle = scanner.nextLine();
        if (modeControle.equals("oui") == false && modeControle.equals("non") == false){
            System.out.println("La réponse (" + modeControle + ") n'est pas reconnue !!!");
            return;
        }
        boolean choixIa = true;
        if (modeControle.equals("oui"))
            choixIa = false;
        */
        String modeControle = "non";
        boolean choixIa = true;
            
        //CHOIX PROFONDEUR RECHERCHE
        int taillePronfondeurRechercheMax = tailleCarte * tailleCarte;
        int profondeurRecherche = 0;
        if (choixIa){
			System.out.println("Combien de profondeur de recherche voulez vous: [1.." + taillePronfondeurRechercheMax + "]");
			profondeurRecherche = Integer.parseInt(scanner.nextLine());
			if (profondeurRecherche < 1 || profondeurRecherche > taillePronfondeurRechercheMax){
				System.out.println("La pronfondeur de recherche (" + profondeurRecherche + ") doit etre superieur à 0 et inférieur à " + taillePronfondeurRechercheMax + "!!!");
				return;
			}
		}
		
		//CHOIX IA
		String ia = "";
		if (choixIa){
			System.out.println("Quel ia voulez vous: [MaxN, Paranoide, MaxNSOS, ParanoideSOS]");
			ia = scanner.nextLine();
			if (ia.equals("MaxN") == false && ia.equals("Paranoide") == false && ia.equals("MaxNSOS") == false && ia.equals("ParanoideSOS") == false){
				System.out.println("L'ia (" + ia + ") n'existe pas parmis les choix: [MaxN, Paranoide, MaxNSOS, ParanoideSOS]");
				return;
			}
		}
		
		//TAILLE EQUIPE
		int tailleEquipe = 1;
		if (ia.equals("ParanoideSOS") || ia.equals("MaxNSOS")){
			int tailleEquipeMax = nbJoueurs-1;
			System.out.println("Quel taille d'équipe voulez vous: [2.." + tailleEquipeMax + "]");
			tailleEquipe = Integer.parseInt(scanner.nextLine());
			if (tailleEquipe < 2 || tailleEquipe > tailleEquipeMax){
				System.out.println("La taille d'équipe n'est pas correct (" + tailleEquipe + ")");
				return;
			}
		}
		
		//SUPPORT JEU
        System.out.println("Quel support de jeu voulez-vous ? Parmis les choix: [Terminal, Graphique] ?");
        String supportJeu = scanner.nextLine();
        if (supportJeu.equals("Terminal") == false && supportJeu.equals("Graphique") == false){
            System.out.println("La réponse (" + supportJeu + ") n'est pas reconnue !!!");
            return;
        }
        
        //MS ACTION
        int msEntreAction = 200;
        if (supportJeu.equals("Graphique")){
			System.out.println("Quel délai en (ms) voulez-vous pendant la visualisation: [0..N]");
			msEntreAction = Integer.parseInt(scanner.nextLine());
		}

        //MODELE
        Plateau plateau = new Plateau(tailleCarte, tailleCarte, nbJoueurs, tailleEquipe);
        TronPlacementStrategie tronPlacementStrategie;
        if (modePlacement.equals("equilibre")){
        	tronPlacementStrategie = new TronPlacementEquilibre();
        }
        else if (modePlacement.equals("chaos")){
        	tronPlacementStrategie = new TronPlacementChaos();
        }
        else if (modePlacement.equals("optimale")){
        	tronPlacementStrategie = new TronPlacementOptimale();
        }
        else return;
        tronPlacementStrategie.placerTrons(tailleCarte, nbJoueurs, plateau);
		
		//TERMINAL
		if (supportJeu.equals("Terminal")){
			//VUE
			TerminalVue terminalVue = new TerminalVue(plateau);
			plateau.afficherTrons();
			plateau.afficherEquipes();

			//CONTROLLEUR
			GeneralControlleur generalControlleur = new GeneralControlleur(plateau);
			TerminalControlleur terminalControlleur = new TerminalControlleur(plateau, terminalVue, generalControlleur, choixIa, profondeurRecherche, ia);
		}
        //GRAPHIQUE
		else if (supportJeu.equals("Graphique")){
			//CONTROLLEUR
			GeneralControlleur generalControlleur = new GeneralControlleur(plateau);
			
			//VUE 
			GraphiqueVue graphiqueVue = new GraphiqueVue(plateau, generalControlleur, profondeurRecherche, ia, msEntreAction);
		}
    }
}
