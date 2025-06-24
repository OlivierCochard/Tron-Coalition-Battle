package analyseJeu;

import outilsJeu.Vecteur;
import entitesJeu.Tron;
import plateauJeu.modele.Plateau;
import plateauJeu.modele.placement.TronPlacementChaos;
import plateauJeu.modele.placement.TronPlacementStrategie;
import plateauJeu.controlleur.GeneralControlleur;

//calcule le temps moyen de calcul en ms des ia de l'equipe 1 sur x parties avec un grand nombre de parametres
public class AnalyseTemps{
    static int tailleMap = 20;

    static String iaEquipe1 = "MaxN";
    static String iaEquipe2 = "Paranoide";
    static int profondeurRecherche = 5;

    static int tailleEquipe1 = 1;
    static int tailleEquipe2 = 1;

    static int quantiteAnalyse = 100;

    public static void main(String[] args){
        long tempsTotalGlobal = 0;
        int nombreCoupsGlobal = 0;

        for (int i = 0; i < quantiteAnalyse; i++){
            long tempsTotal = 0;
            int nombreCoups = 0;

            Plateau plateau = new Plateau(tailleMap, tailleMap, tailleEquipe1 + tailleEquipe2, tailleEquipe1, tailleEquipe2);
            TronPlacementStrategie tronPlacementStrategie = new TronPlacementChaos();
            tronPlacementStrategie.placerTrons(tailleMap, tailleEquipe1 + tailleEquipe2, plateau);
            
            GeneralControlleur generalControlleur = new GeneralControlleur(plateau);
            
            Tron tron1 = plateau.getTronCourant();
            //plateau.afficherEquipes();

            while (plateau.getTermine() == false){
                Tron tronCourant = plateau.getTronCourant();
                if (tronCourant.getMort()){
                    plateau.changementTronCourant();
                    continue;
                }

                String ia = iaEquipe2;
                if (tailleEquipe1 != 1 || tailleEquipe2 != 1){
                    if (plateau.getEquipe(tronCourant) == 1){
                        ia = iaEquipe1;
                    }
                } else {
                    if (tronCourant == tron1){
                        ia = iaEquipe1;
                    }
                }
                
                long tempsDebut = System.currentTimeMillis();
                String direction = generalControlleur.obtenirDirectionIA(ia, profondeurRecherche);
                long tempsFin = System.currentTimeMillis();
                long duree = tempsFin - tempsDebut;
                if (tailleEquipe1 != 1 || tailleEquipe2 != 1){
                    if (plateau.getEquipe(tronCourant) == 0){
                        tempsTotal += duree;
                        nombreCoups++;
                    }
                } else {
                    if (tronCourant == tron1){
                        tempsTotal += duree;
                        nombreCoups++;
                    }
                }

                generalControlleur.appliquerDeplacement(new Vecteur(direction), tronCourant);
            }

            System.out.println("Partie " + (i + 1) + " -> Temps total: " + tempsTotal + "ms, Nombre de coups: " + nombreCoups);

            tempsTotalGlobal += tempsTotal;
            nombreCoupsGlobal += nombreCoups;
        }

        double moyenneTempsParCoup = (nombreCoupsGlobal > 0) ? (tempsTotalGlobal / (double) nombreCoupsGlobal) : 0;
        System.out.println("\nTemps total : " + tempsTotalGlobal + "ms / Nombre de coups : " + nombreCoupsGlobal + " <=> Moyenne par coup : " + moyenneTempsParCoup + "ms");
    }   
}
