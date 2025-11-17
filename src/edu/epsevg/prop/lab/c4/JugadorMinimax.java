/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.epsevg.prop.lab.c4;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author asier
 */
public class JugadorMinimax implements Jugador, IAuto{
    private String nom;
    private int profunditatMaxima;
    private long nodesExplorats;
    private int colorPropi;
    
    public JugadorMinimax(int profunditat){
        this.nom = "JugadorMinimax";
        this.profunditatMaxima = profunditat;
        this.nodesExplorats = 0;
    }
    
    @Override
    public String nom() {
        return nom;
    }

    @Override
    public int moviment(Tauler t, int color) {
        
    }
    
    private int minimax(Tauler t, int profunditat, int alpha, int beta, boolean esMaximitzant, int color){
        
    }
    
    
    //Heuristica, no tocar
    private int verificarVictoria(Tauler t){
        for(int col = 0; col < t.getMida(); col++){
            if(t.solucio(col, colorPropi)){
                return 100000;
            }
            if(t.solucio(col, -colorPropi)){
                return -100000;
            }
        }
        return 0;
    }
    
    private int avaluarTauler(Tauler t){
        int victoria = verificarVictoria(t);
        if(victoria != 0) return victoria;
        
        int puntuacio = 0;
        
        int amenacesPropia = contarAmenaces(t,colorPropi);
        int amenacesContrari = contarAmenaces(t,-colorPropi);
        
        if (amenacesContrari > 0) {
            puntuacio -= amenacesContrari * 5000;
        }
        if (amenacesPropia > 0) {
            puntuacio += amenacesPropia * 4000;
        }
        
        puntuacio += avaluarLinies(t, colorPropi) - avaluarLinies(t, -colorPropi);
        puntuacio += avaluarCentre(t, colorPropi) - avaluarCentre(t, -colorPropi);
        puntuacio += avaluarConnectivitat(t, colorPropi) - avaluarConnectivitat(t, -colorPropi);
        
        return puntuacio;
    }
    
    private int contarAmenaces(Tauler t, int color){
        int amenaces = 0;
        int mida = t.getMida();
        
        for(int i = 0; i < mida; i++){
            for(int j = 0; j <= mida - 4; j++){
                if(esAmenaca(t,i,j,0,1,color)) amenaces++;
            }
        }
        
        for(int j = 0; j < mida; j++){
            for(int i = 0; i <= mida - 4; i++){
                if(esAmenaca(t,i,j,1,0,color)) amenaces++;
            }
        }
        
        for(int i = 0; i <= mida - 4; i++){
            for(int j = 0; j <= mida - 4; j++){
                if(esAmenaca(t,i,j,1,1,color)) amenaces++;
            }
        }
        
        for(int i = 3; i <= mida - 4; i++){
            for(int j = 0; j <= mida - 4; j++){
                if(esAmenaca(t,i,j,0,1,color)) amenaces++;
            }
        }
        
        return amenaces;
    }
    
    private boolean esAmenaca(Tauler t, int casellaI, int casellaJ, int vertical, int horitzontal, int color ){
        int propis = 0;
        int buits = 0;
        
        for(int i = 0; i < 4; i++){
            int casella = t.getColor(casellaI + i*vertical, casellaJ + i*horitzontal);
            if(casella == color){
                propis++;
            }else if(casella == 0){
                buits++;
            }else{
                return false;
            }
        } 
        return (propis == 3 && buits == 1);
    }
    
    
    private int avaluarLinies(Tauler t, int color){
        int puntuacio = 0;
        int mida = t.getMida();
        
        for(int i = 0; i < mida; i++){
            for(int j = 0; j < mida - 4; j++){
                puntuacio += avaluarSequencia(t,i,j,0,1,color);
            }
        }
        
        for(int j = 0;  j < mida; j++){
            for(int i = 0; i < mida - 4; i++){
                puntuacio += avaluarSequencia(t,i,j,1,0,color);
            }
        } 
        
        for (int i = 0; i <= mida - 4; i++) {
            for (int j = 0; j <= mida - 4; j++) {
                puntuacio += avaluarSequencia(t, i, j, 1, 1, color);
            }
        }
        
        for (int i = 3; i < mida; i++) {
            for (int j = 0; j <= mida - 4; j++) {
                puntuacio += avaluarSequencia(t, i, j, -1, 1, color);
            }
        }
        
        return puntuacio;
    }
    
    private int avaluarSequencia(Tauler t, int casellaI, int casellaJ, int vertical, int horitzontal, int color){
        int propis = 0;
        int buits = 0;
        int contraris = 0;
        
        for(int i = 0; i < 4; i++){
            int casella = t.getColor(casellaI + i*vertical, casellaJ + i*horitzontal);
            if(casella == color){
                propis++;
            }else if(casella == -color){
                contraris++;
            }else{
                buits++;
            }
        }
        
        if(contraris > 0 && propis > 0){
            return 0;
        }
        
        if(propis == 3 && buits == 1){
            return 100;
        }else if(propis == 2 && buits == 2){
            return 20;
        }else if(propis == 1 && buits == 3){
            return 1;
        }
        
        return 0;
    }
    
    private int avaluarCentre(Tauler t, int color){
        int puntuacio = 0;
        int mida = t.getMida();
        int centre = mida/2;
        for(int i = 0; i < mida; i++){
            if(t.getColor(i, centre) == color) puntuacio +=3;
        }
        return puntuacio;
    }
    
     private int avaluarConnectivitat(Tauler t, int color){
         int puntuacio = 0;
         int mida = t.getMida();
         
         for(int i = 0; i < mida; i++){
             for(int j = 0; j < mida; j++){
                  if (t.getColor(i, j) == color) {
                    if (j < mida - 1 && t.getColor(i, j + 1) == color) puntuacio += 2;
                    if (i < mida - 1 && t.getColor(i + 1, j) == color) puntuacio += 2;
                    if (i < mida - 1 && j < mida - 1 && t.getColor(i + 1, j + 1) == color) puntuacio += 2;
                    if (i < mida - 1 && j > 0 && t.getColor(i + 1, j - 1) == color) puntuacio += 2;
                }
             }
         }
         return puntuacio;
     }
    
}
