package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.*;
public class Principal {
    
    private static AFN miAFN;
    private static ArrayList<AFN> listaAFN = new ArrayList();
    private static ArrayList<AFD> listaAFD = new ArrayList();
    
    public static void main (String[] args){
      
        
        int opcionMenu;
        Scanner leer = new Scanner(System.in);
        boolean salir=false;
        
        while(salir==false){
            System.out.println("-------MENU-------");
            System.out.println("1. Crear AFN básico."); //In: un caracter, y ponerle ID (para cerradura, etc)
            System.out.println("2. Unir 2 AFN."); //In: 2do AFN, ID para AFN unión, Out: AFN unión
            System.out.println("3. Concatenar 2 AFN.");
            System.out.println("4. Cerradura Transitiva o de Kleene");
            System.out.println("5. Unión para Analizador Léxico."); //qué automata quiero unir (mostrar tabla con automatas)
            System.out.println("6. Convertir AFN a AFD."); //cualquier AFN, hasta el de la opcion 6. In: ID del AFN
            System.out.println("7. Validar cadena usando un AFN o un AFD."); //seleccionar AFN o AFD de los ya ingresados
            System.out.println("8. Opcional.");
            System.out.println("9. Salir");
            System.out.print("Selecciona el número de la opción deseada: ");
            opcionMenu = leer.nextInt();

            switch(opcionMenu){
                case 1: //crear AFN
                    System.out.print("Ingresa el caracter para la transición del AFN: ");
                    char c = leer.next().charAt(0);
                    System.out.println("C: "+c);
                    crearAutomataBasico(c);
                    break;
                case 2: //Unir AFN
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                /*    AFN afn3 = (AFN) listaAFN.get(0).clone();
                    AFN afn4 = (AFN) listaAFN.get(1).clone();
                    afn3.unirAFN(afn4);
                    listaAFN.add(afn3);
*/
                    AFN afn3= ClonarUtilidad.cloneAFN(listaAFN.get(0) );
                    AFN afn4= ClonarUtilidad.cloneAFN(listaAFN.get(1) );
                    afn3.unirAFN(afn4);
                    listaAFN.add(afn3);
                    
                    

                    break;
                case 3: //concatenar AFN
                    crearAutomataBasico('c');
                    crearAutomataBasico('d');
                    listaAFN.get(0).concatenarAFN(listaAFN.get(1)); 
                    listaAFN.remove(1);
                    break;
                case 4: //Cerradura
                    break;
                case 5: //Analizador Léxico
                    System.out.println("hola");
                    break;
                case 6: //Convertir AFN a AFD
                    break;
                case 7: //Validar cadena
                    System.out.println("hola");
                    break;
                case 8: //Opcional
                    break;
                default:
                    salir=true;
                    System.out.println("Hasta luego!");
                    break;
            }
//            System.out.println("AFN: "+listaAFN.get(0).getIdAFN());
//            //System.out.println("Trasicion: "+ listaAFN.get(0).getEstadoInicial().getTransicion().getSimbolo());
//            System.out.println("Trasicion: "+ listaAFN.get(0).getEstadoInicial().getTransicion().getSimbolo());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(listaAFN));
        }
    }
    
    private static void crearAutomataBasico(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, listaAFN.size()+1);    
        //System.out.println("Automata con id:" +afn.getIdAFN());
        listaAFN.add(afn);
    }

}
