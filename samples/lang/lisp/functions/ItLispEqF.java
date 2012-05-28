/**
 * ItLispEqF.java
 * eq() Lisp function
 * http://www.cs.cmu.edu/Groups/AI/html/cltl/clm/node74.html
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.lang.lisp.functions;

import java.util.ArrayList;
import scom.*;
import scom.samples.lang.lisp.*;

public class ItLispEqF extends It
{
  public static final String BASENAME   = "eq";
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
    return BASENAME;
  } //---- toString() 
} //---------- ItLispEqF