package AlgLin;

/**
 * La classe Mat3Diag représente une matrice tridiagonale. Cette classe hérite de la classe Matrice.
 * 
 * @author Syrder Baptichon
 * @version 1.1
 */
public class Mat3Diag extends Matrice {

	 /**
     * 
     * @param dim1 Le nombre de lignes (doit être égal à 3).
     * @param dim2 Le nombre de colonnes (taille de la matrice tridiagonale).
     * @throws Exception Si la première dimension n'est pas égale à 3.
     */
	public Mat3Diag(int dim1, int dim2) throws Exception {
		super(3, dim2);
		if (dim1 != 3) {
			throw new Exception("La première dimension doit être égale à 3 !");
		}
	}

	/**
     * 
     * @param tableau Le tableau de valeurs à utiliser pour initialiser la matrice.
     * @throws Exception Si le tableau n'a pas 3 lignes.
     */
	public Mat3Diag(double tableau[][]) throws Exception {
		super(tableau);
		if (tableau.length != 3) {
			throw new Exception("Le tableau doit avoir 3 lignes !");
		}
	}

	 /**
     * 
     * @param dim La taille de la matrice (nombre de colonnes).
     */
	public Mat3Diag(int dim) {
		super(3, dim);
	}

	/**
     * Effectue le produit d'une matrice tridiagonale par un vecteur.
     * 
     * @param matrice La matrice tridiagonale.
     * @param vecteur Le vecteur à multiplier.
     * @return Le vecteur résultant du produit matrice-vecteur.
     * @throws IrregularSysLinException Si les dimensions de la matrice et du vecteur ne sont pas compatibles.
     */
	public static Vecteur produit_par_vecteur(Mat3Diag matrice, Vecteur vecteur) throws IrregularSysLinException {
		int dim = matrice.nbColonne(); //Récupère la taille du système / dimension de la matrice
		
	    // Vérifie que la taille du vecteur est compatible avec la matrice
		if (dim != vecteur.getTaille()) {
			throw new IrregularSysLinException("Les dimensions de la matrice et du vecteur sont incompatibles.");
		}

		Vecteur resultat = new Vecteur(dim);
		double somme; // Initialisation de la somme pour la ligne actuelle

	    // Parcours de chaque ligne de la matrice (c'est-à-dire chaque équation du système)
		for (int i = 0; i < dim; ++i) {
			somme = 0;
			
	        // Contribution de la sous-diagonale
			if (i > 0) { 
				somme += vecteur.getCoef(i - 1) * matrice.getCoef(0, i);
			}
			
	        // Contribution de la diagonale principale
			somme += vecteur.getCoef(i) * matrice.getCoef(1, i);
			
	        // Contribution de la sur-diagonale 
			if (i < dim - 1) {
				somme += vecteur.getCoef(i + 1) * matrice.getCoef(2, i);
			}
			
	        // Stocke la valeur calculée dans le vecteur résultat
			resultat.remplaceCoef(i, 0, somme);
		}

		return resultat;
	}

	/**
     * 
     * @return Une chaîne de caractères représentant la matrice tridiagonale.
     */
	@Override
	public String toString() {
	    int n = this.nbColonne(); // Taille de la matrice
	    StringBuilder matr = new StringBuilder();

	    for (int i = 0; i < n; i++) {
	        for (int j = 0; j < n; j++) {
	            if (i == j) {
	                // Diagonale principale
	                matr.append(this.getCoef(1, j)).append(" ");
	            } else if (i == j + 1) {
	                // Sous-diagonale (commence à partir de la deuxième colonne)
	                matr.append(this.getCoef(0, j + 1)).append(" "); 
	            } else if (i == j - 1) {
	                // Sur-diagonale (s'arrête à l'avant-dernière colonne)
	                matr.append(this.getCoef(2, i)).append(" ");
	            } else {
	                // Hors des diagonales (zéro)
	                matr.append("0.0 ");
	            }
	        }
	        matr.append("\n"); // Nouvelle ligne après chaque ligne de la matrice
	    }

	    return matr.toString();
	}
	
		
	public static void main(String[] args) {
		try {
			/**************************** TEST MATRICE 3x3 *************************/
			// Matrice 3x3
	        System.out.println("Test matrice 3x3 :");
			double[][] coef = {
					{0, 2, 4},
					{3, 1, -5},
					{5, 10, 0}
			};
			Mat3Diag mat3Diag = new Mat3Diag(coef);
			System.out.println("Matrice tridiagonale \n" + mat3Diag);
	
			// Vecteur
			double[] tab = {1, 2, 3};
			Vecteur vecteur = new Vecteur(tab);	
			System.out.println("Vecteur \n" + vecteur);
	
			// Produit de la matrice tridiagonale par le vecteur
			Vecteur resultat = produit_par_vecteur(mat3Diag, vecteur);	
			System.out.println("Résultat du produit \n" + resultat);
			
			/**************************** TEST MATRICE 5x5 *************************/
			// Matrice 5x5
	        System.out.println("Test matrice 5x5 :");
	        double[][] coef1 = {
	            {0, 1, 2, 3, 4},  // Sous-diagonale 
	            {5, 6, 7, 8, 9}, // Diagonale principale 
	            {11, 12, 13, 8, 0} // Sur-diagonale 
	        };
	        Mat3Diag mat3Diag1 = new Mat3Diag(coef1);
	        System.out.println("Matrice tridiagonale \n" + mat3Diag1);
	
	        double[] tab1 = {1, 2, 3, 4, 5};
	        Vecteur vecteur1 = new Vecteur(tab1);
	        System.out.println("Vecteur \n" + vecteur1);
	
			// Produit de la matrice tridiagonale par le vecteur
	        Vecteur resultat1 = Mat3Diag.produit_par_vecteur(mat3Diag1, vecteur1);
	        System.out.println("Résultat \n" + resultat1);
			
	        /************************** TEST AVEC MATRICE DE MAUVAISE TAILLE *******************/
	        System.out.println("Test matrice de mauvaise dimension :");
	        double[][] coefficientsMat3diag2 = {
                {1, 1},  
                {8, 1, -3}, 
                {0, 4} 
            }; // le tableau doit être de dimension 3x3
            Mat3Diag mat3Diag2 = new Mat3Diag(coefficientsMat3diag2);

            double[] tab2 = {1, 2, 6}; 
            Vecteur vecteur2 = new Vecteur(tab);

            System.out.println("Test vecteur taille incorrecte :");
            Vecteur resultat2 = Mat3Diag.produit_par_vecteur(mat3Diag2, vecteur);
            System.out.println("Résultat : " + resultat);
            
		} catch (IrregularSysLinException e) {
	        System.err.println("Erreur lors de la résolution du système linéaire : " + e.getMessage());
	        e.printStackTrace();
	    } catch (Exception e) {
	        System.err.println("Erreur inattendue : " + e.getMessage());
	        e.printStackTrace();
	    }
	}
}
