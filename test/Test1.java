/**
 * Test1.java
 * Test of ScomItem: add connections and retrieve them
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 20 may 2012 - 22:31
 * Copyright (C) <2012> www.terimakasi.com
 * ...................................................................................
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of 
 * the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * ...................................................................................
 */
package scom.test;

import scom.It;

public class Test1 
{
  public static void main(String[] args) 
  {
    System.out.println("**** Test1: connections and AOM (Adaptative Object Model) ****");
    
    It thing_class          = It.NewClass("Thing");
    It anotherthing_class   = It.NewClass("AnotherThing");
    
    System.out.print("'Integer'          : "      + It.INTEGER);
    System.out.print("   super(Integer):       "  + It.INTEGER.getFacet(It.K_SUPERCLASS));
    System.out.println("    class(Integer):     " + It.INTEGER.getFacet(It.K_CLASS));
    
    System.out.print("'thing_class'      : "       + thing_class);
    System.out.print("     super(thing_class):   " + It.INTEGER.getFacet(It.K_SUPERCLASS));
    System.out.println("    class(thing_class): "  + thing_class.getFacet(It.K_CLASS));
    
    It thing_it = thing_class.evaluate(); // implicitly calls New(thing_class)
    System.out.print("'" + thing_it + "'           : " + thing_it);
    System.out.println("                                    class('" + thing_it + "'):    " + thing_it.getFacet(It.K_CLASS));
    
    
    thing_it = It.New(thing_class);
    
    System.out.println("'" + thing_it + "'           : " + thing_it + "\n");

    It object_it            = It.New(It.OBJECT);
    System.out.println("'" + object_it + "'          : " + object_it);
    
    object_it               = It.New(It.OBJECT);
    System.out.println("'" + object_it + "'          : " + object_it + "\n");
  
    It another_thing_it     = It.New(anotherthing_class);
    System.out.println("'another_thing_it' : " + another_thing_it);
    
    It thing_it_class       = thing_it.getFacet(It.K_CLASS);
    System.out.println("  'class' facet of '" + thing_it.getValue() + "': "       + thing_it_class);
    
    It thing_it_class_class = thing_it_class.getFacet(It.K_CLASS);
    System.out.println("  'class' facet of '" + thing_it_class.getFacet(It.K_CLASS) + "' : " + thing_it_class_class);
    
    thing_it.setValue(It.NewValue("thing1_new_name"));
    System.out.println("after modification of 'thing_it'");
    System.out.println("  '" + thing_it + "'.evaluate()    : " + thing_it.evaluate());
  } //---- main()
} //---------- Test1