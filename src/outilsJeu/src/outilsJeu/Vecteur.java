package outilsJeu;

//permet de plus facilement une double donnée, utilisé dans les vecteurs de direction/déplacement mais aussi dans les vecteurs de coordonnées
public class Vecteur {
	public int x;
	public int y;

    public Vecteur(){}
	public Vecteur(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Vecteur(String direction){
		if (direction.equals("haut")){
			x = 0;
			y = 1;
			return;
		}
		if (direction.equals("droite")){
			x = 1;
			y = 0;
			return;
		}
		if (direction.equals("bas")){
			x = 0;
			y = -1;
			return;
		}
		if (direction.equals("gauche")){
			x = -1;
			y = 0;
			return;
		}
		throw new IllegalArgumentException("Direction invalide.");
	}
	
	@Override
    public String toString(){
        return "x: " + x + ", y:" + y;
    }
}
