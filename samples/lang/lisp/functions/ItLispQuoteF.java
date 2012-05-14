/**
 * ItLispQuoteF.java
 * quote() Lisp function
 * http://www.math.utah.edu/docs/info/emacs-lisp-intro_8.html
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 1 may 2012 - 23:42
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
import scom.It;
import scom.samples.lang.lisp.*;

public class ItLispQuoteF extends It
{
  public static final String NAME       = "quote";
  public static final String CLASS_NAME = "scom.samples.lang.lisp.functions.ItLispQuoteF";
          
  protected ItLispQuoteF(Object key, Object value, Object option) 
  {
    super(key, value, option);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  {  
    if (input.size() != 1) return It.NIL;
    return input.get(0);
  } //---- evaluate() 
} //---------- ItLispQuoteF