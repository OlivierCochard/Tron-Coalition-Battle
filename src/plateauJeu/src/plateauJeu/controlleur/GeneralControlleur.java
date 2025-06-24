package plateauJeu.controlleur;

import plateauJeu.modele.Plateau;
import entitesJeu.Mur;
import entitesJeu.Tron;
import entitesJeu.Entite;
import outilsJeu.Vecteur;
import plateauJeu.modele.ia.ParanoideSOS;
import plateauJeu.modele.ia.Paranoide;
import plateauJeu.modele.ia.MaxNSOS;
import plateauJeu.modele.ia.MaxN;
import plateauJeu.modele.ia.IA;

public class GeneralControlleur {
	private Plateau plateau;

	public GeneralControlleur(Plateau plateau){
		this.plateau = plateau;
	}

	public String obtenirDirectionIA(String ia, int profondeurRecherche){
		Tron tronCourant = plateau.getTronCourant();
		IA iaBot = null;
		if (ia.equals("Paranoide")){
			iaBot = new Paranoide(tronCourant.getCoordonnee().x, tronCourant.getCoordonnee().y, plateau.getGrille());
		}
		else if (ia.equals("ParanoideSOS")){
			iaBot = new ParanoideSOS(tronCourant.getCoordonnee().x, tronCourant.getCoordonnee().y, plateau.getGrille(), plateau.getTrons(), plateau.getIndexTronCourant(), plateau.getEquipes());
		}
		else if (ia.equals("MaxN")){
			iaBot = new MaxN(tronCourant.getCoordonnee().x, tronCourant.getCoordonnee().y, plateau.getGrille(), plateau.getTrons());
		}
		else if (ia.equals("MaxNSOS")){
			iaBot = new MaxNSOS(tronCourant.getCoordonnee().x, tronCourant.getCoordonnee().y, plateau.getGrille(), plateau.getTrons(), plateau.getIndexTronCourant(), plateau.getEquipes());
		}
		if (iaBot == null){
			return "";
		}
		return iaBot.obtenirDirection(profondeurRecherche);
	}

	public void appliquerDeplacement(Vecteur direction, Tron tronCourant){
		int tronCordX = tronCourant.getCoordonnee().x;
		int tronCordY = tronCourant.getCoordonnee().y;

		int newTronCordX = tronCordX + direction.x;
		int newTronCordY = tronCordY + direction.y;

		//Mort
		if (plateau.estClasseValide(newTronCordX, newTronCordY) == false){
			tronCourant.destruction();
		}
		//DEPLACEMENT
		else{
			plateau.ajouterEntite((Entite) new Mur(new Vecteur(tronCordX, tronCordY)), tronCordX, tronCordY);
			plateau.ajouterEntite((Entite) tronCourant, newTronCordX, newTronCordY);
		}

		plateau.changementTronCourant();
	}
}
