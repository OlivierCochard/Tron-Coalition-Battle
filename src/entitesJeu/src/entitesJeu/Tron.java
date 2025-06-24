package entitesJeu;

import outilsJeu.Vecteur;

public class Tron extends Mur {
	private String nom;
	private boolean mort;

	public Tron(String nom) {
        super();
        this.nom = nom;
        this.mort = false;
    }

    public Tron(String nom, Vecteur coordonnee) {
        super(coordonnee);
        this.nom = nom;
        this.mort = false;
    }

	public String getNom(){ return nom; }
	public boolean getMort(){ return mort; }

    public void setMort(boolean mort){ this.mort = mort; }
    
    //systeme Observable Observeur, quand l'observeur demande de detruire l'entité
    public void destruction(){
    	String tmp = "destruction";
    	notifierObserveurs(tmp);
    	//System.out.println("[" + nom + "] est mort");
    }
    
    @Override
    public String getRepresentation() {
     // Retourne un caractère par défaut si le nom est vide ou null
        if (nom == null || nom.isEmpty()) {
            return "?";
        }
        if (mort == true){
            return "X";
        }
        return "" + nom.charAt(nom.length() - 1);
    }
    @Override
    public String toString(){
        return "Tron: " + nom + " | " + "mort: " + mort + " | " + "coordonnee: (" + getCoordonnee() + ")";
    }
}
