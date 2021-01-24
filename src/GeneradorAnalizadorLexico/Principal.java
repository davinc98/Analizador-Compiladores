/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneradorAnalizadorLexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author J.PEREZ
 */
public class Principal {

    public static void main (String[] args){    
        
        String nombreFichero = "ExpresionesRegulares.txt";
        BufferedReader br = null;
        
        MainGeneradorAnalizadorLexico AFDgenerador = new MainGeneradorAnalizadorLexico();
        
        
        try {
            br = new BufferedReader(new FileReader(nombreFichero));
            String linea = br.readLine();
            while (linea != null) {
                String []cad = linea.split(" ");
                String expreg = cad[0];int token = Integer.parseInt(cad[1]);
                
                AFDgenerador.generarAFN(expreg, token);
                //System.out.println(linea);
                
                linea = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Fichero no encontrado");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de lectura del fichero");
            System.out.println(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                System.out.println("Error al cerrar el fichero");
                System.out.println(e.getMessage());
            }
        }
    }
}
