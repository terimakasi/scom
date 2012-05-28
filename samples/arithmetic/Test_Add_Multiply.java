/**
 * Test_Add_Multiply.java
 * Test of the Additioner and Multiplier Items: ScomItemAdd and ScomItemMultiply
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.arithmetic;

import static scom.It.*;
import scom.It;
import scom.samples.arithmetic.operators.ItMultiplyF;
import scom.samples.arithmetic.operators.ItAddF;
import java.util.ArrayList;

public class Test_Add_Multiply 
{
  public static void main(String[] args) 
  {
    Print("**** Test_Add_Multiply ****");
    
    It add_function      = New(K_FUNCTION, "add",      ItAddF.CLASS_NAME);
    It multiply_function = New(K_FUNCTION, "multiply", ItMultiplyF.CLASS_NAME);
   
    ArrayList<It> multiply_params = ToList(new Object[]{ 2, 3 });
      multiply_params.add(add_function.evaluate(ToList(new Object[]{ 1.5, 3.5 })));
      
    It multiply_result = multiply_function.evaluate(multiply_params);
    Print("(1.5 + 3.5) * 2 * 3 = " + multiply_result);
  } //---- main()
} //---------- Test_Add_Multiply