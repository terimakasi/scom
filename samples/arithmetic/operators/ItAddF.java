/**
 * ItAdd.java
 * An Additioner It subclass which overrides evaluate() method
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 27 may 2012 - 16:54
 * Copyright (C) <2012> www.terimakasi.com
 */
package scom.samples.arithmetic.operators;

import java.util.ArrayList;
import scom.It;

public class ItAddF extends It
{
  public static final String BASENAME   = ItAddF.class.getSimpleName();
  public static final String CLASS_NAME = ItAddF.class.getCanonicalName();
          
  protected ItAddF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    Double output_value = new Double(0);
    for (int i=0; i < input.size(); i++)
    {
      output_value += Double.parseDouble(input.get(i).evaluate().toString());
    }
    return It.New(K_VALUE, output_value.toString());
  } //---- evaluate() 
  
  @Override
  public String toString()
  {  
    return BASENAME;
  } //---- toString() 
} 