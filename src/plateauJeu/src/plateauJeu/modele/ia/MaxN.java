
package plateauJeu.modele.ia;

import entitesJeu.Mur;
import entitesJeu.Tron;
import entitesJeu.Entite;
import java.util.*;

public class MaxN implements IA {
    private static final int PENALITE_PROXIMITE = 200;
    private static final int POIDS_ESPACE_LIBRE = 10;
    private static final int DISTANCE_CRITIQUE = 3;
    private static final int PENALITE_COLLISION_IMMINENTE = 500;
    private static final int PROFONDEUR_MAX = 3;
    
    private int x, y;
    private int largeur, hauteur;
    private Entite[][] grille;
    private List<Tron> adversaires;
    
    private static final Map<int[], String> DIRECTIONS = Map.of(
        new int[]{-1, 0}, "bas",
        new int[]{0, 1}, "droite",
        new int[]{1, 0}, "haut",
        new int[]{0, -1}, "gauche"
    );

    public MaxN(int x, int y, Entite[][] grille, List<Tron> adversaires) {
        this.x = x;
        this.y = y;
        this.grille = grille;
        this.hauteur = grille.length;
        this.largeur = grille[0].length;
        this.adversaires = adversaires;
    }

    public String obtenirDirection(int profondeur) {
        profondeur = Math.min(profondeur, PROFONDEUR_MAX);

        if (estBloquee()) {
            return trouverDirectionDeSecours();
        }

        List<String> meilleuresDirections = new ArrayList<>();
        int maxScore = Integer.MIN_VALUE;

        // Évaluation de chaque direction possible
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
            return trouverDirectionDeSecours();
        }

        return meilleuresDirections.get(new Random().nextInt(meilleuresDirections.size()));
    }

    private boolean estBloquee() {
        for (int[] dir : DIRECTIONS.keySet()) {
            int nx = x + dir[1];
            int ny = y + dir[0];
            if (deplacementValide(nx, ny)) {
                return false;
            }
        }
        return true;
    }

    private int evaluerDeplacement(int x, int y, int profondeur) {
        if (profondeur == 0) return 0;

        int score = 0;
        if (estZoneFermee(x, y)) {
            score -= 1000; 
        }

        score += evaluerLongTerme(x, y, profondeur);

        return score;
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
        if (x < 0 || y < 0 || x >= grille[0].length || y >= grille.length) {
            return true; // Hors limites
        }

        if (grille[y][x] instanceof Mur) {
            return true;
        }

        int issues = 0;
        for (int[] dir : DIRECTIONS.keySet()) {
            int voisinX = x + dir[1];
            int voisinY = y + dir[0];

            if (deplacementValide(voisinX, voisinY)) {
                issues++;
            }
        }

        return issues <= 1;
    }
    private int evaluerLongTerme(int x, int y, int profondeur) {
        int score = 0;

        if (estZoneFermee(x, y)) {
            score -= 1000;
        }

        int espaceLibre = calculerEspaceLibre(x, y);
        score += espaceLibre * POIDS_ESPACE_LIBRE;

        for (Tron adversaire : adversaires) {
            int advX = adversaire.getCoordonnee().x;
            int advY = adversaire.getCoordonnee().y;
            
            int scoreAdv = evaluerDeplacementAdversaire(advX, advY, profondeur);
        }

        return score;
    }






    private int evaluerDeplacementAdversaire(int x, int y, int profondeur) {
        Entite original = grille[y][x];
        grille[y][x] = new Tron("ADV");

        int score = evaluerPosition(x, y);

        if (profondeur == 0) {
            grille[y][x] = original;
            return score;
        }

        int meilleurScoreIA = Integer.MIN_VALUE;
        for (int[] dir : DIRECTIONS.keySet()) {
            int nx = this.x + dir[1];
            int ny = this.y + dir[0];

            if (deplacementValide(nx, ny)) {
                int scoreIA = evaluerDeplacement(nx, ny, profondeur - 1);
                meilleurScoreIA = Math.max(meilleurScoreIA, scoreIA);
            }
        }

        grille[y][x] = original;

        if (meilleurScoreIA == Integer.MIN_VALUE) {
            return score;
        }

        return score - meilleurScoreIA;
    }

    private int evaluerPosition(int x, int y) {
        int score = 0;

        int espaceLibre = calculerEspaceLibre(x, y);
        score += espaceLibre * POIDS_ESPACE_LIBRE;

        // Évaluation des adversaires
        for (Tron adversaire : adversaires) {
            int distance = distanceManhattan(x, y,
                adversaire.getCoordonnee().x,
                adversaire.getCoordonnee().y);

            if (distance < DISTANCE_CRITIQUE) {
                score -= PENALITE_PROXIMITE;
            } else {
                score -= distance; 
            }

            if (distance < 2) {
                score -= PENALITE_COLLISION_IMMINENTE;
            }
        }

        // Bonus pour se rapprocher du centre
        int centreX = largeur / 2;
        int centreY = hauteur / 2;
        score += (hauteur + largeur) - distanceManhattan(x, y, centreX, centreY);

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

    private boolean deplacementValide(int x, int y) {
        return x >= 0 && x < largeur && y >= 0 && y < hauteur && grille[y][x] == null;
    }

    private String trouverDirectionDeSecours() {
        for (String dir : DIRECTIONS.values()) {
            int[] delta = getDeltaDirection(dir);
            int nx = x + delta[1];
            int ny = y + delta[0];
            if (deplacementValide(nx, ny)) {
                return dir;
            }
        }

        return "haut";
    }

    private int[] getDeltaDirection(String dir) {
        switch (dir) {
            case "haut": return new int[]{-1, 0};
            case "bas": return new int[]{1, 0};
            case "gauche": return new int[]{0, -1};
            case "droite": return new int[]{0, 1};
            default: return new int[]{0, 0};
        }
    }

    private int distanceManhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
