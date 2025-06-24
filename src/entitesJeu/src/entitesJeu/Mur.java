package entitesJeu;

import outilsJeu.Vecteur;

public class Mur extends Entite {
    private Vecteur coordonnee;

    public Mur(){
        coordonnee = new Vecteur();
    }
    public Mur(Vecteur coordonnee){
        this.coordonnee = coordonnee;
    }

    public Vecteur getCoordonnee(){ return coordonnee; }
    public void setCoordonnee(Vecteur coordonnee){ this.coordonnee = coordonnee; }

    @Override
    public String getRepresentation() {
        return "◻";
    }
    @Override
    public String toString(){
        return "Mur: (" + coordonnee + ")";
    }
}
