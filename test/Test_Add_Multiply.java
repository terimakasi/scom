/**
 * Test_Add_Multiply.java
 * Test of the Additioner and Multiplier Items: ScomItemAdd and ScomItemMultiply
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

import scom.samples.operators.ItMultiply;
import scom.It;
import java.util.ArrayList;
import scom.samples.operators.ItAdd;

public class Test_Add_Multiply 
{
  public static void main(String[] args) 
  {
    System.out.println("**** Test_Add_Multiply ****");
    
    It add_function               = It.New(It.K_FUNCTION, "add",      ItAdd.CLASS_NAME);
    It multiply_function          = It.New(It.K_FUNCTION, "multiply", ItMultiply.CLASS_NAME);
   
    ArrayList<It> multiply_params = It.ToArgList(new Object[]{"2", "3"});
      multiply_params.add(add_function.evaluate(It.ToArgList(new Object[]{"1.5", "3.5"})));
      
    It multiply_result = multiply_function.evaluate(multiply_params);
    System.out.println("(1.5 + 3.5) * 2 * 3 = " + multiply_result);
  } //---- main()
} //---------- Test_Add_Multiply