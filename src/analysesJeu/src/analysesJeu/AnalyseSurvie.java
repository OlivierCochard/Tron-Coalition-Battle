package analyseJeu;

import outilsJeu.Vecteur;
import entitesJeu.Tron;
import plateauJeu.modele.Plateau;
import plateauJeu.modele.placement.TronPlacementChaos;
import plateauJeu.modele.placement.TronPlacementStrategie;
import plateauJeu.controlleur.GeneralControlleur;

//calcule la durée moyenne de survie en tour sur x parties avec un grand nombre de parametres
public class AnalyseSurvie {
    static int tailleMap = 20;

    static String iaEquipe1 = "MaxN";
    static String iaEquipe2 = "MaxN";
    static int profondeurRecherche = 3;

    static int tailleEquipe1 = 1;
    static int tailleEquipe2 = 1;

    static int quantiteAnalyse = 250;

    public static void main(String[] args) {
        int totalToursSurvieEquipe1 = 0;
        int totalJoueursEquipe1 = tailleEquipe1 * quantiteAnalyse;

        for (int i = 0; i < quantiteAnalyse; i++) {
            Plateau plateau = new Plateau(tailleMap, tailleMap, tailleEquipe1 + tailleEquipe2, tailleEquipe1, tailleEquipe2);
            TronPlacementStrategie tronPlacementStrategie = new TronPlacementChaos();
            tronPlacementStrategie.placerTrons(tailleMap, tailleEquipe1 + tailleEquipe2, plateau);
            
            GeneralControlleur generalControlleur = new GeneralControlleur(plateau);
            int tours = 0;

            while (!plateau.getTermine()) {
                int joueursEnVieEquipe1 = 0;

                for (Tron tron : plateau.getTrons()) {
                    if (!tron.getMort() && plateau.getEquipe(tron) == 1) {
                        joueursEnVieEquipe1++;
                    }
                }

                // Ajouter les tours de survie de l'équipe 1
                totalToursSurvieEquipe1 += joueursEnVieEquipe1;

                Tron tronCourant = plateau.getTronCourant();
                if (tronCourant.getMort()) {
                    plateau.changementTronCourant();
                    continue;
                }

                String ia = (plateau.getEquipe(tronCourant) == 1) ? iaEquipe1 : iaEquipe2;

                String direction = generalControlleur.obtenirDirectionIA(ia, profondeurRecherche);
                generalControlleur.appliquerDeplacement(new Vecteur(direction), tronCourant);
                
                tours++;
            }

            System.out.println("Partie " + (i + 1) + " -> Nombre de tours : " + tours);
        }

        double moyenneToursSurvie = (totalJoueursEquipe1 > 0) ? (totalToursSurvieEquipe1 / (double) totalJoueursEquipe1) : 0;
        System.out.println("\nMoyenne de tours de survie par joueur de l'équipe 1 : " + moyenneToursSurvie);
    }
}
