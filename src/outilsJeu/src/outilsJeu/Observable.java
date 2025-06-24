package outilsJeu;

import outilsJeu.Observeur;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    void ajouterObserveur(Observeur observeur);
    void retirerObserveur(Observeur observeur);

    //methode pour notifier à tous les observeur une liste d'evenement et d'informations
    void notifierObserveurs(String evenement);
}