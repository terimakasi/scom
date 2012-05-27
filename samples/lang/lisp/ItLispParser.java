/**
 * ItLispParseF.java
 * REPL: Parser
 * http://liquicode.com/wordpress/dev/259/tokenizer-a-class-for-the-lexical-analysis-of-text
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
package scom.samples.lang.lisp;

import java.util.ArrayList;
import scom.It;

public class ItLispParser extends It
{
  public static final String CLASS_NAME = ItLispParser.class.getCanonicalName();
  
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
  //--------------- Parser's evaluate() ---------------
  public It evaluate(ArrayList<It> input)
  {         
    if (input.size()==0) return It.NIL;
    
    String arg = input.get(0).toString();
    
    It lexer = ENVIRONMENT.getFunction("lexer", ItLispLexer.CLASS_NAME);
    lexer.evaluate(It.ToList(arg));
    
    ArrayList<It> tokens = (ArrayList<It>)lexer.getValue();
    ArrayList<It> parse_tree = buildParseTree(tokens, null, 0);
    
    setValue(tokens);
    
    return this;
  } //---- Parser's evaluate() 
  
  private ArrayList<It> buildParseTree(ArrayList<It> tokens, ArrayList<It> parse_tree, int index)
  {
    //System.out.println("ItLispParser.buildParseTree index:" + index);
    
    if (parse_tree==null)
      parse_tree = new ArrayList<It>();
    
    for (int i=index; i < tokens.size(); i++) 
    {
      It     token      = tokens.get(i);
      String token_str  = token.getValue().toString().trim();
      String token_type = token.getNext().toString();
      
      boolean is_whitespace = token_type.equals(ItLisp.K_WHITESPACE);
      
     
      if (token_type.equals(SEPARATOR_OPEN_LIST))
      {
        is_whitespace = true; 
      }
      else if (token_type.equals(SEPARATOR_OPEN_LIST))
      {
        parse_tree = buildParseTree(tokens, parse_tree, ++i);
      }
      else if (token_type.equals(SEPARATOR_CLOSE_LIST))
      {
        return parse_tree;
      }
      else if (token_type.equals(ItLisp.K_INTEGER_TYPE))
      {
        It new_integer = New(INTEGER);
        new_integer.setValue(token_str);
        
        parse_tree.add(new_integer);
      }
      else if (token_type.equals(ItLisp.K_SYMBOL_TYPE))
      {
        It new_symbol = ENVIRONMENT.getFacet(token_str);        
        parse_tree.add(new_symbol);
      }
      //System.out.println("  parse_tree : " + parse_tree);
    } // for each token
    return parse_tree;
  } //---- buildParseTree
  
  @Override
  public String toString() 
  {   
    String        output_str = K_NIL;
    ArrayList<It> tokens;
    try                  { tokens = (ArrayList<It>)getValue(); }
    catch (Exception e)  { return K_NIL; }
    
    output_str = "";
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
    return output_str;
  } // toString
} //---------- ItLispParser