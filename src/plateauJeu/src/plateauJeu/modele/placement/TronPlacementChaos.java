package plateauJeu.modele.placement;

import plateauJeu.modele.placement.TronPlacementStrategie;
import plateauJeu.modele.Plateau;

//joueurs possiblement a coté d'autres joueurs
public class TronPlacementChaos implements TronPlacementStrategie {
    public void placerTrons(int taille, int nbJoueur, Plateau plateau) {
        plateau.placerTronsAleatoire(1); // Distance min entre joueurs
    }
}
