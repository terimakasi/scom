/**
 * Test_AOM.java
 * Test of It class: Adaptative Object Model features
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.aom;

import static scom.It.*;
import scom.It;

public class Test_AOM 
{
  public static void main(String[] args) 
  {
    Print("**** Test_AOM: Adaptative Object Model features ****");
    
    Print(INTEGER);
        
    It thing_class = NewClass("Thing");
      Print(thing_class); // instance count should be 0
    
    It thing_it = thing_class.evaluate(); // implicitly calls New(thing_class);
      Print(thing_it);
      Print(thing_class); // now instance count should be 1

    It object_it = NIL;
    object_it = New(OBJECT);
      Print("'" + object_it + "'          : " + object_it);
    
    object_it = New(OBJECT);
      Print("'" + object_it + "'          : " + object_it);
     
    Print("----------------------");
    Print(ENVIRONMENT, WITH_UNLIMITED_DEPTH);
  } //---- main()
} //---------- Test_AOM
