/**
 * ItMultiply.java
 * A Multiplier It subclass which overrides evaluate() method
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
package scom.samples.arithmetic.operators;

import scom.*;
import java.util.ArrayList;

public class ItMultiplyF extends It
{
  public static final String NAME       = ItMultiplyF.class.getName();
  public static final String CLASS_NAME = ItMultiplyF.class.getCanonicalName();
      
  protected ItMultiplyF(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    Double output_value = new Double(1);
    for (int i=0; i < input.size(); i++)
    {
      output_value *= Double.parseDouble(input.get(i).evaluate().toString());
    }
    return It.New("result", output_value.toString());
  } //---- evaluate()
  
  @Override
  public String toString()
  {  
    return NAME;
  } //---- toString()
} 