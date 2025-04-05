package AlgLin;

/**
 * 
 */
public class HilbertMatrice extends Matrice {

	/**
	 * @param ordre
	 */
	public HilbertMatrice(int ordre) {
		super(ordre, ordre);
		if (ordre <= 0) {
			throw new IllegalArgumentException("L'ordre de la matrice de Hilbert doit être supérieur à zéro.");
		}
		for (int i = 0; i < ordre; i++) {
			for (int j = 0; j < ordre; j++) {
				coefficient[i][j] = 1.0 / (i + j + 1);
			}
		}
	}
	
	public static void main(String[] args) throws IrregularSysLinException {
		try {

			for (int ordre = 3; ordre <= 15; ordre++) {
				HilbertMatrice hilbertMatrice = new HilbertMatrice(ordre);
				System.out.println("Matrice de Hilbert d'ordre " + ordre + " :");
				System.out.println(hilbertMatrice);
	
				System.out.println("Inverse de la matrice de Hilbert d'ordre " + ordre + " :");
				Matrice inverseHilbert = inverse(hilbertMatrice);
				System.out.println(inverseHilbert);
				
				// Vérification en multipliant la matrice d'origine par son inverse
				System.out.println("Produit de la matrice de Hilbert d'ordre " + ordre + " par son inverse :");
				System.out.println(produit(hilbertMatrice, inverseHilbert));
	
				// Produit de d et son inverse
				Matrice produitParInverse = Matrice.produit(hilbertMatrice, inverseHilbert);

				// Création de la matrice identité de même taille
				Matrice identite = new Matrice(ordre, ordre);
				for (int i = 0; i < ordre; i++) {
					identite.remplaceCoef(i, i, 1.0);
				}

				// Calcul de la norme de la différence entre (d * d_inv) et l'identité
				Matrice diff = Matrice.addition(produitParInverse, identite.produit(-1)); // (d * d_inv - I)
				System.out.println("Norme 1 de la différence : " + diff.norme_1());
				System.out.println("Norme inf de la différence : " + diff.norme_inf());

				// Affichage des conditionnements
				System.out.println("Conditionnement en norme 1 : " + hilbertMatrice.cond_1());
				System.out.println("Conditionnement en norme inf : " + hilbertMatrice.cond_inf() + "\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}