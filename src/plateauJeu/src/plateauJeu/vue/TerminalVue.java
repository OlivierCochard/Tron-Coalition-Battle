package plateauJeu.vue;

import plateauJeu.modele.Plateau;
import entitesJeu.Mur;
import entitesJeu.Tron;
import entitesJeu.Entite;

public class TerminalVue {
	private Plateau plateau;

	public TerminalVue(Plateau plateau){
    	this.plateau = plateau;
    }

    @Override
    public String toString() {
        StringBuilder plateauString = new StringBuilder(plateau.getHauteur()-1 + " | ");
	    for (int y = plateau.getHauteur()-1; y >= 0; y--) {
	    	if (y != plateau.getHauteur()-1){
	    		plateauString.append("\n");
	    		plateauString.append(y + " | ");
	    	}
	        for (int x = 0; x < plateau.getLargeur(); x++) {
	            if (plateau.getGrille()[y][x] != null) {
	                plateauString.append(plateau.getGrille()[y][x].getRepresentation()).append(" ");
	            } else {
	                plateauString.append(". ");
	            }
	        }
	    }
	    return plateauString.toString();
    }
}