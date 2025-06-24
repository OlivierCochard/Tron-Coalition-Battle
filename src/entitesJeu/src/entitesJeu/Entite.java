package entitesJeu;

import outilsJeu.Observable;
import outilsJeu.Observeur;

import java.util.ArrayList;
import java.util.List;

//class qui implemente l'interface Observable pour suivre le modele Observeur Observable
public abstract class Entite implements Observable {
    private List<Observeur> observeurs = new ArrayList<>();

    @Override
    public void ajouterObserveur(Observeur observeur) {
        observeurs.add(observeur);
    }
    @Override
    public void retirerObserveur(Observeur observeur) {
        observeurs.remove(observeur);
    }
    @Override
    public void notifierObserveurs(String evenement) {
        for (Observeur observeur : observeurs) {
            observeur.miseAJour(evenement, this);
        }
    }

	//permet de visualiser dans la vue du terminal
    abstract public String getRepresentation();
}
