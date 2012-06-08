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
    
    It item  = It.New(); // this creates an instance of 'OBJECT' native AOM class
    item.putFacet(VALUE, New("Hello World !")); 
    
    // Prints object's value and facets on Standard console output
    Print(item);
    
    // Now print object's value and facets in 'object1.txt' file
    Print(item, TEXT_FILE_WRITER);
    
    // Now print object's value and facets in a String
    String output_str = Print(item, STRING_WRITER);
    Print(output_str);
    
    // Now print item facets recursively
    //Print(item, WITH_UNLIMITED_DEPTH);
    
    // Calls interactive command shell
    SHELL.evaluate();
  } //---- main()
} //---------- HelloWorld