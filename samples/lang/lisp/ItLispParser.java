/**
 * ItLispParseF.java
 * REPL: Parser
 * http://liquicode.com/wordpress/dev/259/tokenizer-a-class-for-the-lexical-analysis-of-text
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

public class ItLispParser extends It
{
  public static final String CLASS_NAME           = "scom.samples.lang.lisp.ItLispParser";
  
  public static final String SEPARATOR_OPEN_LIST  = "(";
  public static final String SEPARATOR_CLOSE_LIST = ")";
  public static final String SEPARATOR_STRING     = "\"";
  public static final String SEPARATOR_QUOTE      = "'";
  
  // NB: don't add "0x0b" in WhiteSpace separators as it breaks Integer parsing
  public static final String SEPARATOR_WHITESPACE = " \t\n\f\r"; 
  
  public static final String SEPARATORS = 
         SEPARATOR_WHITESPACE + SEPARATOR_OPEN_LIST + SEPARATOR_CLOSE_LIST + SEPARATOR_STRING + SEPARATOR_QUOTE;
          
  protected ItLispParser(Object key, Object value, Object next) 
  {
    super(key, value, next);
  } // Private Constructor
   
  @Override
  //--------------- Parser ---------------
  public It evaluate(ArrayList<It> input)
  {         
    if (input.size()==0) return It.NIL;
    
    String arg = input.get(0).toString();
    
    It lexer = ENVIRONMENT.getFunction("lexer", ItLispLexer.CLASS_NAME);
    lexer.evaluate(It.ToList(arg));
    
    ArrayList<It> tokens = (ArrayList<It>)lexer.getValue();

    String output_str = "";
    for (int i=0; i < tokens.size(); i++) 
    {
      It token = tokens.get(i);
      String token_str  = token.getValue().toString().trim();
      String token_type = token.getNext().toString();
      
      if (     token_type==ItLisp.K_WHITESPACE 
            || token_type==ItLisp.K_OPEN_LIST || token_type==ItLisp.K_CLOSE_LIST)
        output_str += token_str;
      else
        output_str += token_type + ":" + token_str;
        
      if (tokens.size()>1 && i<(tokens.size()-1))
        output_str += " ";
    }
    
    setValue(output_str);
    
    return this;
  } //---- evaluate() 
} //---------- ItLispParser