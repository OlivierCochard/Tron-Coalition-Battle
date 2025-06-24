package outilsJeu;

import outilsJeu.Observable;

import java.util.ArrayList;
import java.util.List;

public interface Observeur {
    //methode qui permet de recevoir les evenement ou informations d'un Observable
    void miseAJour(String evenement, Observable source);
}
