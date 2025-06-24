package plateauJeu.modele.placement;

import plateauJeu.modele.placement.TronPlacementStrategie;
import plateauJeu.modele.Plateau;

//joueurs avec une distance de securité
public class TronPlacementEquilibre implements TronPlacementStrategie {
    public void placerTrons(int taille, int nbJoueur, Plateau plateau) {
        plateau.placerTronsAleatoire(taille/nbJoueur); // Distance min entre joueurs
    }
}
