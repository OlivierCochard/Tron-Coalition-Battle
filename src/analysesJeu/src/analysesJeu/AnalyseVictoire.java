package analyseJeu;

import outilsJeu.Vecteur;
import entitesJeu.Tron;
import plateauJeu.modele.Plateau;
import plateauJeu.modele.placement.TronPlacementChaos;
import plateauJeu.modele.placement.TronPlacementStrategie;
import plateauJeu.controlleur.GeneralControlleur;

//calcule le taux de victoire en pourcentage des ia de l'equipe 1 sur x parties avec un grand nombre de parametres
public class AnalyseVictoire {
    static int tailleMap = 20;

    static String iaEquipe1 = "MaxNSOS";
    static String iaEquipe2 = "ParanoideSOS";
    static int profondeurRecherche = 5;

    static int tailleEquipe1 = 2;
    static int tailleEquipe2 = 2;

    static int quantiteAnalyse = 100;
    static int winAmount;

    public static void main(String[] args){
        for (int i = 0; i < quantiteAnalyse; i++){
            Plateau plateau = new Plateau(tailleMap, tailleMap, tailleEquipe1+tailleEquipe2, tailleEquipe1, tailleEquipe2);
            
            TronPlacementStrategie tronPlacementStrategie = tronPlacementStrategie = new TronPlacementChaos();
            tronPlacementStrategie.placerTrons(tailleMap, tailleEquipe1+tailleEquipe2, plateau);
            
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
                

                String direction = generalControlleur.obtenirDirectionIA(ia, profondeurRecherche);
                generalControlleur.appliquerDeplacement(new Vecteur(direction), tronCourant);
            }

            Tron tronGagnant = plateau.getGagnantSolo();
            if (tailleEquipe1 != 1 || tailleEquipe2 != 1){
                tronGagnant = plateau.getGagnantEquipe();
                if (plateau.getEquipe(tronGagnant) == plateau.getEquipe(tron1)){
                    winAmount++;
                    System.out.println(i + " -> Victoire");
                } else {
                    System.out.println(i + " -> Défaite");
                }
            } else {
                if (tronGagnant == tron1){
                    winAmount++;
                    System.out.println(i + " -> Victoire");
                } else {
                    System.out.println(i + " -> Défaite");
                }
            } 
        }
        float pourcentage = winAmount * 100.0f / quantiteAnalyse;
        System.out.println("\nRésultat: " + winAmount + "/" + quantiteAnalyse + " <=> " + pourcentage + "%");
    }   
}
