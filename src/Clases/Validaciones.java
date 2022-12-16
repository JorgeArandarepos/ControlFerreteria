/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author jorch
 */
public class Validaciones {

    String entrada = null;

    public Validaciones(String entrada) {
        this.entrada = entrada;
    }

    public static boolean isNumeric(String entrada) {
        try {
            Integer.parseInt(entrada);
            return true;
        } catch (NumberFormatException nfe) {
            return false;

        }
    }
        
}
