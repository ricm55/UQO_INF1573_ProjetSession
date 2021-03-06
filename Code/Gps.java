/**
 * Nom: Gps
 * Version: 1.0
 * Date: 03/26/2021
 * Auteur: Membres de l'equipe 4
 * 
 * Description: Permet de recuperer la trajectoire de la voiture et l'etat des routes sur la carte
 * 
 * Copyright 2021 equipe 4
 */

package Code;
	
import java.util.ArrayList;

public class Gps extends Route {

	/**
	 * Attributs de la classe
	 */
	private int noeudDepart;
	private int noeudFin;

	protected ArrayList<Route> cheminRoute;
	private int distanceParcourue;
	public Route[] listeRoutes;

	// Graphe d'origine sans congestion
	static final int[][] DISTNOEUD = { { 0, 5, 0, 0, 0, 1, 2, 0 },
			{ 5, 0, 1, 0, 0, 0, 3, 0 },
			{ 0, 1, 0, 6, 0, 0, 0, 3 },
			{ 0, 0, 6, 0, 12, 0, 0, 6 },
			{ 0, 0, 0, 12, 0, 3, 0, 4 },
			{ 1, 0, 0, 0, 3, 0, 1, 0 },
			{ 2, 3, 0, 0, 0, 1, 0, 10 },
			{ 0, 0, 3, 6, 4, 0, 10, 0 } };

	// Copie du graphe d'origine qui sera modifie
	public  int[][] copieGraphe = new int[8][8];

	/**
	 * Constructeur avec parametre
	 * 
	 * @param noeudDepart
	 * @param noeudFin
	 */
	public Gps(int noeudDepart, int noeudFin) {

		this.noeudDepart = noeudDepart;

		this.noeudFin = noeudFin;

		cheminRoute = new ArrayList<Route>() ;

		creationRoute();

		setDistanceParcourue( 0 );
		
		this.copieGraphe = copieGraphe(Gps.DISTNOEUD);

	}

	/**
	 * Creation des routes
	 */
	public void creationRoute() {

		listeRoutes = new Route[26]; // Creer les routes
		listeRoutes[0]  = new Route( 13, 5, 0, 1);  //maxVoiture, longueur, noeudDepart, noeudArrive
		listeRoutes[1]  = new Route( 5, 2, 0, 6); 
		listeRoutes[2]  = new Route( 3, 1, 0, 5); 
		listeRoutes[3]  = new Route( 13, 5, 1, 0);
		listeRoutes[4]  = new Route( 3, 1, 1, 2);
		listeRoutes[5]  = new Route( 8, 3, 1, 6);
		listeRoutes[6]  = new Route( 3, 1, 2, 1);
		listeRoutes[7]  = new Route( 8, 3, 2, 7);
		listeRoutes[8]  = new Route( 15, 6, 2, 3);
		listeRoutes[9]  = new Route( 15, 6, 3, 2);
		listeRoutes[10] = new Route( 15, 6, 3, 7);
		listeRoutes[11] = new Route( 30, 12, 3, 4);
		listeRoutes[12] = new Route( 8, 3, 4, 5);
		listeRoutes[13] = new Route( 10, 4, 4, 7);
		listeRoutes[14] = new Route( 30, 12, 4, 3);
		listeRoutes[15] = new Route( 3, 1, 5, 0);
		listeRoutes[16] = new Route( 3, 1, 5, 6);
		listeRoutes[17] = new Route( 8, 3, 5, 4);
		listeRoutes[18] = new Route( 5, 2, 6, 0);
		listeRoutes[19] = new Route( 8, 3, 6, 1);
		listeRoutes[20] = new Route( 3, 1, 6, 5);
		listeRoutes[21] = new Route( 25, 10, 6, 7);
		listeRoutes[22] = new Route( 8, 3, 7, 2);
		listeRoutes[23] = new Route( 15, 6, 7, 3);
		listeRoutes[24] = new Route( 10, 4, 7, 4);
		listeRoutes[25] = new Route( 25, 10, 7, 6);

	}

	/**
	 * Accesseur pour le tableau de routes
	 * 
	 * @return : ArrayList cheminRoute
	 */
	public ArrayList<Route> getCheminRoute() {

		return this.cheminRoute;
	}

	/**
	 * Initialise le noeud de depart
	 * 
	 * @param n : le noeud de depart
	 */
	public void setNoeudDepart( int n ) {

		this.noeudDepart = n;
	}

	/**
	 * Initialise le noeud d'arrivee
	 * 
	 * @param n : le noeud d'arrivee
	 */
	public void setNoeudFin( int n ) {

		this.noeudFin = n;
	}

	/**
	 * Accesseur pour le noeud de depart
	 * 
	 * @return le noeud de depart
	 */
	public int getNoeudDepart() {

		return noeudDepart;
	} 

	/**
	 * Accesseur pour le noeud d'arrivee
	 * 
	 * @return le noeud d'arrivee
	 */
	public int getNoeudFin() {

		return this.noeudFin ;
	}

	/**
	 * Transforme le tableau de noeud de dijkstra en tableau de routes.
	 * 
	 * @param positionActuelle
	 */
	public void calculeItineraire(int positionActuelle)   {

		copieGraphe = modifierGraphe();		// modifie le graphe en fonction des routes fermees

		int [] cheminNoeud;		// tableau qui contiendra l'itineraire

		try {

			cheminNoeud = Dijkstra.cheminASuivre(copieGraphe, positionActuelle, noeudFin ); //Graphe, Depart, Arrivee

		} catch(ArrayIndexOutOfBoundsException e) {		// Cas ou toutes les routes autour sont fermees

			System.out.println("Zone Erreur, toute fluide!!!");

			for(Route route : listeRoutes) {	// Met toutes les routes fluides
			    route.setEtat(EtatRoute.FLUIDE);
			}
			
			cheminNoeud = Dijkstra.cheminASuivre(DISTNOEUD, positionActuelle, noeudFin ); 	//Utilise le graphe d'origine 

		}

		if (!this.cheminRoute.isEmpty()) {		// si l'itineraire n'est pas vide, le vider

			this.cheminRoute.clear();

		}

		for (int i =0; i<cheminNoeud.length-1; i++) {

			for (int j = 0; j<= listeRoutes.length-1; j++) {

				if((cheminNoeud[i] == listeRoutes[j].getNoeud(0)) && 
						(cheminNoeud[i+1] == listeRoutes[j].getNoeud(1))) {

					this.cheminRoute.add(listeRoutes[j]);
					//si une route est relie par les deux noeuds, l'ajouter 
				}
			}	
		}

	}


	/**
	 * Ajoute la distance recemment parcourue a la distance totale parcourue.
	 * 
	 * @param distance : distance recemment parcourue
	 */
	protected void ajouterDistance( int distance ) {

		distanceParcourue += distance;
	}

	/**
	 * Permet d'acceder a la liste des routes
	 * 
	 * @return liste des routes
	 */
	public Route[] getListeRoute() {

		return listeRoutes;
	}

	/**
	 * Permet de generer du traffic sur toutes les routes
	 */
	public void reinitialiserTraffic() {

		for(int i = 0;  i < listeRoutes.length; i++) {

			listeRoutes[i].genererTrafic();
		}

	}

	/**
	 * Permet d'acceder a la distance totale parcourue
	 * 
	 * @return la distance totale parcourue
	 */
	public double getDistance() {
		return this.distanceParcourue;
	}
	
	/**
	 * Permet de copier un graphe (tableau en 2 dimensions)
	 * 
	 * @param premierGraphe
	 * @return
	 */
	public int[][] copieGraphe(int [][]premierGraphe){
		
		int [][] copieGraphe = new int [8][8];
		
		for (int i = 0 ; i<DISTNOEUD.length; i++) {

			for(int y = 0 ; y <DISTNOEUD[i].length; y++) {

				copieGraphe[i][y] =	DISTNOEUD[i][y] ;

			}

		}
		
		return copieGraphe;
	}

	/**
	 * Initialise la distance totale parcourue
	 * 
	 * @param distance : distance totale parcourue
	 */
	private void setDistanceParcourue( int distance ) {

		this.distanceParcourue = distance;
	}

	/**
	 * Modifie le graphe en remplacant les routes fermees par des 0 dans 
	 * la copie du graphe de depart.
	 * 
	 * @return la copie du graphe de depart avec les routes fermees
	 */
	private int[][] modifierGraphe(){
		
		this.copieGraphe = copieGraphe(Gps.DISTNOEUD);

		for (int i = 0 ; i< listeRoutes.length; i++) {
			
			if((listeRoutes[i].getEtat() == EtatRoute.CONGESTION) || ( listeRoutes[i].getEtat() == EtatRoute.ACCIDENT)){	
				// Si une route est fermee
				copieGraphe[listeRoutes[i].getNoeud(0) ][listeRoutes[i].getNoeud(1)] = 0 ;
			}
		}

		return copieGraphe;
	}

}