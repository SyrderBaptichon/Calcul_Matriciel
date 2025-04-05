package AlgLin;

import java.io.*;
import java.util.*;

/**
 * Classe représentant un système linéaire triangulaire supérieur.
 * Cette classe résout un système linéaire où la matrice est triangulaire supérieure.
 * Elle étend la classe `SysLin` et applique la méthode de substitution arrière pour résoudre le système.
 */
public class SysTriangSup extends SysLin {
	
	/**
     * Constructeur du système linéaire triangulaire supérieur.
     * Ce constructeur initialise un système linéaire triangulaire supérieur avec une matrice et un second membre donnés.
     * 
     * @param matriceSystem La matrice du système triangulaire supérieur.
     * @param secondMembre Le second membre du système.
     * @throws IrregularSysLinException Si la matrice est irrégulière ou si le second membre est incompatible.
     */
	public SysTriangSup(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		super(matriceSystem, secondMembre);
	}
 
	/**
     * Résout le système linéaire triangulaire supérieur.
     * La méthode applique la méthode de substitution arrière pour résoudre chaque équation de la forme A * x = b,
     * en utilisant la structure triangulaire supérieure de la matrice.
     * 
     * @return Un vecteur contenant la solution du système.
     * @throws IrregularSysLinException Si un coefficient nul est trouvé sur la diagonale de la matrice.
     */
	@Override
	public Vecteur resolution() throws IrregularSysLinException {
	    Vecteur solution = new Vecteur(getOrdre());

	    for(int i = getOrdre() - 1; i >= 0; i--) {
	    	 double coeff = getMatriceSystem().getCoef(i, i);
	            if (coeff == 0) { 
	                throw new IrregularSysLinException("Coefficient nul sur la diagonale.");
	            }

	            double somme = getSecondMembre().getCoef(i);

	            for (int j = i + 1; j < getOrdre(); j++) {
	            	somme -= getMatriceSystem().getCoef(i, j) * solution.getCoef(j);
	            }

	            solution.remplaceCoef(i, somme / coeff);
	    }
	    return solution;
	}
	
	/**
     * Méthode principale pour tester la résolution d'un système linéaire triangulaire supérieur.
     * Cette méthode crée une matrice triangulaire supérieure et un second membre, résout le système,
     * calcule le résidu et vérifie la solution.
     * 
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
	public static void main(String[] args) {
		try {
	        // Création de la matrice triangulaire supérieure
	        double[][] coefficientsTriangSup = {
	            {1.0, 2.0, 4.0},
	            {0.0, 3.0, 5.0},
	            {0.0, 0.0, 6.0}
	        };

	        Matrice matriceOriginale = new Matrice(coefficientsTriangSup);
	        System.out.println("Matrice A: \n" + matriceOriginale);

	        // Création du vecteur second membre
	        double[] secondMembreData = {7.0, 8.0, 9.0};
	        Vecteur vecteurOriginal = new Vecteur(secondMembreData);
	        System.out.println("Vecteur b: \n" + vecteurOriginal);

	        // Recopie de la matrice et du vecteur avant la résolution
	        Matrice matriceCopie = new Matrice(matriceOriginale.nbLigne(), matriceOriginale.nbColonne());
	        matriceCopie.recopie(matriceOriginale);
	        
	        Vecteur vecteurCopie = new Vecteur(vecteurOriginal.getTaille());
	        for (int i = 0; i < vecteurOriginal.getTaille(); i++) {
	            vecteurCopie.remplaceCoef(i, vecteurOriginal.getCoef(i));
	        }

	        // Création du système triangulaire supérieur
	        SysTriangSup systemeTriangSup = new SysTriangSup(matriceCopie, vecteurCopie);

	        // Résolution du système
	        Vecteur solution = systemeTriangSup.resolution();

	        // Affichage de la solution
	        System.out.println("Solution du système triangulaire supérieur :");
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
	        System.out.println("Le système linéaire est irrégulier.");
	        e.printStackTrace();
	    }
	}
}
