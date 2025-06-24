package plateauJeu.modele;

import entitesJeu.Mur;
import entitesJeu.Tron;
import entitesJeu.Entite;
import outilsJeu.Observable;
import outilsJeu.Observeur;
import outilsJeu.Vecteur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Plateau implements Observeur {
    private int largeur;
    private int hauteur;
    private Entite[][] grille;
    private List<Tron> trons;
    private Tron tronCourant;
    private int[] equipes;

    public Plateau(int largeur, int hauteur, int nbJoueurs, int tailleEquipe) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.grille = new Entite[hauteur][largeur];
        this.trons = new ArrayList<>();

        for (int i = 0; i < nbJoueurs; i++) {
            Tron tron = new Tron("Bot 0" + (i + 1));
            trons.add(tron);
            tron.ajouterObserveur(this);
        }
        tronCourant = trons.get(0);
		
		//EQUIPE
        if (tailleEquipe == 1) return;
        
        creerEquipes(nbJoueurs, tailleEquipe);
    }

    public Plateau(int largeur, int hauteur, int nbJoueurs, int nbJoueursEquipe1, int nbJoueursEquipe2) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.grille = new Entite[hauteur][largeur];
        this.trons = new ArrayList<>();

        for (int i = 0; i < nbJoueurs; i++) {
            Tron tron = new Tron("Bot 0" + (i + 1));
            trons.add(tron);
            tron.ajouterObserveur(this);
        }
        tronCourant = trons.get(0);
		
		//EQUIPE
        if ((nbJoueurs != nbJoueursEquipe1 + nbJoueursEquipe2) && (nbJoueursEquipe1 == 1 && nbJoueursEquipe2 == 1)) return;
        creerEquipes(nbJoueurs, nbJoueursEquipe1, nbJoueursEquipe2);
    }

    public int getEquipe(Tron tron){
        int indexTron = trons.indexOf(tron);
        return equipes[indexTron];
    }
    

    private void creerEquipes(int nbJoueurs, int tailleEquipe) {
        equipes = new int[nbJoueurs]; 
        int equipeId = 0;
        int joueurIndex = 0;

        while (joueurIndex < nbJoueurs) {
            int joueursDansEquipe = Math.min(tailleEquipe, nbJoueurs - joueurIndex);

            for (int i = 0; i < joueursDansEquipe; i++) {
                equipes[joueurIndex++] = equipeId;
            }

            equipeId++;
        }
    }
    //POUR 2 EQUIPES
    private void creerEquipes(int nbJoueurs, int tailleEquipe1, int tailleEquipe2) {
        equipes = new int[nbJoueurs]; 

        for (int i = tailleEquipe1; i < nbJoueurs; i++){
            equipes[i] = 1;
        }
    }
    
    public void afficherEquipes() {
        System.out.println("Répartition des équipes :");
		if (equipes == null){
			System.out.println("Chacun pour soi !");
			return;
		}
        
        for (int i = 0; i < equipes.length; i++) {
            System.out.println("Joueur " + (i + 1) + " → Équipe " + equipes[i]);
        }
    }

	public int[] getEquipes(){
		return equipes;
	}
    public int getLargeur() {
        return largeur;
    }
    public int getHauteur() {
        return hauteur;
    }
    public Entite[][] getGrille() {
        return grille;
    }
    public List<Tron> getTrons() {
        return trons;
    }
    public Tron getTronCourant() {
        return tronCourant;
    }

	public boolean getTermine(){
		//VERIF SI TERMINE SUIVANT CHACUN POUR SOI OU EQUIPES
		boolean termine = true;
		if (equipes == null){
			termine = getTermineSolo();
		}
		else {
			termine = getTermineEquipe();
		}
		return termine;
	}
	public boolean getTermineSolo() {
        int nbTronsVivants = 0;
        for (Tron tron : trons){
            if (tron.getMort() == false){
                nbTronsVivants++;
            }
        }
        return nbTronsVivants < 2;
    }
    public boolean getTermineEquipe() {
		int nbTronsVivants = 0;
		Set<Integer> equipesVivantes = new HashSet<>();
		// Vérifier le nombre de trons vivants et l'état des équipes
		for (int i = 0; i < trons.size(); i++) {
			Tron tron = trons.get(i);
			if (!tron.getMort()) {
				nbTronsVivants++; 
				equipesVivantes.add(equipes[i]); 
			}
		}

		// Si moins de 2 trons vivants, le jeu est terminé
		if (nbTronsVivants < 2) {
			return true;
		}

		// Vérifier si une seule équipe est vivante
		return equipesVivantes.size() <= 1;
	}

    public Tron getGagnantSolo() {
        for (Tron tron : trons){
            if (tron.getMort() == false){
                return tron;
            }
        }
        return null;
    }
    public Tron getGagnantEquipe() {
        for (Tron tron : trons){
            if (tron.getMort() == false){
                return tron;
            }
        }
        return null;
    }

    public void changementTronCourant(){
        int indexCourant = getIndexTronCourant();
        if (indexCourant + 1 >= trons.size()){
            indexCourant = 0;
        }
        else {
            indexCourant++;
        }
        tronCourant = trons.get(indexCourant);
    }
    public int getIndexTronCourant(){
        for (int i = 0; i < trons.size(); i++){
            if (tronCourant == trons.get(i)){
                return i;
            }
        }
        return -1;
    }

    public void ajouterEntite(Entite entite, int x, int y) {
        if (estPositionValide(x, y)) {
            grille[y][x] = entite;

            if (entite instanceof Mur){
                Mur murEntite = (Mur) entite;
                murEntite.setCoordonnee(new Vecteur(x, y));
            }
        } else {
            throw new IndexOutOfBoundsException("Position invalide sur le plateau.");
        }
    }
    public Entite obtenirEntite(int x, int y) {
        if (estPositionValide(x, y)) {
            return grille[y][x];
        } else {
            throw new IndexOutOfBoundsException("Position invalide sur le plateau.");
        }
    }
    public void retirerEntite(int x, int y) {
        if (estPositionValide(x, y)) {
            grille[y][x] = null;
        } else {
            throw new IndexOutOfBoundsException("Position invalide sur le plateau.");
        }
    }

    private boolean estPositionValide(int x, int y) {
        if (x < 0 || x >= largeur || y < 0 || y >= hauteur){
            return false;
        }
        return true;
    }
    public boolean estClasseValide(int x, int y){
        if (estPositionValide(x, y) == false) return false;

        if (grille[y][x] != null){
            return false;
        }
        return true;
    }

    public void placerJoueursOptimal(int nbJoueurs) {
        List<int[]> positionsJoueurs = new ArrayList<>();

        // 4 max
        int[][] coins = {
            {0, 0}, // 
            {largeur - 1, hauteur - 1},
            {0, hauteur - 1}, 
            {largeur - 1, 0}, 
        };
        for (int i = 0; i < Math.min(nbJoueurs, 4); i++) {
            int[] coin = coins[i];
            positionsJoueurs.add(coin);
            ajouterEntite(trons.get(i), coin[0], coin[1]);
        }

        // 9 max
        int[][] cotes = {
            {largeur/2, 0}, 
            {largeur/2, hauteur - 1}, 
            {0, hauteur/2}, 
            {largeur - 1, hauteur/2},
        };
        int nbJoueursRestants = Math.min(nbJoueurs-4, 5);
        for (int i = 0; i < nbJoueursRestants; i++) {
            if (i == nbJoueursRestants - 1 && nbJoueursRestants % 2 != 0){
                int[] milieu = {largeur/2, largeur/2};
                positionsJoueurs.add(milieu);
                ajouterEntite(trons.get(nbJoueursRestants - 1), milieu[0], milieu[1]);
                break;
            }

            int[] cote = cotes[i];
            positionsJoueurs.add(cote);
            ajouterEntite(trons.get(i+4), cote[0], cote[1]);
        }
    }


    public void placerTronsAleatoire(int distanceMin) {
        Random random = new Random();
        List<int[]> positionsTrons = new ArrayList<>();

        // Vérification de la distance minimale
        int espaceNecessaire = (int) Math.ceil(Math.sqrt(trons.size()) * distanceMin);
        if (espaceNecessaire > Math.min(largeur, hauteur)) {
            System.out.println("La distance minimale est trop grande pour cette taille de plateau.");
            return;
        }

        // Boucle pour placer chaque Tron
        for (Tron tron : trons) {
            boolean positionValide = false;
            int x = 0, y = 0;

            // Chercher une position valide pour le Tron
            while (!positionValide) {
                x = random.nextInt(largeur); 
                y = random.nextInt(hauteur); 

                positionValide = true;
                for (int[] pos : positionsTrons) {
                    int dx = pos[0] - x;
                    int dy = pos[1] - y;
                    if (Math.sqrt(dx * dx + dy * dy) < distanceMin) {
                        positionValide = false;
                        break;
                    }
                }
            }

            positionsTrons.add(new int[] { x, y });
            ajouterEntite(tron, x, y); 
        }
    }

    private void tuerTron(Tron tron){
        tron.setMort(true);
    }

    public void afficherTrons() {
        System.out.println("Liste des trons :");
        for (Tron tron : trons) {
            System.out.println(tron.getNom() + tron.getCoordonnee());
        }
    }
    public void afficherPlateau() {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                if (grille[y][x] != null) {
                    System.out.print(grille[y][x].getRepresentation() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void miseAJour(String evenement, Observable source){
        if (evenement.equals("destruction")){
            tuerTron((Tron) source);
            return;
        }
        System.out.println("Evenement (" + evenement + ") inconnu");
    }
}
