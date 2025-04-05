package AlgLin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Classe ModPoly décrivant un modèle linéaire spécifique pour l'ajustement
 * d'un polynôme aux données par la méthode des moindres carrés.
 *
 */
public class ModPoly {
	
    private static double[] coefficients; // Coefficients du polynôme (a_0, a_1, ..., a_m)
    private int m; // Degré du polynôme

    /**
     * Constructeur qui initialise le modèle avec le degré souhaité.
     * 
     * @param m le degré du polynôme.
     */
    public ModPoly(int m) throws FileNotFoundException {
        this.m = m;
        this.coefficients = new double[m + 1]; 
    }
    
    
    /**
     * Lit les points de support à partir d'un fichier texte.
     * Chaque ligne du fichier doit contenir deux nombres : x et y.
     * 
     * @param fichier le chemin du fichier contenant les points.
     * @return un tableau 2D avec deux lignes : la première pour les x, la seconde pour les y.
     * @throws FileNotFoundException si le fichier est introuvable.
     * @throws IllegalArgumentException si les données ne sont pas valides
     */
    public static double[][] lirePoints(String fichier) throws FileNotFoundException {
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(fichier));
        
        while (fileScanner.hasNextLine()) {
            String ligne = fileScanner.nextLine().trim();
            if (!ligne.isEmpty() && !ligne.startsWith("#")) {
                String[] values = ligne.split("\\s+");
                if (values.length != 2) throw new IllegalArgumentException("Format invalide.");
                xList.add(Double.parseDouble(values[0]));
                yList.add(Double.parseDouble(values[1]));
            }
        }
        fileScanner.close();
        double[] x = xList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] y = yList.stream().mapToDouble(Double::doubleValue).toArray();
        
        // Vérifie que le nombre d'abscisses et d'ordonnées est identique
        if (x.length != y.length) {
            throw new IllegalArgumentException("Les tableaux abscisses et ordonnées doivent avoir la même longueur.");
        }
        // Vérifie qu'il y a au moins deux points pour définir la courbe
        if (x.length < 2) {
            throw new IllegalArgumentException("Il faut au moins deux points de support pour tracer une courbe.");
        }
        // Vérifie que les abscisses sont strictement croissantes
        for (int i = 1; i < x.length; i++) {
            if (x[i] <= x[i - 1]) {
                throw new IllegalArgumentException("Les abscisses doivent être strictement croissantes.");
            }
        }
        
        return new double[][] {x, y};
    }


    /**
     * Calcule les coefficients du polynôme d'ajustement par la méthode des moindres carrés 
     * à partir des points de support.
     *
     * @param x tableau des abscisses des points de support.
     * @param y tableau des ordonnées des points de support.
     * @throws Exception en cas d'erreur de résolution du système linéaire.
     */
    public void identifie(double[] x, double[] y) throws Exception {
        int n = x.length; // Nombre de points de support.
        Matrice F = new Matrice(n, m + 1); 
        Vecteur Y = new Vecteur(y);

        // Construction de la matrice F
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= m; j++) {
                F.remplaceCoef(i, j, Math.pow(x[i], j)); 
            }
        }

        // Résolution du système linéaire F^t * F * coefficients = F^t * Y 
        // Dans le cours : (FtF)X = FtY. La solution X correspond aux (m + 1) coefficients a_i cherchés
        Matrice Ft = F.transposee();
        Matrice FtF = Matrice.produit(Ft, F);
        Vecteur Ftb = Vecteur.produitMatrice(Y, Ft);

        // Utilisation de la décomposition LDR pour résolution de système linéaire
        Helder sysLin = new Helder(FtF, Ftb);

        // Résolution du système linéaire
        Vecteur X = sysLin.resolution();
        
        // Stocke les coefficients obtenus dans l’attribut "coefficients"
        for (int i = 0; i <= m; i++) {
            this.coefficients[i] = X.getCoef(i);
        }
    }
    

    /**
     * Évalue le polynôme ajusté en un point donné.
     * 
     * @param x le point en abscisse.
     * @return la valeur du polynôme en x.
     */
    public double evaluation(double x) {
        double result = 0;
        for (int i = 0; i <= m; i++) {
            result += coefficients[i] * Math.pow(x, i);
        }
        return result;
    }


    /**
     * Méthode principale qui :
     * - Lit les points depuis un fichier dont le nom est donné par l'utilisateur.
     * - Demande à l'utilisateur le degré du polynôme.
     * - Calcule l'ajustement.
     * - Affiche les points et la courbe du polynôme ajusté.
     */
    public static void main(String[] args) {
    	try {
    		// récupération des points de support depuis un fichier dont le nom est donné par l'utilisateur
    		Scanner sc = new Scanner(System.in);
            System.out.print("Nom du fichier des points : ");
            String chemin = sc.nextLine();
            //String chemin = "/home/syrder13/Desktop/SEMESTRE VI (2)/Programmation Scientifique/Calcul_Matriciel/src/AlgLin/points.txt"; // Nom du fichier contenant les points de support
           
            // Choix du degré du polynôme par l'utilisateur
            System.out.print("Degré du polynôme : ");
            int m = sc.nextInt();
            //int m = 1; // Degré du polynôme

            // Lecture des points de support depuis le fichier spécifié
            double[][] points = lirePoints(chemin);
            double[] x = points[0];
            double[] y = points[1];

            // Création d'une instance de ModPoly, puis calcul des coefficients du polynôme par moindres carrés
            ModPoly modPoly = new ModPoly(m);
            modPoly.identifie(x, y);
            
            // Création de la série de points de support pour l'affichage graphique
            XYSeries seriesPoints = new XYSeries("Points de support");
            for (int i = 0; i < x.length; i++) {
                seriesPoints.add(x[i], y[i]); // Ajout des points (xi, yi)
            }
            
            // Création de la série du polynôme
            XYSeries seriesPoly = new XYSeries("Polynôme");
            // Définition de l'intervalle d'évaluation du polynôme
            double minX = x[0]; // Première abscisse
            double maxX = x[x.length - 1]; // Dernière abscisse
            double saut = (maxX - minX) / 100.0; // Espacement régulier des points d'évaluation
            
            // Calcul et ajout des points
            for (double xi = minX; xi <= maxX; xi += saut) {
                seriesPoly.add(xi, modPoly.evaluation(xi)); // Évaluation du polynôme ajusté en plusieurs points entre minX et maxX
            }

            // Création du dataset combinant les deux séries
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(seriesPoints);
            dataset.addSeries(seriesPoly);

            // Génération du graphique
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Approximation par moindres carrés", // Titre
                    "X", // Axe X
                    "Y", // Axe Y
                    dataset, // Données
                    PlotOrientation.VERTICAL,
                    true, // Inclure une légende
                    true,
                    false
            );
            
            // Personnalisation de l'affichage
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesLinesVisible(0, false); // Afficher uniquement les symboles pour les points de support
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesLinesVisible(1, true); // Affichage de la courbe du polynome
            renderer.setSeriesShapesVisible(1, false);
            chart.getXYPlot().setRenderer(renderer);

            // Affichage du graphique
            ChartFrame frame = new ChartFrame("Graphique", chart);
            frame.pack();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
