/**
 * La classe Matrice permet de représenter et de manipuler des matrices.
 * @author bs214279
 * @version 1.1
 */

package AlgLin;

import java.io.*;
import java.util.*;


public class Matrice {
	/** Définir ici les attributs de la classe **/

	/**
	 * Tableau 2D contenant les coefficients de la matrice.
	 */
	protected double coefficient[][];

	public static final double EPSILON = 1.0E-06;

	/** Définir ici les constructeur de la classe **/

	/**
	 * Construit une matrice de dimensions spécifiées.
	 * @param nbligne  Le nombre de lignes de la matrice
	 * @param nbcolonne  Le nombre de colonnes de la matrice
	 */
	Matrice (int nbligne, int nbcolonne){
		this.coefficient = new double[nbligne][nbcolonne];
	}

	/**
	 * Construit une matrice à partir d'un tableau à 2 dimensions.
	 *
	 * @param tableau	Tableau à 2 dimensions dont les coefficients sont de type double
	 */
	Matrice(double[][] tableau){
		coefficient = tableau;
	}

	/**
	 * Construit une matrice à partir d'un fichier.
	 *
	 * @param fichier	Fichier qui contiendra le nombre de lignes et le nombre de colonnes de la matrice
	 */
	Matrice(String fichier){
		try {
			Scanner sc = new Scanner(new File(fichier));
			int ligne = sc.nextInt();
			int colonne = sc.nextInt();
			this.coefficient = new double[ligne][colonne];
			for(int i=0; i < ligne;i++)
				for(int j = 0; j < colonne; j++)
					this.coefficient[i][j] = sc.nextDouble();
			sc.close();	
		}
		catch(FileNotFoundException e) {
			System.out.println("Fichier absent");
		}
	}

	/** Definir ici les autres methodes */

	/**
	 * Recopie les coefficients d'une matrice donnée dans la matrice actuelle.
	 * @param arecopier La matrice dont les coefficients sont copiés dans la matrice actuelle.
	 */
	public void recopie(Matrice arecopier){
		int ligne, colonne;
		ligne = arecopier.nbLigne(); 
		colonne = arecopier.nbColonne();
		this.coefficient = new double[ligne][colonne];
		for(int i = 0; i < ligne; i++)
			for (int j = 0;j < colonne;j++)
				this.coefficient[i][j] = arecopier.coefficient[i][j];	
	}

	/**
	 * Retourne le nombre de lignes de la matrice.
	 * @return Le nombre de lignes de la matrice.
	 */
	public int nbLigne(){
		return this.coefficient.length;
	}

	/**
	 * Retourne le nombre de colonnes de la matrice.
	 * @return Le nombre de colonnes de la matrice.
	 */
	public int nbColonne(){
		return this.coefficient[0].length;
	}


	/**
	 * Retourne la valeur du coefficient à la position spécifiée par la ligne et la colonne.
	 * @param ligne L'indice de la ligne du coefficient à obtenir.
	 * @param colonne L'indice de la colonne du coefficient à obtenir.
	 * @return La valeur du coefficient à la position donnée.
	 */
	public double getCoef(int ligne, int colonne){
		return this.coefficient[ligne][colonne];
	}

	/**
	 * Remplace le coefficient à la position spécifiée par une nouvelle valeur.
	 * @param ligne L'indice de la ligne du coefficient à remplacer.
	 * @param colonne L'indice de la colonne du coefficient à remplacer.
	 * @param value La nouvelle valeur du coefficient à mettre à la position spécifiée.
	 */
	public void remplaceCoef(int ligne, int colonne, double value){
		this.coefficient[ligne][colonne] = value;
	}

	/**
	 * Retourne une représentation sous forme de chaîne de caractères de la matrice, avec chaque ligne représentée sur une nouvelle ligne.
	 * @return Une chaîne de caractères représentant la matrice.
	 */
	public String toString(){
		int ligne = this.nbLigne();
		int colonne = this.nbColonne();
		String matr = "";
		for(int i = 0; i<ligne;i++){
			for(int j =0; j< colonne;j++){
				if(j == 0)
				{
					matr += this.getCoef(i, j);
				}
				else{
					matr += " " + this.getCoef(i, j);
				}
			}
			matr += "\n";
		}
		return matr;
	}

	/**
	 * Multiplie chaque coefficient de la matrice par un scalaire donné.
	 * @param scalaire Le facteur par lequel chaque coefficient de la matrice sera multiplié.
	 * @return La matrice après multiplication par le scalaire.
	 */
	public Matrice produit(double scalaire){
		int ligne = this.nbLigne();
		int colonne = this.nbColonne();
		for(int i = 0; i < ligne;i++)
			for(int j = 0; j < colonne; j++)
				this.coefficient[i][j] *= scalaire;
		return this;
	}

	/**
	 * Effectue l'addition de deux matrices et retourne le résultat.
	 * @param a La première matrice à additionner.
	 * @param b La deuxième matrice à additionner.
	 * @return La matrice résultant de l'addition des deux matrices.
	 */
	static Matrice addition(Matrice a, Matrice b){
		int ligne = a.nbLigne();
		int colonne = a.nbColonne();
		Matrice mat = new Matrice(ligne, colonne);
		for(int i=0; i<ligne;i++)
			for(int j=0; j< colonne; j++)
				mat.coefficient[i][j]=a.coefficient[i][j] + b.coefficient[i][j];
		return mat;
	}

	/**
	 * Vérifie si deux matrices peuvent être additionnées (elles doivent avoir les mêmes dimensions), et retourne la matrice résultante de l'addition si les dimensions sont valides.
	 * @param a La première matrice à additionner.
	 * @param b La deuxième matrice à additionner.
	 * @return La matrice résultant de l'addition des deux matrices si elles ont les mêmes dimensions.
	 * @throws Exception Si les matrices n'ont pas les mêmes dimensions, une exception est lancée.
	 */
	static Matrice verif_addition(Matrice a, Matrice b) throws Exception{
		if((a.nbLigne() == b.nbLigne()) && (a.nbColonne() == b.nbColonne()))
		{
			int ligne = a.nbLigne();
			int colonne = a.nbColonne();
			Matrice mat = new Matrice(ligne, colonne);
			for(int i=0; i<ligne;i++)
				for(int j=0; j< colonne; j++)
					mat.coefficient[i][j]=a.coefficient[i][j] + b.coefficient[i][j];
			return mat;
		}
		else {
			throw new Exception("Les deux matrices n'ont pas les mêmes dimensions !!!"); 
		}
	}

	/**
	 * Effectue le produit de deux matrices et retourne le résultat.
	 * @param a La première matrice à multiplier.
	 * @param b La deuxième matrice à multiplier.
	 * @return La matrice résultant du produit des deux matrices.
	 */
	static Matrice produit(Matrice a, Matrice b){
		int ligne, colonne;
		ligne = a.nbLigne();
		colonne = b.nbColonne();		
		Matrice mat = new Matrice(ligne, colonne);
		for(int i=0; i<ligne;i++)
			for(int j=0; j< colonne; j++)
			{
				mat.coefficient[i][j]=0;
				for(int k=0; k <a.nbColonne();k++)
					mat.coefficient[i][j] += a.coefficient[i][k] * b.coefficient[k][j];
			}
		return mat;					
	}

	/**
	 * Vérifie si deux matrices peuvent être multipliées (le nombre de colonnes de la première matrice 
	 * doit être égal au nombre de lignes de la deuxième matrice), et retourne la matrice résultante du produit
	 * si les dimensions sont valides.
	 * @param a La première matrice à multiplier.
	 * @param b La deuxième matrice à multiplier.
	 * @return La matrice résultant du produit des deux matrices si les dimensions sont valides.
	 * @throws Exception Si les dimensions des matrices ne permettent pas de les multiplier, une exception est lancée.
	 */
	static Matrice verif_produit(Matrice a, Matrice b) throws Exception{
		int ligne = 0;
		int colonne = 0;
		if(a.nbColonne()==b.nbLigne())
		{
			ligne = a.nbLigne();
			colonne = b.nbColonne();
		}
		else{
			throw new Exception("Dimensions des matrices à multiplier incorrectes");
		}

		Matrice mat = new Matrice(ligne, colonne);
		for(int i=0; i<ligne;i++)
			for(int j=0; j< colonne; j++)
			{
				mat.coefficient[i][j]=0;
				for(int k=0; k <a.nbColonne(); k++)
					mat.coefficient[i][j] += a.coefficient[i][k] * b.coefficient[k][j];
			}
		return mat;					
	}

    public Matrice inverse() throws Exception {
        if (this.nbLigne() != this.nbColonne()) {
            throw new IllegalArgumentException("La matrice n'est pas carrée");
        } else {
            int n = this.nbLigne();
            Vecteur b[] = new Vecteur[n];

            Matrice mat = new Matrice(n, n);
            mat.recopie(this);
            Matrice inverse = new Matrice(n, n);

            for (int nbligne = 0; nbligne < n; nbligne++) {
                Vecteur x = new Vecteur(n);
                x.remplaceCoef(nbligne, 1);
                b[nbligne] = x;
            }

            Helder h = new Helder(mat, b[0]);
            h.factorLDR();
            for (int i = 0; i < n; i++) {

                h.setSecondMembre(b[i]);
                Vecteur x = h.resolutionPartielle();
                for(int j = 0; j < n; j++) {
                    inverse.coefficient[j][i] = x.getCoef(j);
                }
            }

            return inverse;
        }
    }

	/**
	 * @param matrice
	 * @return
	 * @throws Exception
	 */
	public static Matrice inverse(Matrice matrice) throws Exception {
		int n = matrice.nbLigne();

		// Vérifier que la matrice est carrée
		if (n != matrice.nbColonne()) {
			throw new IllegalOperationException("La matrice n'est pas carrée !");
		}

		// Étape 1 : Appliquer la factorisation LDR
		Helder decomposition = new Helder(matrice, new Vecteur(n));
		decomposition.factorLDR();

		// Étape 2 : Initialisation des vecteurs pour l'identité et l'inverse
		Vecteur[] tabVecteurIdentite = new Vecteur[n];
		Vecteur[] tabVecteurResultatInverse = new Vecteur[n];

		// Remplissage des colonnes de la matrice identité
		for (int j = 0; j < n; j++) {
			tabVecteurIdentite[j] = new Vecteur(n);
			for (int i = 0; i < n; i++) {
				tabVecteurIdentite[j].remplaceCoef(i, (i == j) ? 1.0 : 0.0);
			}
		}

		// Étape 3 : Résolution des systèmes Ax = Ei
		for (int i = 0; i < n; i++) {
			// Remplace le second membre par Ei
			decomposition.setSecondMembre(tabVecteurIdentite[i]);

			// Résolution du système avec la factorisation LDR
			tabVecteurResultatInverse[i] = decomposition.resolutionPartielle();
		}

		// Étape 4 : Construire la matrice inverse à partir des vecteurs résultats
		double[][] tabmatriceInverse = new double[n][n];
		for (int j = 0; j < n; j++) {
			for (int i = 0; i < n; i++) {
				tabmatriceInverse[i][j] = tabVecteurResultatInverse[j].getCoef(i);
			}
		}

		// Retourner la matrice inverse
		return new Matrice(tabmatriceInverse);
	}
	
	
	/**
	 * Retourne la transposée de la matrice actuelle.
	 * La transposée d'une matrice est obtenue en échangeant les lignes et les colonnes.
	 * 
	 * @return La matrice transposée.
	 */
	public Matrice transposee() {
	    int lignes = this.nbLigne();
	    int colonnes = this.nbColonne();
	    Matrice transposée = new Matrice(colonnes, lignes);

	    for (int i = 0; i < lignes; i++) {
	        for (int j = 0; j < colonnes; j++) {
	            transposée.remplaceCoef(j, i, this.getCoef(i, j));
	        }
	    }

	    return transposée;
	}


	/**
	 * Méthode pour calculer la norme matricielle L1 de la différence entre deux matrices
	 * @return
	 */
	public double norme_1() {
		double norme = 0.0;

		for (int j = 0; j < nbColonne(); j++) { // On itère sur les colonnes
			double somme = 0.0;
			for (int i = 0; i < nbLigne(); i++) {
				somme += Math.abs(getCoef(i, j));
			}
			norme = Math.max(norme, somme); // On prend le maximum des sommes de colonnes
		}
		return norme;
	}


	/**
	 * Méthode pour calculer la norme matricielle L_infini de la différence entre deux matrices
	 * @return
	 */
	public double norme_inf() {
		double norme = 0.0;

		for (int i = 0; i < nbLigne(); i++) { // On itère sur les lignes
			double somme = 0.0;
			for (int j = 0; j < nbColonne(); j++) {
				somme += Math.abs(getCoef(i, j));
			}
			norme = Math.max(norme, somme); // On prend le maximum des sommes de lignes
		}
		return norme;
	}


	/**
	 * conditionnement en utilisant la norme L1
	 * @return
	 * @throws IrregularSysLinException
	 */
	public double cond_1() throws IrregularSysLinException {
		try {
			return norme_1() * inverse(this).norme_1();
		} catch (Exception e) {
			throw new IrregularSysLinException("La matrice n'est pas inversible !");
		}
	}

	/**
	 * conditionnement en utilisant la norme L_infini
	 * @return
	 * @throws IrregularSysLinException
	 */
	public double cond_inf() throws IrregularSysLinException {
		try {
			return norme_inf() * inverse(this).norme_inf();
		} catch (Exception e) {
			throw new IrregularSysLinException("La matrice n'est pas inversible !");
		}
	}

	/**
	 * Méthode principale de l'application, qui va permettre de tester toutes les méthodes de la classe.
	 * @param args Les arguments de la ligne de commande (non utilisés dans cette méthode).
	 * @throws Exception Si une erreur se produit lors de l'exécution des opérations sur les matrices, une exception peut être lancée.
	 */
	public static void main(String[] args) throws Exception {
		/*
		double mat[][]= {{2,1},{0,1}};
		Matrice a = new Matrice(mat);
		System.out.println("construction d'une matrice par affectation d'un tableau :\n"+a);

		Matrice b = new Matrice("/home/syrder13/Desktop/SEMESTRE VI (2)/Programmation Scientifique/Calcul_Matriciel/src/AlgLin/matrice1.txt");
		System.out.println("Construction d'une matrice par lecture d'un fichier :\n"+b);

		Matrice c = new Matrice(3,3);
		c.recopie(b);
		System.out.println("Recopie de la matrice b :\n"+c);

		System.out.println("Nombre de lignes et colonnes de la matrice c : "+c.nbLigne()+
				", "+c.nbColonne());
		System.out.println("Coefficient (2,2) de la matrice b : "+b.getCoef(1, 1));
		System.out.println("Nouvelle valeur de ce coefficient : 8");
		b.remplaceCoef(1, 1, 8);
		System.out.println("Vérification de la modification du coefficient");
		System.out.println("Coefficient (2,2) de la matrice b : "+b.getCoef(1, 1) + "\n");

		System.out.println("Addition de 2 matrices : affichage des 2 matrices "+
				"puis de leur addition");
		System.out.println("matrice 1 :\n"+a+"matrice 2 :\n"+b+"somme :\n"+
				Matrice.addition(a,b));

		System.out.println("Produit de 2 matrices : affichage des 2 matrices "+
				"puis de leur produit");
		System.out.println("matrice 1 :\n"+a+"matrice 2 :\n"+b+"produit :\n"+
				produit(a,b));
		*/

	    Random rand = new Random();
		int N = 3; // Taille des matrices générées (modifiable)
	    int nombreDeMatrices = 3; // Nombre de matrices à tester
		
	 // Génération et test de matrices aléatoires
	    for (int k = 0; k < nombreDeMatrices; k++) {
	        // Génération d'une matrice aléatoire NxN
	        double[][] mat1 = new double[N][N];
	        for (int i = 0; i < N; i++) {
	            for (int j = 0; j < N; j++) {
	            	mat1[i][j] = rand.nextInt(5) + 1; // Valeurs entre 1 et 5
	            }
	        }
	        
	        Matrice d = new Matrice(mat1);
	        Matrice copieD = new Matrice(N, N);
	        copieD.recopie(d);
	        System.out.println("\n==== Test d'inversion pour Matrice " + (k + 1) + " ====");
	        System.out.println("Matrice d :\n" + d);
	        
			Matrice d_inv = Matrice.inverse(d);
			System.out.println("Matrice inverse de d :\n" + d_inv);
	
			// Produit de d et son inverse
			Matrice produitD = Matrice.produit(d, d_inv);
			System.out.println("Produit de d et son inverse :\n" + produitD);
	
			// Création de la matrice identité de même taille
			Matrice identiteD = new Matrice(N, N);
			for (int i = 0; i < N; i++) {
				identiteD.remplaceCoef(i, i, 1.0);
			}
	
			// Calcul de la norme de la différence entre (d * d_inv) et l'identité
			Matrice diff = Matrice.addition(produitD, identiteD.produit(-1)); // (d * d_inv - I)
			System.out.println(diff);
			System.out.println("Norme 1 de la différence : " + diff.norme_1());
			System.out.println("Norme inf de la différence : " + diff.norme_inf());
	
			// Affichage des conditionnements
			System.out.println("Conditionnement en norme 1 : " + d.cond_1());
			System.out.println("Conditionnement en norme inf : " + d.cond_inf());
	    }
	}
}
