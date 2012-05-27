/**
 * ItLispEqF.java
 * eq() Lisp function
 * http://www.cs.cmu.edu/Groups/AI/html/cltl/clm/node74.html
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
package scom.samples.lang.lisp.functions;

import java.util.ArrayList;
import scom.*;
import scom.samples.lang.lisp.*;

public class ItLispEqF extends It
{
  public static final String NAME       = "cdr";
  public static final String CLASS_NAME = ItLispEqF.class.getCanonicalName();
          
  protected ItLispEqF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  {  
    if (input.size() != 2) return It.NIL;
    
    It arg1 = input.get(0);
    It arg2 = input.get(1);
    
    if (arg1.getUUID().equals(arg2.getUUID()))
      return It.TRUE;
    
    return It.NIL;
  } //---- evaluate() 
  
  @Override
  public String toString()
  {  
    return NAME;
  } //---- toString() 
} //---------- ItLispEqF