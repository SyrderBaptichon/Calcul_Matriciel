/**
 * La classe Vecteur représente un vecteur, qui est une matrice avec une seule colonne.
 * @author bs214279
 * @version 1.1
 */

package AlgLin;

import java.util.* ;
import java.io.* ;

public class Vecteur extends Matrice{
	/** Définir ici les constructeurs **/
	
	 /**
     * Constructeur pour créer un vecteur avec une taille donnée.
     * Ce constructeur crée un vecteur avec la taille spécifiée, initialisé à des coefficients nuls.
     * @param taille La taille du vecteur (nombre de lignes).
     */
	public Vecteur(int taille) {
		super(taille, 1);
	}

	/**
     * Constructeur pour créer un vecteur à partir d'un tableau de valeurs.
     * @param tableau Le tableau de valeurs à utiliser pour initialiser le vecteur.
     */
	public Vecteur(double[] tableau) {
		super(new double[tableau.length][1]);
		for(int i = 0; i < tableau.length; i++) {
			remplaceCoef(i, 0, tableau[i]);
		}
	}
	
	 /**
     * Constructeur pour créer un vecteur à partir d'un fichier contenant les valeurs.
     * Ce constructeur lit les valeurs à partir d'un fichier, où la première valeur représente la taille du vecteur
     * et les valeurs suivantes représentent les coefficients du vecteur.
     * @param fichier Le nom du fichier contenant les données du vecteur.
     */
	public Vecteur(String fichier) {
		super(1, 1);
		try {
			Scanner sc = new Scanner(new File(fichier));
			int taille = sc.nextInt();
			this.coefficient = new double[taille][1];
			for(int i=0; i<taille;i++)
				this.coefficient[i][0]=sc.nextDouble();
			sc.close();	
		}
		catch(FileNotFoundException e) {
			System.out.println("Fichier absent");
		}
	}
	
	/** Définir ici les autres méthodes */
	
	/** 
     * Retourne la taille du vecteur.
     * @return La taille du vecteur, c'est-à-dire le nombre de lignes.
     */
	public int getTaille() {
		return nbLigne();
	}
	
	/** 
     * Retourne le coefficient du vecteur à la position donnée.
     * @param position La position du coefficient dans le vecteur (l'indice de la ligne).
     * @return Le coefficient à la position spécifiée.
     */
	public double getCoef(int position) {
		return getCoef(position, 0);
	}
	
	/** 
     * Remplace le coefficient à la position spécifiée par une nouvelle valeur.
     * @param position La position du coefficient à remplacer.
     * @param value La nouvelle valeur à attribuer au coefficient.
     */
	public void remplaceCoef(int position, double value){
		remplaceCoef(position, 0, value);
	}
	
	/** 
     * Retourne une représentation sous forme de chaîne de caractères du vecteur.
     * Cette méthode crée une chaîne qui contient les valeurs du vecteur, une par ligne.
     * @return Une chaîne de caractères représentant le vecteur.
     */
	public String toString() {
		String matr = "";
		for(int i = 0; i < getTaille();i++){
			matr += this.getCoef(i);
			matr += "\n";
		}
		return matr;
	}
	
	/**
	 * Calcule le produit scalaire de deux vecteurs.
	 * @param a  le premier vecteur
	 * @param b  le second vecteur
	 * @return le produit scalaire des deux vecteurs
	 * @see Matrice
	 */
	static double produit(Vecteur a, Vecteur b) {
        double produitScalaire = 0.0;
        for (int i = 0; i < a.getTaille(); i++) {
            produitScalaire += a.getCoef(i) * b.getCoef(i);
        }
        return produitScalaire;
	}
	
	
	/**
	 * Calcule le produit d'un vecteur par une matrice.
	 * Le vecteur doit avoir la même taille que le nombre de colonnes de la matrice.
	 * @param vecteur Le vecteur à multiplier par la matrice.
	 * @param matrice La matrice à multiplier par le vecteur.
	 * @return Un nouveau vecteur résultant du produit du vecteur par la matrice.
	 * @throws IllegalArgumentException Si la taille du vecteur ne correspond pas au nombre de colonnes de la matrice.
	 */
	public static Vecteur produitMatrice(Vecteur vecteur, Matrice matrice) {
	    // Vérifie que la taille du vecteur correspond au nombre de colonnes de la matrice
	    if (vecteur.getTaille() != matrice.nbColonne()) {
	        throw new IllegalArgumentException("La taille du vecteur doit correspondre au nombre de colonnes de la matrice.");
	    }

	    // Crée un nouveau vecteur pour stocker le résultat
	    Vecteur resultat = new Vecteur(matrice.nbLigne());

	    // Effectue le produit du vecteur par la matrice
	    for (int i = 0; i < matrice.nbLigne(); i++) {
	        double somme = 0.0;
	        for (int j = 0; j < matrice.nbColonne(); j++) {
	            somme += vecteur.getCoef(j) * matrice.getCoef(i, j);
	        }
	        resultat.remplaceCoef(i, somme);
	    }

	    return resultat;
	}
	

	/**
	 * Calcule le produit scalaire de deux vecteurs.
	 * @param a  le premier vecteur
	 * @param b  le second vecteur
	 * @return le produit scalaire des deux vecteurs
	 * @throws Exception si les vecteurs n'ont pas la même taille
	 * @see Matrice
	 */
	static double verif_produit(Vecteur a, Vecteur b) throws Exception {
		double produitScalaire = 0.0;
		// Vérifier que les deux vecteurs ont la même dimension
        if (a.getTaille() != b.getTaille()) {
            throw new IllegalArgumentException("Les vecteurs n'ont pas la même dimension");
        }
        
        for (int i = 0; i < a.getTaille(); i++) {
            produitScalaire += a.getCoef(i) * b.getCoef(i);
        }
        return produitScalaire;
	}
	
	public double normeL1(){
		double res = 0.0;
		for(int i = 0; i < getTaille(); i++){
			res += Math.abs(getCoef(i));
		}
		return res;
	}
	
	/**
	 * Soustrait deux vecteurs de même taille.
	 * @param a Le premier vecteur.
	 * @param b Le second vecteur.
	 * @return Un nouveau vecteur résultant de la soustraction de a et b.
	 * @throws IllegalArgumentException Si les vecteurs n'ont pas la même taille.
	 */
	public static Vecteur soustraction(Vecteur a, Vecteur b) {
	    // Vérifie que les deux vecteurs ont la même taille
	    if (a.getTaille() != b.getTaille()) {
	        throw new IllegalArgumentException("Les vecteurs doivent avoir la même taille.");
	    }

	    // Crée un nouveau vecteur pour stocker le résultat
	    Vecteur resultat = new Vecteur(a.getTaille());

	    // Soustrait les coefficients des deux vecteurs
	    for (int i = 0; i < a.getTaille(); i++) {
	        resultat.remplaceCoef(i, a.getCoef(i) - b.getCoef(i));
	    }

	    return resultat;
	}

	public double normeL2(){
		double res = 0.0;
		for(int i = 0; i < getTaille(); i++){
			res += Math.pow(Math.abs(getCoef(i)), 2);
		}
        return Math.sqrt(res);
	}

	public double normeInfini(){
		double res = 0.0;
		for(int i = 0; i < getTaille(); i++) {
			res = Math.max(res, Math.abs(getCoef(i)));
		}
		return res;
	}
	
	/**
	 * Méthode principale de l'application, qui va permettre de tester toutes les méthodes de la classe.
	 * 
	 * @param args Les arguments de la ligne de commande (non utilisés dans cette méthode).
	 */
	public static void main(String[] args) {
		double vect[] = {1, 4, 9};
		Vecteur a = new Vecteur(vect);
		
		System.out.println("construction d'un Vecteur par affectation d'un tableau :\n"+a);
		Vecteur b = new Vecteur("/home/syrder13/Desktop/SEMESTRE VI (2)/Programmation Scientifique/Calcul_Matriciel/src/AlgLin/vecteur1.txt");
		System.out.println("Construction du Vecteur b par lecture d'un fichier :\n"+b);
		
		Vecteur c = new Vecteur(4);
		c.recopie(b);
		System.out.println("Recopie du vecteur b :\n"+c);
		
		System.out.println("Taille du vecteur c : "+c.nbLigne());
		System.out.println("Coefficient (2) du Vecteur b : "+b.getCoef(1));
		System.out.println("Nouvelle valeur de ce coefficient : 8");
		b.remplaceCoef(1, 8);
		
		System.out.println("Vérification de la modification du coefficient (2) du Vecteur b : "+b.getCoef(1) + "\n");
		System.out.println("Produit de 2 Vecteurs : affichage des 2 Vecteurs puis de leur produit");
		System.out.println("Vecteur 1 :\n"+a+"Vecteur 2 :\n"+b+"produit : "+
				produit(a,b) + "\n");
		
	}
}
