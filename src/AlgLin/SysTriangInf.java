package AlgLin;

import java.io.*;
import java.util.*;

/**
 * Classe représentant un système linéaire triangulaire inférieur.
 * Cette classe résout un système linéaire où la matrice est triangulaire inférieure, 
 * c'est-à-dire que tous les coefficients au-dessus de la diagonale sont nuls.
 * Elle étend la classe abstraite SysLin et implémente la méthode de résolution pour les systèmes triangulaires inférieurs.
 */
public class SysTriangInf extends SysLin {

	/**
     * Constructeur du système linéaire triangulaire inférieur.
     * Ce constructeur initialise un système linéaire triangulaire inférieur avec une matrice et un second membre donnés.
     * 
     * @param matriceSystem La matrice du système triangulaire inférieur.
     * @param secondMembre Le second membre du système.
     * @throws IrregularSysLinException Si la matrice n'est pas carrée ou si la taille du second membre est incorrecte.
     */
	public SysTriangInf(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		super(matriceSystem, secondMembre);
	}

	/** 
     * Résout le système linéaire triangulaire inférieur.
     * La méthode applique la méthode de substitution avant pour résoudre chaque équation
     * de la forme A * x = b, en utilisant la structure triangulaire inférieure de la matrice.
     * Si un coefficient diagonal est nul, une exception IrregularSysLinException est levée.
     * 
     * @return Un vecteur contenant la solution du système.
     * @throws IrregularSysLinException Si un coefficient diagonal est nul.
     */
	@Override
	public Vecteur resolution() throws IrregularSysLinException {
		Vecteur solution =  new Vecteur(getOrdre());
		
		for (int i = 0; i < getOrdre(); i++) {
            double diagonalCoefficient = matriceSystem.getCoef(i, i); 
            if (diagonalCoefficient == 0.0) {
                throw new IrregularSysLinException("Coefficient nul sur la diagonale");
            }
	        double somme = getSecondMembre().getCoef(i);
	        
	        for(int j = 0; j < i; j++) {
	            somme -= getMatriceSystem().getCoef(i, j) * solution.getCoef(j);
	        }
	        
            solution.remplaceCoef(i, somme/diagonalCoefficient); 
        }
		
		return solution;
	}
	
	/**
     * Méthode principale pour tester la résolution d'un système linéaire triangulaire inférieur.
     * Cette méthode crée une matrice triangulaire inférieure et un second membre, résout le système,
     * calcule le résidu et vérifie la solution.
     * 
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     * @throws Exception Si une exception est levée lors de la résolution du système.
     */
	public static void main(String[] args) throws Exception  {
		try {
	        // Création de la matrice triangulaire inférieure
	        double[][] coefficientsTriangInf = {
	            {2.0, 0.0, 0.0},
	            {3.0, 2.0, 0.0},
	            {4.0, 2.0, 1.0}
	        };

	        Matrice matriceOriginale = new Matrice(coefficientsTriangInf);
	        System.out.println("Matrice A: \n" + matriceOriginale);

	        // Création du vecteur second membre
	        double[] secondMembreData = {4.0, 5.0, 3.0};
	        Vecteur vecteurOriginal = new Vecteur(secondMembreData);
	        System.out.println("Vecteur b: \n" + vecteurOriginal);

	        // Recopie de la matrice et du vecteur avant la résolution
	        Matrice matriceCopie = new Matrice(matriceOriginale.nbLigne(), matriceOriginale.nbColonne());
	        matriceCopie.recopie(matriceOriginale);
	        
	        Vecteur vecteurCopie = new Vecteur(vecteurOriginal.getTaille());
	        for (int i = 0; i < vecteurOriginal.getTaille(); i++) {
	            vecteurCopie.remplaceCoef(i, vecteurOriginal.getCoef(i));
	        }

	        // Création du système triangulaire inférieur
	        SysTriangInf systemeTriangInf = new SysTriangInf(matriceCopie, vecteurCopie);

	        // Résolution du système
	        Vecteur solution = systemeTriangInf.resolution();

	        // Affichage de la solution
	        System.out.println("Solution du système triangulaire inférieur :");
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