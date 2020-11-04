package analizadorcompiladores;
import java.util.ArrayList;
import java.util.Scanner;

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
                    break;
                case 3: //concatenar AFN
                    System.out.println("hola");
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
            System.out.println("Estado inicial: "+
                    listaAFN.get(0).getEstadoInicial().getIdentificador());
            System.out.println("Trasicion: "+
                    listaAFN.get(0));
        }
    }
    
    
    private static void crearAutomataBasico(char c){
        AFN afn = new AFN();
        afn.crearBasico(c, listaAFN.size()+1);        
        listaAFN.add(afn);
    }

}
