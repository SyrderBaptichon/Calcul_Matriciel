package AlgLin;

import java.io.*;
import java.util.*;

/**
 * Classe abstraite représentant un système linéaire à résoudre.
 * Cette classe définit la structure de base d'un système linéaire, 
 * avec la matrice du système et son second membre, ainsi que des méthodes 
 * pour accéder aux propriétés du système et vérifier sa validité.
 */
public abstract class SysLin {
	
    /** Taille du système (nombre de lignes/colonnes de la matrice et du second membre). */
	private int ordre;
	
    /** Matrice du système linéaire. */
	protected Matrice matriceSystem;
	
    /** Second membre du système linéaire. */
	protected Vecteur secondMembre;				

	/**
     * Constructeur de la classe SysLin.
     * Ce constructeur initialise un système linéaire avec la matrice et le second membre donnés.
     * Il vérifie que la matrice est carrée et que sa taille correspond à celle du second membre.
     * Si l'une de ces conditions n'est pas remplie, une exception IrregularSysLinException est levée.
     * 
     * @param m La matrice du système.
     * @param secondMembre Le second membre du système.
     * @throws IrregularSysLinException Si la matrice n'est pas carrée ou si sa taille ne correspond pas à celle du second membre.
     */
	public SysLin(Matrice m, Vecteur secondMembre) throws IrregularSysLinException{
		if(m.nbLigne() != m.nbColonne()) {	
			throw new IrregularSysLinException("Matrice non carrée.");
		}
		if(secondMembre.getTaille() != m.nbLigne()) {
			throw new IrregularSysLinException("Mauvais second membre.");
		}
		this.secondMembre = secondMembre;
		this.matriceSystem = m;
		this.ordre = m.nbLigne();
	}
	
	public SysLin(Mat3Diag m, Vecteur secondMembre) throws IrregularSysLinException{
		if(secondMembre.getTaille() != m.nbColonne()) {
			throw new IrregularSysLinException("Mauvais second membre.");
		}
		this.secondMembre = secondMembre;
		this.matriceSystem = m;
		this.ordre = m.nbLigne();
	}

	/**
     * Retourne l'ordre du système, c'est-à-dire la taille de la matrice (ou du second membre).
     * 
     * @return L'ordre du système.
     */
	public int getOrdre() {
		return ordre;
	}

	/**
     * Retourne la matrice du système linéaire.
     * 
     * @return La matrice du système.
     */
	public Matrice getMatriceSystem() {
		return matriceSystem;
	}

	/**
     * Retourne le second membre du système linéaire.
     * 
     * @return Le second membre du système.
     */
	public Vecteur getSecondMembre() {
		return secondMembre;
	}

	/**
     * Méthode abstraite qui résout le système linéaire.
     * Cette méthode doit être implémentée dans les classes dérivées pour résoudre des systèmes particuliers.
     * Elle peut lever une exception IrregularSysLinException si le système est irrégulier.
     * 
     * @return Le vecteur solution du système.
     * @throws IrregularSysLinException Si le système est irrégulier.
     */
	public abstract Vecteur resolution() throws IrregularSysLinException;
}