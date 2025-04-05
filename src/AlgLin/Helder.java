package AlgLin;

import java.io.File;

/**
 * La classe {@code Helder} permet de résoudre un système linéaire en utilisant la factorisation LDR (L, D, R).
 * @author Syrder BAPTICHON
 */
public class Helder extends SysLin {
	
	private Matrice L;
    private Matrice D;
    private Matrice R;
    
    /**
     * Constructeur de la classe {@code Helder}.
     * Initialise le système linéaire avec une matrice et un second membre. Le système n'est pas factorisé à ce stade.
     * 
     * @param matriceSystem la matrice du système linéaire.
     * @param secondMembre le vecteur représentant le second membre du système.
     * @throws Exception si une exception liée au système linéaire se produit.
     */
    public Helder(Matrice matriceSystem, Vecteur secondMembre) throws Exception  {
        super(matriceSystem, secondMembre);
        
        this.L = new Matrice(matriceSystem.nbLigne(), matriceSystem.nbColonne());
        this.D = new Matrice(matriceSystem.nbLigne(), matriceSystem.nbColonne());
        this.R = new Matrice(matriceSystem.nbLigne(), matriceSystem.nbColonne());
    }

    /**
     * Factorise la matrice du système en trois matrices L, D et R (LDR).
     * La matrice est factorisée en matrice triangulaire inférieure (L), diagonale (D) et 
     * triangulaire supérieure (R) à diagonale unitaire. Si la matrice est singulière ou presque singulière, une exception est lancée.
     * 
     * @throws IrregularSysLinException si la matrice est singulière ou presque singulière.
     */
    public void factorLDR() throws IrregularSysLinException {
        int n = getOrdre();
        Matrice A = this.matriceSystem;
        double val = 0.0;
        double somme = 0.0;

        for(int i=0; i<getOrdre(); i++) {

            //Calcul de la factorisation pour la matrice L
            //données pour la matrice triangulaire inférieure unitaire
            for(int j=0; j<i; j++) {
                somme = 0.;
                for(int k=0; k<j; k++) {
                    somme += this.L.getCoef(i, k) *
                            this.D.getCoef(k, k) *
                            this.R.getCoef(k, j);
                }

                val = 1/this.D.getCoef(j, j) *
                        ( A.getCoef(i, j) - somme );

                L.remplaceCoef(i, j, val);
            }

            //Calcul de la factorisation pour la matrice D
            //données pour la matrice diagonale
            somme = 0.;
            for(int k=0; k<i; k++) {
                somme += this.L.getCoef(i, k) *
                        this.D.getCoef(k, k) *
                        this.R.getCoef(k, i);
            }
            val = A.getCoef(i, i) - somme;
            this.D.remplaceCoef(i, i, val);
            
          if (Math.abs(D.getCoef(i, i)) < Matrice.EPSILON) {
        	  //throw new IrregularSysLinException("Matrice singulière ou presque singulière");
          }

            //Calcul de la factorisation pour la matrice R
            //données pour la matrice triangulaire supérieure unitaire
            for(int j=i+1; j<getOrdre(); j++) {
                somme = 0.;
                for(int k=0; k<i; k++) {
                    somme += this.L.getCoef(i, k) *
                            this.D.getCoef(k, k) *
                            this.R.getCoef(k, j);
                }

                val = 1/this.D.getCoef(i, i) *
                        ( A.getCoef(i, j) - somme );

                this.R.remplaceCoef(i, j, val);
            }
        }

        for(int i=0; i<getOrdre();i++) {
            this.L.remplaceCoef(i, i, 1);
            this.R.remplaceCoef(i, i, 1);
        }
    }
   

    /**
     * Résout le système linéaire en appliquant la factorisation LDR.
     * Si la matrice n'est pas encore factorisée, la méthode {@link #factorLDR()} est appelée pour effectuer la factorisation.
     * Puis la méthode {@link #resolutionPartielle()} est utilisée pour résoudre le système.
     * 
     * @return le vecteur solution du système.
     * @throws IrregularSysLinException si le système est irrégulier.
     */
    @Override
    public Vecteur resolution() throws IrregularSysLinException {
        this.factorLDR();
        return resolutionPartielle();
    }

    /**
     * Résout le système linéaire en utilisant les matrices L, D et R déjà factorisées.
     * Cette méthode suppose que la matrice du système a déjà été factorisée en L, D et R.
     * 
     * @return le vecteur solution du système.
     * @throws IrregularSysLinException si le système est irrégulier.
     */
    public Vecteur resolutionPartielle() throws IrregularSysLinException {     
        // Résolution Ly = b
        SysTriangInfUnite sysL = new SysTriangInfUnite(L, secondMembre);
        Vecteur y = sysL.resolution();

        // Résolution Dz = y
        SysDiagonal sysD = new SysDiagonal(D, y);
        Vecteur z = sysD.resolution();

        // Résolution Rx = z
        SysTriangSupUnite sysR = new SysTriangSupUnite(R, z);
        return sysR.resolution();
    }
  

    /**
     * Modifie le second membre du système.
     * 
     * @param secondMembre le nouveau vecteur représentant le second membre du système.
     */
    public void setSecondMembre(Vecteur secondMembre) {
        this.secondMembre = secondMembre;
    }
    
    /**
     * Convertit la première colonne d'une matrice en un vecteur.
     * 
     * @param m la matrice à convertir.
     * @return un vecteur contenant la première colonne de la matrice.
     */
    private static Vecteur matriceAVecteur(Matrice m) {
        Vecteur v = new Vecteur(m.nbLigne());
        for (int i = 0; i < m.nbLigne(); i++) {
            v.remplaceCoef(i, m.getCoef(i, 0));
        }
        return v;
    }

    /**
     * Point d'entrée principal pour tester la classe {@code Helder}.
     * Crée des matrices et vecteurs à partir de fichiers, effectue deux résolutions successives et vérifie la solution.
     * 
     * @throws Exception si une exception se produit pendant l'exécution.
     */
    public static void main(String[] args) throws Exception {
    	try {
            // Création des objets Matrice et Vecteur a partir des fichiers "matrice1.txt" et "vecteur1.txt"
            Matrice A = new Matrice("/home/syrder13/Desktop/SEMESTRE VI (2)/Programmation Scientifique/Calcul_Matriciel/src/AlgLin/matrice1.txt");
            Vecteur b = new Vecteur("/home/syrder13/Desktop/SEMESTRE VI (2)/Programmation Scientifique/Calcul_Matriciel/src/AlgLin/vecteur1.txt");

            // Affichage de la matrice A et du vecteur b
            System.out.println("Matrice A :\n" + A);
    		System.out.println("Vecteur b :\n"+b);;

            // Création des copies pour résoudre successivement Ay = b et Ax = y
            Matrice A1 = new Matrice(A.nbLigne(), A.nbColonne());
            A1.recopie(A);
            Matrice A2 = new Matrice(A.nbLigne(), A.nbColonne());
            A2.recopie(A);

            // Résolution du premier système : Ay = b
            Helder system1 = new Helder(A1, b);
            Vecteur y = system1.resolution();
            System.out.println("Solution du premier système (y) :\n" + y);

            // Résolution du deuxième système : Ax = y
            Helder system2 = new Helder(A2, y);
            Vecteur x = system2.resolution();
            System.out.println("Solution du deuxième système (x) :\n" + x);

            // Vérification de la solution en calculant A²x - b
            Matrice A2Matrice = Matrice.produit(A, A);
            Vecteur A2x = matriceAVecteur(Matrice.produit(A2Matrice, x));
            System.out.println("Vérification A²x :\n" + A2x);

            Vecteur residu = new Vecteur(b.getTaille());
            for (int i = 0; i < residu.getTaille(); i++) {
                residu.remplaceCoef(i, A2x.getCoef(i) - b.getCoef(i));
            }

            // Vérification de la norme infinie du résidu
            double norme = residu.normeInfini();
            System.out.println("Norme du résidu : " + norme);

            if (norme < Matrice.EPSILON) {
                System.out.println("Solution validée !");
            } else {
                System.out.println("Erreur : Résidu non nul.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}