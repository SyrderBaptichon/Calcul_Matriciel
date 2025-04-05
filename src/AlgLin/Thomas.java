package AlgLin;

/**
 * Cette classe implémente la méthode de Thomas pour résoudre des systèmes linéaires tridiagonaux.
 * 
 * @author Syrder Baptichon
 * @version 1.0
 */
public class Thomas extends SysLin {

    /**
     * Constructeur de la classe Thomas.
     * 
     * @param matriceSystem La matrice tridiagonale du système linéaire.
     * @param secondMembre Le vecteur second membre du système linéaire.
     * @throws IrregularSysLinException Si la matrice est singulière ou si le système est irrégulier.
     */
    public Thomas(Mat3Diag matriceSystem, Vecteur secondMembre) throws IrregularSysLinException {
        super(matriceSystem, secondMembre);
    }

    /**
     * Résout le système linéaire tridiagonal Ax = b en utilisant l'algorithme de Thomas.
     * 
     * @return Le vecteur x contenant la solution du système tridiagonal.
     */
    @Override
    public Vecteur resolution() {
        int n = secondMembre.getTaille();
        Vecteur p = new Vecteur(n);
        Vecteur q = new Vecteur(n);
        Vecteur solution = new Vecteur(n);

        // Étape 1 : Initialisation de p1 et q1
        // p1 = -c1 / b1
        p.remplaceCoef(0, - getMatriceSystem().getCoef(2, 0) / getMatriceSystem().getCoef(1, 0));
        // q1 = d1 / b1
        q.remplaceCoef(0, secondMembre.getCoef(0) / getMatriceSystem().getCoef(1, 0));

        // Étape 2 : Descente (calcul par récurrence des coefficients pk et qk pour k = 1 à n-2)
        for (int k = 1; k <= n - 2; k++) {
            // β = a_{k+1} * p_k + b_{k+1}
            double beta = getMatriceSystem().getCoef(0, k) * p.getCoef(k - 1) + getMatriceSystem().getCoef(1, k);
            
            // p_{k+1} = -c_{k+1} / β
            p.remplaceCoef(k, -getMatriceSystem().getCoef(2, k) / beta);
            
            // q_{k+1} = (d_{k+1} - a_{k+1} * q_k) / β
            q.remplaceCoef(k, (secondMembre.getCoef(k) - getMatriceSystem().getCoef(0, k) * q.getCoef(k - 1)) / beta);
        }

        // Étape 3 : Calcul de x_n
        // x_n = (d_n - a_n * q_{n-1}) / (a_n * p_{n-1} + b_n)
        double a_n = getMatriceSystem().getCoef(0, n - 1);
        double b_n = getMatriceSystem().getCoef(1, n - 1);
        double d_n = secondMembre.getCoef(n - 1);
        solution.remplaceCoef(n - 1, (d_n - a_n * q.getCoef(n - 2)) / (a_n * p.getCoef(n - 2) + b_n));

        // Étape 4 : calcul des x_k pour k = n-1 à 1
        for (int k = n - 2; k >= 0; k--) {
            // x_k = p_k * x_{k+1} + q_k
            solution.remplaceCoef(k, p.getCoef(k) * solution.getCoef(k + 1) + q.getCoef(k));
        }
        
        return solution;
    }

    public static void main(String[] args) throws Exception {
    	try {
    		System.out.println("*** Exemple du TD (exercice 5.7) ***");
	        // Création de la matrice tridiagonale
	        double[][] coefMat3Diag = {
        		{0, -1, -1, -1},  // Sous-diagonale 
	            {2, 2, 2, 2}, // Diagonale principale 
	            {-1, -1, -1, 0} // Sur-diagonale
	        };
	        Mat3Diag matriceOriginale = new Mat3Diag(coefMat3Diag);
	        System.out.println("Matrice A: \n" + matriceOriginale);

	        // Création du vecteur second membre
	        double[] secondMembre = {-2.0, -2.0, -2.0, 23.0};
	        Vecteur vecteurOriginal = new Vecteur(secondMembre);
	        System.out.println("Vecteur b: \n" + vecteurOriginal);

	        // Recopie de la matrice et du vecteur avant la résolution
	        Mat3Diag matriceCopie = new Mat3Diag(matriceOriginale.nbLigne(), matriceOriginale.nbColonne());
	        matriceCopie.recopie(matriceOriginale);
	        
	        Vecteur vecteurCopie = new Vecteur(vecteurOriginal.getTaille());
	        for (int i = 0; i < vecteurOriginal.getTaille(); i++) {
	            vecteurCopie.remplaceCoef(i, vecteurOriginal.getCoef(i));
	        }

	        // Création du système tridiagonal par la méthode de Thomas
	        Thomas thomas = new Thomas(matriceCopie, vecteurCopie);

	        // Résolution du système
	        Vecteur solution = thomas.resolution();

	        // Affichage de la solution
	        System.out.println("Solution du système tridiagonal par la méthode de Thomas : \n" + solution);

	        // Calcul du résidu : r = Ax - b
	        Vecteur residu = new Vecteur(vecteurOriginal.getTaille());
	        Vecteur Ax = Mat3Diag.produit_par_vecteur(matriceCopie, solution);  // Calcul de Ax
	        residu = Vecteur.soustraction(Ax, vecteurOriginal); // Calcul de r = Ax - b

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
