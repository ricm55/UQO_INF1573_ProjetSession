/**
 * Nom: Accident
 * Version: 1.0
 * Date: 03/26/2021
 * Auteur: Membres de l'equipe 4
 * 
 * Desciption : Permet de generer des accidents selon 
 * un pourcentage de probabilites
 * 
 * Copyright 2021 equipe 4
 */

package Code;

import java.util.Random;

public class Accident {
    
	/**
	 * Variable globale
	 */
    private static int probabilite = 5; //Probabilite d'avoir un accident en %
    
    /**
     * Permet de generer un accident en fonction de
     * la probabilite
     * 
     * @return  un boolean : true si un accident a lieu, false
     * sinon
     */
    public static boolean causeAccident() {
        Random random = new Random();
        
        if(random.nextInt( 100 ) < probabilite) {
            return true;
        }
        return false;
    }
    
}
