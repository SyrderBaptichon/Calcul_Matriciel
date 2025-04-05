package AlgLin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.IntStream;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;

/**
 * Classe pour l'interpolation par splines cubiques.
 * Cette classe permet de calculer une spline cubique à partir de points de support
 * et d'évaluer la spline en un point donné.
 * 
 * @author Syrder Baptichon
 * @version 1.0
 */
public class Spline {

	static double[] abscisses; // Tableau des abscisses des points de support
    static double[] ordonnees; // Tableau des ordonnées des points de support
    private Vecteur deriveeSeconde; // Vecteur représentant la dérivée seconde de la fonction d'interpolation

    /**
     * Constructeur de la classe Spline.
     * @param abscisses Tableau des abscisses des points de support.
     * @param ordonnees Tableau des ordonnées des points de support.
     * @throws DataOutOfRangeException Si les tableaux ne sont pas valides.
     */
    public Spline(double[] abscisses, double[] ordonnees) throws Exception {
        this.abscisses = abscisses;
        this.ordonnees = ordonnees;
        
        // Vérification de la validité des données d'entrée
        verifierDonnees();
        
        this.deriveeSeconde = calculDeriveeSeconde();
    }
    
    /**
     * Constructeur de la classe Spline.
     * Lit un fichier contenant des points (x, y) et initialise les tableaux d'abscisses et d'ordonnées.
     *      *
     * @param fichier Chemin du fichier contenant les points de support.
     * @throws Exception Si le fichier est introuvable ou si les données ne sont pas valides.
     */
    public Spline(String fichier) throws Exception {
        // Initialisation des listes pour stocker les données
        ArrayList<Double> abs = new ArrayList<>();
        ArrayList<Double> ord = new ArrayList<>();

        // Lecture du fichier et extraction des données
        try {
            Scanner fileScanner = new Scanner(new File(fichier));
            
            while (fileScanner.hasNextLine()) {
                String ligne = fileScanner.nextLine().trim(); // Suppression des espaces inutiles
                
                // Vérifie que la ligne n'est pas vide et ne commence pas par "#" (commentaire)
                if (!ligne.isEmpty() && !ligne.startsWith("#")) { 
                    String[] values = ligne.split("\\s+"); // Séparation des valeurs par espaces
                    
                    // Vérification que la ligne contient exactement deux valeurs
                    if (values.length != 2) {
                        throw new IllegalArgumentException("Format invalide : chaque ligne doit contenir exactement deux nombres.");
                    }
                    
                    // Conversion et ajout des valeurs aux listes
                    abs.add(Double.parseDouble(values[0])); // Ajout de l'abscisse
                    ord.add(Double.parseDouble(values[1])); // Ajout de l'ordonnée
                }
            }
            
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Le fichier : '" + fichier + "' est introuvable.");
        }

        // Conversion de la liste d'objets Double (ArrayList<Double>) en un tableau de doubles (double[])
        this.abscisses  = abs.stream().mapToDouble(Double::doubleValue).toArray();
        this.ordonnees = ord.stream().mapToDouble(Double::doubleValue).toArray();
        
        // Vérification de la validité des données
        verifierDonnees();

        deriveeSeconde = calculDeriveeSeconde();
    }
    
    /** 
     * Vérifie que les données respectent les contraintes de la spline. 
     * 
     * @throws IllegalArgumentException Si les données ne sont pas valides.
     */
    private void verifierDonnees() throws DataOutOfRangeException {
        // Vérifie que le nombre d'abscisses et d'ordonnées est identique
        if (abscisses.length != ordonnees.length) {
            throw new IllegalArgumentException("Les tableaux abscisses et ordonnées doivent avoir la même longueur.");
        }
        // Vérifie qu'il y a au moins deux points pour définir une spline
        if (abscisses.length < 2) {
            throw new IllegalArgumentException("Il faut au moins deux points de support pour créer une spline.");
        }
        // Vérifie que les abscisses sont strictement croissantes
        for (int i = 1; i < abscisses.length; i++) {
            if (abscisses[i] <= abscisses[i - 1]) {
                throw new IllegalArgumentException("Les abscisses doivent être strictement croissantes.");
            }
        }
    }

    /**
     * Calcule les dérivées secondes de la fonction d'interpolation aux points de support.
     *
     * @return Un vecteur contenant les dérivées secondes aux points de support.
     * @throws Exception Si une erreur survient lors de la résolution du système tridiagonal.
     */
    private Vecteur calculDeriveeSeconde() throws Exception {
        int n = abscisses.length; // Nombres de points de support

        // Initialisation des tableaux pour les coefficients de la matrice tridiagonale
        double[] a = new double[n]; // Sous-diagonale
        double[] b = new double[n]; // Diagonale principale
        double[] c = new double[n]; // Sur-diagonale
        double[] d = new double[n]; // Vecteur second membre

        // Initialisation des dérivées secondes aux extrémités
        b[0] = 2 * (abscisses[2] - abscisses[0]); // b_1 = 2 ∗ (x_2 − x_0)
        c[0] = abscisses[2] - abscisses[1]; // c_1 = x_2 − x_1
        d[0] = 6 * ((ordonnees[2] - ordonnees[1]) / (abscisses[2] - abscisses[1]) - (ordonnees[1] - ordonnees[0]) / (abscisses[1] - abscisses[0]));  

        // Calcul des coefficients pour j = 2 à n-2
        for (int j = 1; j < n - 1; j++) {
            a[j] = c[j - 1]; // Sous-diagonale | symétrie du système
            b[j] = 2 * (abscisses[j + 1] - abscisses[j - 1]); // Diagonale principale
            c[j] = abscisses[j + 1] - abscisses[j]; // Sur-diagonale
            d[j] = 6 * ((ordonnees[j + 1] - ordonnees[j]) / (c[j]) - (ordonnees[j] - ordonnees[j - 1]) / (a[j])); // Second membre
        }
        
        // Calcul des coefficients pour j = n-1
        a[n - 1] = c[n - 2]; // a_{n-1} = c_{n-2}
        b[n - 1] = 2 * (abscisses[n - 1] - abscisses[n - 3]); // b_{n-1} = 2 * (x_n - x_{n-2})
        d[n - 1] = 6 * ((ordonnees[n - 1] - ordonnees[n - 2]) / (abscisses[n - 1] - abscisses[n - 2]) - (ordonnees[n - 2] - ordonnees[n - 3]) / (a[n - 1])); // d_{n-1}

        // Construction de la matrice tridiagonale
        double[][] matrice = new double[3][n];
        matrice[0] = a;
        matrice[1] = b;
        matrice[2] = c;
        Mat3Diag matriceTridiagonale = new Mat3Diag(matrice);

        // Construction du vecteur second membre
        Vecteur secondMembre = new Vecteur(d);

        // Résolution du système tridiagonal avec la méthode de Thomas
        Thomas thomas = new Thomas(matriceTridiagonale, secondMembre);
        
        return thomas.resolution();  
    }

    /**
     * Évalue la spline cubique en un point donné.
     *
     * @param x Le point où évaluer la spline.
     * @return La valeur interpolée de la spline en x.
     * @throws DataOutOfRangeException Si x est en dehors de l'intervalle des abscisses des points de support.
     */
    public double evaluation(double x) throws DataOutOfRangeException {
        // Vérification si x est dans l'intervalle valide
        if (x < abscisses[0] || x > abscisses[abscisses.length - 1]) {
            throw new DataOutOfRangeException(
                "La valeur fournie (" + x + ") est en dehors de l'intervalle [" 
                + abscisses[0] + ", " + abscisses[abscisses.length - 1] + "]."
            );
        }

        // Recherche de l'intervalle [x_j ; x_{j+1}] contenant x
        int j = intervalle(x);

        // Calcul des variables intermédiaires
        double g_j = deriveeSeconde.getCoef(j);
        double g_j1 = deriveeSeconde.getCoef(j + 1);
        
        double alpha = abscisses[j + 1] - x; // α = x_{j+1} − x 
        double beta = x - abscisses[j]; // β = x − x_j
        double gamma = abscisses[j + 1] - abscisses[j]; // γ = x_{j+1} − x_j

        // Application de la formule de l'algorithme
        double t1 = (g_j / 6.0) * ((alpha * alpha * alpha / gamma) - (alpha * gamma));
        double t2 = (g_j1 / 6.0) * ((beta * beta * beta / gamma) - (beta * gamma));
        double t3 = (ordonnees[j] * alpha / gamma) + (ordonnees[j + 1] * beta / gamma);

        return (t1 + t2 + t3);
    }

    /**
     * Trouve l'intervalle [x_j ; x_{j+1}] contenant la valeur donnée.
     *
     * @param val La valeur à rechercher.
     * @return L'indice j tel que x_j <= val <= x_{j+1}.
     */
    private int intervalle(double val) {
        // Initialisation des indices de recherche
        int low = 0;
        int high = abscisses.length - 1;
        
        // Boucle de recherche binaire
        while (low <= high) {
            // Calcul de l'indice du milieu
            int mid = low + (high - low) / 2;
            
            // Vérifie si la valeur est dans l'intervalle [x_mid, x_mid+1]
            if (abscisses[mid] <= val && val <= abscisses[mid + 1]) {
                return mid;
            }
            // Si la valeur est supérieure à x_mid, on cherche dans la moitié droite
            else if (abscisses[mid] < val) {
                low = mid + 1;
            } 
            // Si la valeur est inférieure à x_mid, on cherche dans la moitié gauche
            else {
                high = mid - 1;
            }
        }
        
        // Si la valeur n'est pas trouvée (ne devrait jamais arriver si val est dans l'intervalle)
        return -1; 
    }
    
    
    /**
     * Méthode principale qui :
     * - Lit les points depuis un fichier dont le nom est donné par l'utilisateur.
     * - Calcule la fonction d'interpolation par spline cubiques.
     * - Affiche les points et la courbe.
     */
    public static void main(String[] args) throws Exception {
        // Création de l'objet Spline
		//String chemin = "/home/syrder13/Desktop/SEMESTRE VI (2)/Programmation Scientifique/Calcul_Matriciel/src/AlgLin/dataSpline.txt";
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom du fichier des points : ");
        String chemin = sc.nextLine();
        Spline spline = new Spline(chemin);
                
        // Création de la série de points de support
        XYSeries seriesPoints = new XYSeries("Points de support");
        for (int i = 0; i < spline.abscisses.length; i++) {
            seriesPoints.add(spline.abscisses[i], spline.ordonnees[i]); // Ajout des points (xi, yi)
        }

        // Séries de la courbe interpolée (100 points)
        XYSeries seriesSpline = new XYSeries("Spline Cubique");
        double min = spline.abscisses[0]; // Première abscisse
        double max = spline.abscisses[spline.abscisses.length - 1]; // Dernière abscisse
        double step = (max - min) / 100.0; // Espacement régulier des points d'évaluation

        // Calcul et ajout des points interpolés
        for (double x = min; x < max; x += step) {
            seriesSpline.add(x, spline.evaluation(x));
        }
        
        // Création du dataset contenant les deux séries
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesPoints); // Ajout des points de support
        dataset.addSeries(seriesSpline); // Ajout des points de la courbe interpolée

        // Création du graphique
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Interpolation par Spline Cubique",
                "x", "y",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Personnalisation de l'affichage
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false); // Afficher uniquement les symboles pour les points de support
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true); // Tracer la ligne pour la spline
        renderer.setSeriesShapesVisible(1, false);
        chart.getXYPlot().setRenderer(renderer);

        // Affichage dans une fenêtre Swing
        ChartFrame frame = new ChartFrame("Graphique", chart);
        frame.setPreferredSize(new java.awt.Dimension(800, 600));
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}