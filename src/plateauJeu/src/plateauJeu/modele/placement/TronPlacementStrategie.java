package plateauJeu.modele.placement;

import plateauJeu.modele.Plateau;

//interface qui permet de definir les regles de placement de la carte
public interface TronPlacementStrategie {
    void placerTrons(int taille, int nbJoueur, Plateau plateau);
}
