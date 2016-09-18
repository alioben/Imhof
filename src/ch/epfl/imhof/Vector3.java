package ch.epfl.imhof;

/**
 * Représente un vecteur tridimensionnel.
 *
 * @author Ben Lalah Ali (251758)
 * @author Alami Idrissi Ali (251759)
 */
public final class Vector3 {
    private final double x, y, z;
    
    /**
     * 
     * @param x Coordonées x du vecteur
     * @param y Coordonées y du vecteur
     * @param z Coordonées z du vecteur
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * 
     * @return La norme du vecteur
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * 
     * @return Le vecteur normalisé
     */
    public Vector3 normalized() {
        double norm = norm();
        return new Vector3(x / norm, y / norm, z / norm);
    }
    
    /**
     * 
     * @param that Deuxieme terme du produit scalaire.
     * @return Le produit scalaire du vecteur avec celui entré en paramètre.
     */
    public double scalarProduct(Vector3 that) {
        return this.x * that.x + this.y * that.y + this.z * that.z;
    }
    
}
