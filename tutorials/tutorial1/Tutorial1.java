/* Tutorial1.java
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 28 may 2012 - 22:52
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.tutorials.tutorial1;

import scom.tutorials.tutorial1.ItSuccessorF;
import scom.It;
import static scom.It.*;

public class Tutorial1
{
  public static String K_MOTOR = "moteur";
  
  public static void main(String[] args) 
  {
    Print("**** SCOM: Tutorial1 sample ****");
    
    //----- 'item1' It instance -----   
    It item  = New(K_VALUE, "this is item1")
               .putFacet(NAME,        New("item1"))
               .putFacet("version",   New("1.01"))
               .putFacet(ItSuccessorF.BASENAME, New(K_FUNCTION, "successor", ItSuccessorF.CLASS_NAME));
    Print(item);
    Print("  successeur de '" + item + "': " + item.getFacet(ItSuccessorF.BASENAME).evaluate(New(2)));
    //----- 'item1' It instance
    
    
    //----- 'Voiture' class -----
    It voiture_class_it = NewClass("Voiture")
                          .putFacet(K_MOTOR, New("rolls royce"))
                          .putFacet(ItSuccessorF.BASENAME, New(K_FUNCTION, "successor", ItSuccessorF.CLASS_NAME));
    
    It ma_voiture = New(voiture_class_it);
      Print(ma_voiture);
      Print("successor of '1': " + ma_voiture.getFacet(ItSuccessorF.BASENAME).evaluate(New(1)));
      Print("");
      Print(ma_voiture, WITH_UNLIMITED_DEPTH);
    //----- 'Voiture' class
  } //---- main()
} //---------- Tutorial1
