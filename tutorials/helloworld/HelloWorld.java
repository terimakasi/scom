/* HelloWorld.java
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 28 may 2012 - 22:52
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.tutorials.helloworld;

import scom.tutorials.tutorial1.ItSuccessorF;
import scom.It;
import static scom.It.*;

public class HelloWorld
{
  public static String K_MOTOR = "moteur";
  
  public static void main(String[] args) 
  {
    Print("**** SCOM: HelloWorld sample ****");
    
    It item  = It.New();
    item.putFacet(VALUE, New("Hellow World !")); 
    Print(item);
    //Print(item, WITH_UNLIMITED_DEPTH);
  } //---- main()
} //---------- HelloWorld