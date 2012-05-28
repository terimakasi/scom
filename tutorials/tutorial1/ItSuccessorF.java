/* ItSuccessorF.java
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.tutorials.tutorial1;

import scom.It;

import java.util.ArrayList;

public class ItSuccessorF extends It
{
  public static final String BASENAME   = ItSuccessorF.class.getSimpleName();
  public static final String CLASS_NAME = ItSuccessorF.class.getCanonicalName();
          
  protected ItSuccessorF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    if (input.size() < 1) return It.NIL;
    
    It      arg1   = input.get(0);
    Integer value  = Integer.parseInt(arg1.getValue().toString());
    //System.out.println("ItSuccessorF.evaluate arg1: " + arg1);
    Integer output = value + 1;
    return New(output);
  } //---- evaluate() 
} //---------- ItSuccessorF