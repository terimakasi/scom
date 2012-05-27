// HelloWorld.java
// Michel Kern - 27 may 2012 - 16:54
package scom.tutorials.helloworld;

import static scom.It.*;
import scom.It;

public class HelloWorld
{
  public static String K_MOTOR = "moteur";
  
  public static void main(String[] args) 
  {
    Print("**** SCOM: HelloWorld sample ****");
    
    //----- 'item1' It instance -----   
    It item  = New(K_VALUE, "this is item1")
               .putFacet(K_NAME,            New("item1"))
               .putFacet("version",         New("1.01"))
               .putFacet(ItSuccessorF.NAME, New(K_FUNCTION, "successor", ItSuccessorF.CLASS_NAME));
    Print(item);
    Print("  successeur de '" + item + "': " + item.getFacet(ItSuccessorF.NAME).evaluate(New(2)));
    //----- 'item1' It instance
    
    
    //----- 'Voiture' class -----
    It motor = New("rolls royce")
               .putFacet(K_MODEL_LAYER, USER_MODEL);
    It voiture_class_it = NewClass("Voiture")
                          .putFacet(K_MOTOR, motor);
    
    It ma_voiture = New(voiture_class_it);
      Print("");
      Print(ma_voiture, WITH_UNLIMITED_DEPTH, "");
    //----- 'Voiture' class
  } //---- main()
} //---------- HelloWorld