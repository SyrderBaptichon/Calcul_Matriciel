package AlgLin;

import java.io.*;
import java.util.*;

/**
 * Classe représentant un système linéaire triangulaire inférieur avec une diagonale unitaire.
 * Cette classe résout un système linéaire où la matrice est triangulaire inférieure avec tous les éléments 
 * de la diagonale égaux à 1.
 * Elle étend la classe `SysTriangInf` et implémente la vérification de la diagonale unitaire ainsi que la résolution 
 * du système.
 */
public class SysTriangInfUnite extends SysTriangInf {
	
	/**
     * Constructeur du système linéaire triangulaire inférieur avec diagonale unitaire.
     * Ce constructeur initialise un système linéaire triangulaire inférieur avec une matrice et un second membre donnés.
     * Il vérifie que la matrice possède une diagonale unitaire (tous les éléments de la diagonale doivent être égaux à 1).
     * 
     * @param matriceSystem La matrice du système triangulaire inférieur avec diagonale unitaire.
     * @param secondMembre Le second membre du système.
     * @throws IrregularSysLinException Si la matrice n'est pas carrée, si les éléments de la diagonale ne sont pas égaux à 1,
     *                                  ou si la taille du second membre est incorrecte.
     */
	public SysTriangInfUnite(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
        super(matriceSystem, secondMembre);
        validerMatriceSysteme();
    }

	/**
     * Valide que tous les éléments de la diagonale de la matrice sont égaux à 1.
     * Si l'un des éléments de la diagonale est différent de 1, une exception `IrregularSysLinException` est levée.
     * 
     * @throws IrregularSysLinException Si un élément de la diagonale est différent de 1.
     */
    private void validerMatriceSysteme() throws IrregularSysLinException {
        // Assurez-vous que les éléments de la diagonale sont tous égaux à 1
        for (int i = 0; i < matriceSystem.nbLigne(); i++) {
            if (Math.abs(matriceSystem.getCoef(i, i) - 1.0) > Matrice.EPSILON) {
                throw new IrregularSysLinException("Les éléments de la diagonale doivent être égaux à 1 dans un système triangulaire inférieur avec diagonale unitaire.");
            }
        }
    }
    
    /**
     * Résout le système linéaire triangulaire inférieur avec diagonale unitaire.
     * La méthode applique la méthode de substitution avant pour résoudre chaque équation de la forme A * x = b,
     * en utilisant la structure triangulaire inférieure de la matrice et sachant que les éléments diagonaux sont égaux à 1.
     * 
     * @return Un vecteur contenant la solution du système.
     */
    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        Vecteur solution = new Vecteur(getOrdre());

        for (int i = 0; i < getOrdre(); i++) {
            double somme = getSecondMembre().getCoef(i);
            for (int j = 0; j < i; j++) {
                somme -= getMatriceSystem().getCoef(i, j) * solution.getCoef(j);
            }
            solution.remplaceCoef(i, somme);
        }

        return solution;
    }
    
    /**
     * Méthode principale pour tester la résolution d'un système linéaire triangulaire inférieur avec diagonale unitaire.
     * Cette méthode crée une matrice triangulaire inférieure avec diagonale unitaire et un second membre,
     * résout le système, calcule le résidu et vérifie la solution.
     * 
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
    	try {
            // Création de la matrice triangulaire inférieure avec diagonale unitaire
            double[][] coefficientsTriangInfUnite = {
                {1.0, 0.0, 0.0},
                {2.0, 1.0, 0.0},
                {4.0, 5.0, 1.0}
            };

            Matrice matriceOriginale = new Matrice(coefficientsTriangInfUnite);
	        System.out.println("Matrice A: \n" + matriceOriginale);

            // Création du vecteur second membre
            double[] secondMembreData = {3.0, 7.0, 18.0};
            Vecteur vecteurOriginal = new Vecteur(secondMembreData);
	        System.out.println("Vecteur b: \n" + vecteurOriginal);

            // Recopie de la matrice et du vecteur avant la résolution
            Matrice matriceCopie = new Matrice(matriceOriginale.nbLigne(), matriceOriginale.nbColonne());
            matriceCopie.recopie(matriceOriginale);
            
            Vecteur vecteurCopie = new Vecteur(vecteurOriginal.getTaille());
            for (int i = 0; i < vecteurOriginal.getTaille(); i++) {
                vecteurCopie.remplaceCoef(i, vecteurOriginal.getCoef(i));
            }

            // Création du système triangulaire inférieur avec diagonale unitaire
            SysTriangInfUnite systemeTriangInfUnite = new SysTriangInfUnite(matriceCopie, vecteurCopie);

            // Résolution du système
            Vecteur solution = systemeTriangInfUnite.resolution();

            // Affichage de la solution
            System.out.println("Solution du système triangulaire inférieur avec diagonale unitaire :");
            System.out.println(solution);

            // Calcul du résidu : r = Ax - b
            Vecteur residu = new Vecteur(vecteurOriginal.getTaille());
            for (int i = 0; i < residu.getTaille(); i++) {
                double somme = 0.0;
                for (int j = 0; j < matriceOriginale.nbColonne(); j++) {
                    somme += matriceOriginale.getCoef(i, j) * solution.getCoef(j);
                }
                residu.remplaceCoef(i, somme - vecteurOriginal.getCoef(i));
            }

            // Calcul de la norme infinie du résidu
            double normeResidu = residu.normeInfini();
            System.out.println("Norme du résidu : " + normeResidu);

            // Vérification de la solution
            if (normeResidu < Matrice.EPSILON) {
                System.out.println("Solution validée !");
            } else {
                System.out.println("Erreur : Résidu non nul.");
            }

        } catch (IrregularSysLinException e) {
            System.out.println("Le système linéaire est irrégulier : ");
            e.printStackTrace();
        }
    }
}
