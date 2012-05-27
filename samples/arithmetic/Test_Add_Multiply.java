/**
 * Test_Add_Multiply.java
 * Test of the Additioner and Multiplier Items: ScomItemAdd and ScomItemMultiply
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