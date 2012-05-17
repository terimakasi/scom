/**
 * ItLispAtomF.java
 * atom() Lisp function
 * http://www.math.utah.edu/docs/info/emacs-lisp-intro_8.html
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
package scom.samples.lang.lisp.functions;

import java.util.ArrayList;
import scom.*;
import scom.samples.lang.lisp.*;

public class ItLispAtomF extends It
{
  public static final String NAME       = "cdr";
  public static final String CLASS_NAME = "scom.samples.lang.lisp.functions.ItLispAtomF";
          
  protected ItLispAtomF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  {  
    if (input.size() < 1) return It.TRUE;
    
    It cons_it = input.get(0);
    //System.out.println("ItLispAtompF.evaluate cons_it  key:" + cons_it.getKey() + " value:" + cons_it.getValue());
   
    if (cons_it.getNext() == It.NIL)
      return It.TRUE;
    
    It next_it = It.TRUE;
    try 
    {
      next_it = (It) cons_it.getNext(); 
      return It.NIL;
    }
    catch (Exception e) {}
    
    return It.TRUE;
  } //---- evaluate() 
} //---------- ItLispAtomF