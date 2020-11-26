/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package analizadores;
import clases.AFN;
import com.google.gson.*;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class ClonarUtilidad {

    
    
    public static String serializeObject(AFN o) {
       Gson gson = new Gson();
       String serializedObject = gson.toJson(o);
       return serializedObject;
}
//___________________________________________________________________________________

public static AFN unserializeAFN(String s, AFN o){
       Gson gson = new Gson();
       AFN object = gson.fromJson(s, o.getClass());
       return object;
}
          //___________________________________________________________________________________
public static AFN cloneAFN(AFN o){
       String s = serializeObject(o);
       AFN object =  unserializeAFN(s,o);
       return object;
}
}