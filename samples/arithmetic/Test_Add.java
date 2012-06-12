/**
 * Test_Add.java
 * Test of the Additioner Item: ScomItemAdd (subclass of ScomItem)
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.arithmetic;

import scom.It;
import scom.samples.arithmetic.operators.ItAddF;

public class Test_Add 
{
  public static void main(String[] args) 
  {
    It.Print("**** Test_Add ****");
    
    It add_function = It.New(ItAddF.BASENAME, ItAddF.BASENAME, ItAddF.CLASS_NAME);
    It.Print(add_function.evaluate(It.ToList(new Object[]{0.33, 0.66})));
  } //---- main()
} //---------- Test_Add