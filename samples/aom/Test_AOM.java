/**
 * Test_AOM.java
 * Test of It class: Adaptative Object Model features
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
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
