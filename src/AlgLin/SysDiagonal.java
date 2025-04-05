package AlgLin;

import java.io.*;
import java.util.*;

/**
 * Classe représentant un système linéaire diagonal.
 * Cette classe résout un système linéaire où la matrice est diagonale, 
 * c'est-à-dire que tous les coefficients en dehors de la diagonale sont nuls.
 * Elle étend la classe abstraite SysLin et implémente la méthode de résolution pour les systèmes diagonaux.
 */
public class SysDiagonal extends SysLin {

	/**
     * Constructeur du système linéaire diagonal.
     * Ce constructeur initialise un système linéaire diagonal avec une matrice et un second membre donnés.
     * 
     * @param matriceSystem La matrice du système diagonal.
     * @param secondMembre Le second membre du système.
     * @throws IrregularSysLinException Si la matrice n'est pas carrée ou si la taille du second membre est incorrecte.
     */
	public SysDiagonal(Matrice matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
		super(matriceSystem, secondMembre);
	}

	/** 
     * Résout le système linéaire diagonal.
     * La méthode parcourt la diagonale de la matrice et résout chaque équation de la forme
     * coefficient * x = secondMembre, où le coefficient est l'élément diagonal.
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
                throw new IrregularSysLinException();
            }
            solution.remplaceCoef(i, secondMembre.getCoef(i) / diagonalCoefficient); 
        }
		
		return solution;
	}
	
	 /**
     * Méthode principale pour tester la résolution d'un système linéaire diagonal.
     * Cette méthode crée une matrice diagonale et un second membre, résout le système,
     * calcule le résidu et vérifie la solution.
     * 
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
	public static void main(String[] args) {
		try {
	        // Création d'une matrice diagonale
	        double[][] coefficientsDiagonaux = {
	            {2.0, 0.0, 0.0},
	            {0.0, 3.0, 0.0},
	            {0.0, 0.0, 4.0}
	        };
	        
	        Matrice matriceOriginale = new Matrice(coefficientsDiagonaux);
	        System.out.println("Matrice A: \n" + matriceOriginale);
	        
	        // Création d'un vecteur constant
	        double[] constants = {8.0, 15.0, 24.0};
	        Vecteur vecteurOriginal = new Vecteur(constants);
	        System.out.println("Vecteur b: \n" + vecteurOriginal);

	        // Recopie de la matrice et du vecteur avant résolution
	        Matrice matriceCopie = new Matrice(matriceOriginale.nbLigne(), matriceOriginale.nbColonne());
	        matriceCopie.recopie(matriceOriginale);
	        
	        Vecteur vecteurCopie = new Vecteur(vecteurOriginal.getTaille());
	        for (int i = 0; i < vecteurOriginal.getTaille(); i++) {
	            vecteurCopie.remplaceCoef(i, vecteurOriginal.getCoef(i));
	        }

	        // Création du système diagonal
	        SysDiagonal systemeDiagonal = new SysDiagonal(matriceCopie, vecteurCopie);

	        // Résolution du système
	        Vecteur solution = systemeDiagonal.resolution();

	        // Affichage de la solution
	        System.out.println("Solution du système diagonal : \n" + solution);

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