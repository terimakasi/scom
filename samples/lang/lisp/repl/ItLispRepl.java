/**
 * ItLispRepl.java
 * REPL: Read-Eval-Print-Loop
 * http://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 8 may 2012 - 16:34
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
package scom.samples.lang.lisp.repl;

import java.io.*;
import java.util.ArrayList;
import scom.It;
import scom.samples.lang.lisp.functions.*;

public class ItLispRepl extends It
{
  public static final String CLASS_NAME = "scom.samples.lang.lisp.repl.ItLispRepl";
  public static final String K_PARSE    = "parse";
  private BufferedReader     _reader    = new BufferedReader(new InputStreamReader(System.in));
  private boolean            _forever   = true;
          
  protected ItLispRepl(Object key, Object value, Object option) 
  {
    super(key, value, option);
  } // Private Constructor
  
  //---- Initialize Lisp Environment
  protected void init()
  {
    It parse_f = ENVIRONMENT.getFunction(ItLispParseF.CLASS_NAME);
    System.out.println("ScomRepl.init  parse_f:" + parse_f);
    connect(K_PARSE, parse_f);
      
    ENVIRONMENT.connect(ItLispCarF.NAME,  ENVIRONMENT.getFunction(ItLispCarF.CLASS_NAME));
    ENVIRONMENT.connect(ItLispCdrF.NAME,  ENVIRONMENT.getFunction(ItLispCdrF.CLASS_NAME));
    ENVIRONMENT.connect(ItLispConsF.NAME, ENVIRONMENT.getFunction(ItLispConsF.CLASS_NAME));
    
    It quote_f = ENVIRONMENT.getFunction(ItLispQuoteF.CLASS_NAME);
    ENVIRONMENT.connect(ItLispQuoteF.NAME, quote_f);
    ENVIRONMENT.connect("'",               quote_f);
  } //---- init
  
  @Override
  public It evaluate(ArrayList<It> input)
  { 
    boolean loop_enabled = true;
              
    while (loop_enabled)
    {
      System.out.print("> ");
      String input_str = "";
      
      if (input!= null && input.size()>0)
      { 
        loop_enabled = false;
        input_str = input.get(0).toString();
        System.out.println(input_str);
      }
      else
      {
        try
        {
          input_str = _reader.readLine();
        }
        catch (Exception e) {}
      }
      
      It output = getIt(K_PARSE).evaluate(It.asList(input_str));
      System.out.println("  " + output.getValue());
    }
    
    return It.NIL;
  } //---- evaluate() 
} //---------- ItLispRepl