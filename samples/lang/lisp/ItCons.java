/**
 * ItCons.java
 * The Lisp 'cons' data structure:
 * a cons is a pair:
 *   - 1st element is returned by car() function
 *   - 2nd element is returned by cdr() function
 * http://en.wikipedia.org/wiki/Conshttp://en.wikipedia.org/wiki/Cons
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
package scom.samples.lang.lisp;

import java.util.ArrayList;
import scom.It;
import scom.It;
import scom.samples.lang.lisp.functions.ItLispConsF;

public class ItCons extends It
{
  public static final String CLASS_NAME = "scom.samples.lang.lisp.ItCons";
  
  protected ItCons(Object key, Object value, Object next) 
  {
    super(key, value, next);
    //System.out.println("  > ScomCons key:" + key + " value:" + value + " option:" + option);
    
    addFacet(ItLisp.CAR, New(It.K_VALUE, value));
    addFacet(ItLisp.CDR, New(It.K_VALUE, next));
  } // Private Constructor
  
  @Override
  public String toString()
  {      
    String to_string = "";
    It car_it = getFacet(ItLisp.CAR);
    String car_it_str = car_it.toString();
    
    It cdr_it = getFacet(ItLisp.CDR);
    String cdr_it_str = cdr_it.toString();
    if (cdr_it_str.equals(It.K_NIL))
      to_string = car_it.getValue().toString();
    else
      to_string = "(" + car_it_str + "," + cdr_it_str + ")";   
    return to_string;
  } //---- toString() 
} //---------- ItCons