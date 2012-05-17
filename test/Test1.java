/**
 * Test1.java
 * Test of ScomItem: add connections and retrieve them
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 17 may 2012 - 23:46
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
    System.out.println("**** Test1: add connections and retrieve them ****");
    
    It item1          = It.New(It.K_NAME, "item1");
    It thing_class    = It.New(It.K_NAME, "Thing");
    It object_class   = It.New(It.K_NAME, "Object");
    
    It meta_class     = It.New(It.K_NAME, "Class");
    item1.connect(It.K_CLASS, thing_class);
    thing_class.connect(It.K_CLASS, meta_class);
    
    System.out.println("evaluate on 'item1': " + item1.evaluate());
    
    It query_result;
    query_result = item1.getIt(It.K_CLASS);
    System.out.println("  query(class) on '" + item1.getValue() + "': " + query_result.evaluate());
    
    query_result = query_result.getIt(It.K_CLASS);
    System.out.println("  query(name,class) on '" + thing_class.getValue() + "': " + query_result.evaluate());
    
    System.out.println("  query(***) on '" + item1.getValue() + "': " + item1.getIt("***"));
    
    item1.setValue(It.buildValue("item1_new_name"));
    System.out.println("after modification of 'item1' value:");
    System.out.println("  V: " + item1.getValue());
    System.out.println("  evaluate on 'item1': " + item1.evaluate());
    
    item1.connect(It.K_CLASS, object_class);
    System.out.println("after modification of 'item1' class:");
    query_result = item1.getIt(It.K_CLASS);
    System.out.println("  query(name,class) on '" + item1.getValue() + "': " + query_result.evaluate());
  } //---- main()
} //---------- Test1