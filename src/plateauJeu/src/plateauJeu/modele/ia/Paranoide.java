package plateauJeu.modele.ia;

import entitesJeu.Mur;
import entitesJeu.Tron;
import entitesJeu.Entite;
import java.util.*;

public class Paranoide implements IA {
    private int x, y;
    private int largeur, hauteur;
    private Entite[][] grille;
    
    private static final Map<int[], String> DIRECTIONS = Map.of(
        new int[]{-1, 0}, "bas",
        new int[]{0, 1}, "droite",
        new int[]{1, 0}, "haut",
        new int[]{0, -1}, "gauche"
    );

    public Paranoide(int x, int y, Entite[][] grille) {
        this.x = x;
        this.y = y;
        this.grille = grille;
        this.hauteur = grille.length;      
        this.largeur = grille[0].length;  
    }
    
    public String obtenirDirection(int profondeur) {
        List<String> meilleuresDirections = new ArrayList<>();
        int maxScore = Integer.MIN_VALUE;

        for (int[] dir : DIRECTIONS.keySet()) {
            int nouveauX = x + dir[1];
            int nouveauY = y + dir[0];

            if (deplacementValide(nouveauX, nouveauY)) {
                int score = evaluerDeplacement(nouveauX, nouveauY, profondeur);

                if (score > maxScore) {
                    maxScore = score;
                    meilleuresDirections.clear();
                    meilleuresDirections.add(DIRECTIONS.get(dir));
                } else if (score == maxScore) {
                    meilleuresDirections.add(DIRECTIONS.get(dir));
                }
            }
        }

        if (meilleuresDirections.isEmpty()) {
            return choisirDirectionAleatoire();
        }

        return meilleuresDirections.get(new Random().nextInt(meilleuresDirections.size()));
    }

    private String choisirDirectionAleatoire() {
        List<String> directionsPossibles = new ArrayList<>();
        for (int[] dir : DIRECTIONS.keySet()) {
            int nouveauX = x + dir[1];
            int nouveauY = y + dir[0];
            if (deplacementValide(nouveauX, nouveauY)) {
                directionsPossibles.add(DIRECTIONS.get(dir));
            }
        }
        return directionsPossibles.isEmpty() ? "haut" : directionsPossibles.get(new Random().nextInt(directionsPossibles.size()));
    }

    private boolean deplacementValide(int x, int y) {
        return x >= 0 && x < largeur && y >= 0 && y < hauteur && grille[y][x] == null;
    }

    private int evaluerDeplacement(int x, int y, int profondeur) {
        if (profondeur == 0) return 0;

        Entite ancienneEntite = grille[y][x];  
        grille[y][x] = new Tron(""); 

        int score = calculerEspaceLibre(x, y) - penaliserZonesFermees(x, y);
        
        for (int[] dir : DIRECTIONS.keySet()) {
            int nouveauX = x + dir[1];
            int nouveauY = y + dir[0];
            if (deplacementValide(nouveauX, nouveauY)) {
                score = Math.max(score, 1 + evaluerDeplacement(nouveauX, nouveauY, profondeur - 1));
            }
        }

        grille[y][x] = ancienneEntite;  
        return score;
    }

    private int calculerEspaceLibre(int x, int y) {
        boolean[][] visite = new boolean[hauteur][largeur];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});
        int espace = 0;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int cx = pos[0], cy = pos[1];

            if (cx < 0 || cx >= largeur || cy < 0 || cy >= hauteur || visite[cy][cx] || grille[cy][cx] != null) {
                continue;
            }

            visite[cy][cx] = true;
            espace++;

            queue.add(new int[]{cx + 1, cy});
            queue.add(new int[]{cx - 1, cy});
            queue.add(new int[]{cx, cy + 1});
            queue.add(new int[]{cx, cy - 1});
        }

        return espace;
    }

    private int penaliserZonesFermees(int x, int y) {
        int zonesFermees = 0;
        for (int[] dir : DIRECTIONS.keySet()) {
            int voisinX = x + dir[1];
            int voisinY = y + dir[0];
            if (estZoneFermee(voisinX, voisinY)) {
                zonesFermees++;
            }
        }
        return zonesFermees * 2;  
    }

    private boolean estZoneFermee(int x, int y) {
        if (!deplacementValide(x, y)) return false;
        int murs = 0;
        for (int[] dir : DIRECTIONS.keySet()) {
            int voisinX = x + dir[1];
            int voisinY = y + dir[0];
            if (voisinX < 0 || voisinX >= largeur || voisinY < 0 || voisinY >= hauteur || grille[voisinY][voisinX] != null) {
                murs++;
            }
        }
        return murs >= 3;  
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
