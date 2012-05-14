/**
 * ItLispParseF.java
 * REPL: Parse function
 * http://liquicode.com/wordpress/dev/259/tokenizer-a-class-for-the-lexical-analysis-of-text
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

import java.util.ArrayList;
import java.util.StringTokenizer;
import scom.It;
import scom.samples.lang.lisp.*;

public class ItLispParseF extends It
{
  public static final String CLASS_NAME           = "scom.samples.lang.lisp.repl.ItLispParseF";
  
  public static final String SEPARATOR_OPEN_LIST  = "(";
  public static final String SEPARATOR_CLOSE_LIST = ")";
  public static final String SEPARATOR_STRING     = "\"";
  
  // NB: don't add "0x0b" in WhiteSpace separators as it breaks Integer parsing
  public static final String SEPARATOR_WHITESPACE = " \t\n\f\r"; 
  
  public static final String SEPARATORS = 
         SEPARATOR_WHITESPACE + SEPARATOR_OPEN_LIST + SEPARATOR_CLOSE_LIST + SEPARATOR_STRING;
          
  protected ItLispParseF(Object key, Object value, Object option) 
  {
    super(key, value, option);
  } // Private Constructor
   
  @Override
  public It evaluate(ArrayList<It> input)
  {         
    if (input.size()==0) return It.NIL;
    
    String          arg1   = input.get(0).toString();
    //System.out.println("  arg1:" + arg1);
    StringTokenizer st     = new StringTokenizer(arg1, SEPARATORS, true);
    ArrayList       tokens = new ArrayList();
    while (st.hasMoreTokens()) 
    {
      String token_str = st.nextToken();
      tokens.add(token_str);
    }
     
    //---- surround input with parenthesis if not the case
    if (tokens.size()>1)
      if (! tokens.get(0).equals(SEPARATOR_OPEN_LIST))
        tokens.add(0, SEPARATOR_OPEN_LIST);
     
    if (tokens.size()>2)
      if (! tokens.get(tokens.size()-1).equals(SEPARATOR_CLOSE_LIST))
        tokens.add(SEPARATOR_CLOSE_LIST);
    //----
     
    String output_str = "";
    for (int i=0; i < tokens.size(); i++) 
    {
      String token_str  = tokens.get(i).toString().trim();
      String token_type = getTokenType(token_str);
      It     token_it   = New(It.K_VALUE, token_str);
      
      if (   token_type==ItLisp.K_WHITESPACE 
          || token_type==ItLisp.K_OPEN_LIST || token_type==ItLisp.K_CLOSE_LIST)
        output_str += token_str;
      else
        output_str += token_type +":" + token_str;
        
      if (tokens.size()>1 && i<(tokens.size()-1))
        output_str += " ";
    }
    
    setValue(output_str);
    return this;
  } //---- evaluate() 
  
  // http://www.java2s.com/Tutorial/SCJP/0160__Utility-Classes/EachJavaprimitivedatatypehasacorrespondingwrapperclass.htm
  private String getTokenType(String token_str)
  {
    String token_type = ItLisp.K_UNKNOWN_TYPE;
    
    //---- 1. Check first if token value is NIL
    if (   token_str.equals(It.NIL_KV)
        || token_str.toUpperCase().equals("F"))
      token_type = It.NIL_KV;
    //----
    
    //---- 2. Check if token is WhiteSpace:
    //        - ' ', \n, \r, \r, 0x0b
    if (SEPARATOR_WHITESPACE.indexOf(token_str)!= -1)
      token_type = ItLisp.K_WHITESPACE;
    //----
    
    //---- 3. Check  if token value is either:
    //        - ( : open list
    //        - ) : close list
    //        - " : string delimiter
    if (token_str.equals(SEPARATOR_OPEN_LIST))
      token_type = SEPARATOR_OPEN_LIST;
    
    if (token_str.equals(SEPARATOR_CLOSE_LIST))
      token_type = SEPARATOR_CLOSE_LIST;
    
    if (token_str.equals(SEPARATOR_STRING))
      token_type = SEPARATOR_STRING;
    //----
    
    //---- 4. Check if token value is an Integer
    try
    {
      Integer value = Integer.valueOf(token_str);
      token_type = ItLisp.K_INTEGER_TYPE;
    }
    catch (Exception e) {}
    //----
    
    //---- 5. Check if token value is a Double/Float/Real
    if (token_type.equals(ItLisp.K_UNKNOWN_TYPE))
    try
    {
      Double value = Double.valueOf(token_str);
      token_type   = ItLisp.K_DOUBLE_TYPE;
    }
    catch (Exception e) {}
    //----
    
    //---- 6. Check if token value is a known Symbol in ENVIRONMENT
    if (ENVIRONMENT.getIt(token_str) != It.NIL)
      token_type = ItLisp.K_SYMBOL_TYPE;
    //----
    
    return token_type;
  } //---- getTokenType
} //---------- ItLispParseF