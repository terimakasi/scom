/**
 * It.java
 * Core and Single class of SCOM project
 * NB: Shortest name because 
 *     1. used widely by client code for it's constants (e.g: It.NIL)
 *     2. used as a prefix for class names (like in 'hungarian notation')
 *        e.g: ItCons class in List interpreter sample
 * NB: originally 'ScomIt', a mix between 'Scom' and 'comit' (The first string-handling and pattern-matching language)
 *     see: http://en.wikipedia.org/wiki/COMIT
 * ...................................................................................
 * SCOM: Single Class Object Model (http://code.google.com/p/scom/)
 * Licence: MIT (http://en.wikipedia.org/wiki/MIT_License)
 * Michel Kern - 20 may 2012 - 22:31
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
package scom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.UUID;
import java.lang.Class;
import java.lang.Integer;
import java.lang.reflect.Constructor;
import scom.samples.lang.lisp.functions.ItLispCarF;
import scom.samples.lang.lisp.functions.ItLispCdrF;

public class It implements java.util.Map.Entry<Object, Object> 
{
  public final String CLASS_NAME = "scom.It";  
  
  public static final String               K_NAME                   = "name";
  public static final String               K_FUNCTION               = "function";
  public static final String               K_VALUE                  = "value";
  public static final String               K_CLASS                  = "class";
  public static final String               K_SUPERCLASS             = "superclass";
  public static final String               K_COUNT                  = "count";
  public static final String               K_NEW_F                  = "new";
  
  public static final String               K_METACLASS              = "Class";
  public static       It                   METACLASS                = null;
  public static       int                  _init_count              = 0;
  
  public static final String               K_NIL                    = "NIL";
  public static       It                   NIL                      = new It(K_NIL, K_NIL, K_NIL);
  
  public static final String               K_OBJECT                 = "Object";
  public static       It                   OBJECT;                  // Caution: don't initialize with null
  
  public static final String               K_INTEGER                = "Integer";
  public static       It                   INTEGER;                 // Caution: don't initialize with null
  
  public static final String               K_TRUE                   = "true";
  public static       It                   TRUE                     = new It(K_TRUE, K_TRUE, K_TRUE);
  
  public static final String               K_ENVIRONMENT            = "Environment";
  public static       It                   ENVIRONMENT              = GetEnvironment();
    
  protected           HashMap<It, It>      _facets                  = new HashMap<It, It>();
  protected           HashMap<String, It>  _kv2it                   = new HashMap<String, It>();
  protected final     Object               _key;
  protected           Object               _value                   = It.NIL;
  protected           Object               _next                    = It.NIL;
  protected final     UUID                 _uuid                    = UUID.randomUUID();
  
  //*** Private Constructor: API clients must use factory method 'New' instead
  // Usage: if provided key is "" it's value will be instance's UUID
  protected It(Object key, Object value, Object next) 
  {
    if (key.toString().equals(""))
      _key = _uuid.toString();
    else
      _key  = key;
    
    _value  = value;
    _next   = next;
   
    //if (_init_count<1)
    init_environment();
  } // Private Constructor
  
  static protected It GetEnvironment()
  {
    //System.out.println("It.GetEnvironment " + ENVIRONMENT);
    if (ENVIRONMENT==null)
      ENVIRONMENT = new It(K_ENVIRONMENT, K_ENVIRONMENT, K_NIL);
    
    return ENVIRONMENT;
  } //---- GetEnvironment
  
  protected void init_environment()
  {
    if (_init_count > 1)
      return;
    
    _init_count++;
    GetEnvironment();
    
    // http://en.wikipedia.org/wiki/Metaclass  
    METACLASS = new It(K_METACLASS, K_METACLASS, K_NIL);
      METACLASS.addFacet(K_CLASS, METACLASS);
      METACLASS.addFacet(K_NEW_F, METACLASS);
      METACLASS.addFacet(K_COUNT, NewValue("1"));
      METACLASS.addFacet(K_SUPERCLASS, METACLASS);
    
    OBJECT = new It(K_OBJECT, K_OBJECT, K_NIL);
      OBJECT.addFacet(K_CLASS, METACLASS);
      OBJECT.addFacet(K_NEW_F, OBJECT);
      OBJECT.addFacet(K_COUNT, NewValue("0"));
      OBJECT.addFacet(K_SUPERCLASS, OBJECT); // Caution: Bug if super is It.NIL
      
    INTEGER = new It(K_INTEGER, K_INTEGER, K_NIL);
      INTEGER.addFacet(K_CLASS, METACLASS);
      INTEGER.addFacet(K_NEW_F, INTEGER);
      INTEGER.addFacet(K_COUNT, NewValue("0"));
      INTEGER.addFacet(K_SUPERCLASS, OBJECT); // Caution: Bug if super is It.NIL
    
    METACLASS.addFacet(K_COUNT, NewValue("2")); // METACLASS & OBJECT
  
    ENVIRONMENT.addFacet(K_METACLASS, METACLASS);
    ENVIRONMENT.addFacet(K_OBJECT,    OBJECT); 
    ENVIRONMENT.addFacet(K_INTEGER,   INTEGER); 
  } //---- init_environment
  
  
  //---------- New ----------
  // 'pseudo constructor' required to handle 'NIL' instance of It (inspired from LISP) **** 
  // this one instanciates an 'It class' (an It instance which has the required 'It class' facets)
  static public It New(It class_it) 
  {
    //System.out.println("New class(" + class_it + ")");
    //----- Check if 'class_it' is a class -----
    if (! IsClass(class_it))  return It.NIL;
    //----- Check if 'class_it' is a class

    
    //----- instance_name -----
    String   class_name = class_it.getValue().toString();
    String[] words      = class_name.split("(?<=[a-z])(?=[A-Z])");
    class_name          = CamelCaseToUnderscore(class_name);    
    //----- instance_name
    
    
    //---- increment count of class intances ----
    It      count_it       = class_it.getFacet(K_COUNT);
    Integer instance_count = CastToInteger(count_it.getValue()) + 1;
    count_it.setValue(instance_count.toString());
    //---- increment count of class intances

    
    //---- Instanciate class ----
    String instance_name = class_name + instance_count;
    It instance_it = new It(instance_name, class_name + instance_count, It.NIL);
      instance_it.addFacet(It.K_CLASS, class_it);
    //---- Instanciate class
    
      
    //----- Add facets to instance from class template -----
    HashMap<It, It> connections = class_it.getFacets();
    Set<It>         keys        = connections.keySet();
    Iterator<It>    iterator    = keys.iterator();
    while (iterator.hasNext())
    {
      It key = iterator.next();
      //System.out.println("  facet.key:" + key);
      
      /*
       * Caution: buggy !! side effect on class_it
      if (key.toString().equals(K_CLASS))
        instance_it.connect(key, class_it);

      else
        instance_it.connect(key, connections.get(key));
        */
    }
    //----- Add facets from class
      
    return instance_it;
  } //---- New (instanciate 'It class')
  
  static public It New(Object key, Object value) 
  {
    return New(key, value, It.NIL);
  }
  static public It New(Object key, Object value, Object option) 
  {
    It item = isNIL(key,value);
    if (item == null)
      item = new It(key, value, option);
    return item;
  }
  static public It New(Object key, Object value, String class_name) 
  {
    return New(key, value, It.NIL, class_name);
  }
  static public It New(Object key, Object value, Object next, String class_name) 
  {
    It is_nil = isNIL(key,value);

    if (is_nil == null)
    {
      try
      {
        Class       cls         = Class.forName(class_name);
        Constructor constructor = cls.getDeclaredConstructor(new Class[]{Object.class, Object.class, Object.class});
          constructor.setAccessible(true);
        return (It) constructor.newInstance(new Object[]{key, value, next});
      }
      catch(Exception e)
      {  
        System.out.println("*** Exception in It.New *** " + e.getMessage());
        return It.NIL; 
      }
    }
    return It.NIL;
  }
  //---------- New
  
  
  //---------- NewClass ----------
  static public It NewClass(String class_name)
  {  
    return NewClass(class_name, METACLASS);
  }
  static public It NewClass(String class_name, It metaclass_it)
  {  
    return NewClass(class_name, metaclass_it, OBJECT);
  }
  static public It NewClass(String class_name, It metaclass_it, It super_it)
  {  
    //System.out.println("It.NewClass class_name:" + class_name + "  class:" + metaclass_it + "  super:" + super_it);
    if (! IsClass(metaclass_it))  return It.NIL;
    
    It new_class = new It(class_name, class_name, K_NIL);
      new_class.addFacet(K_CLASS, metaclass_it);
      new_class.addFacet(K_NEW_F, new_class);
      new_class.addFacet(K_COUNT, NewValue("0"));
      new_class.addFacet(K_SUPERCLASS, super_it);
          
      //---- increment count of class intances ----
      It      count_it       = metaclass_it.getFacet(K_COUNT);
      Integer instance_count = CastToInteger(count_it) + 1;
      count_it.setValue(instance_count.toString());
      //---- increment count of class intances
    return new_class;
  } //---- NewClass
  
  
  static public boolean IsClass(It o_it)
  {
    if (o_it == null)             return false;
    if (o_it == It.NIL)           return false;
    
    It  metaclass_it = o_it.getFacet(K_CLASS);
    if (metaclass_it == It.NIL)   return false;
    
    It  count_it = o_it.getFacet(K_COUNT);
    if (count_it == It.NIL)       return false;
    
    It  new_f_it = o_it.getFacet(K_NEW_F);
    if (new_f_it == It.NIL)       return false;
    
    It  superclass_it = o_it.getFacet(K_SUPERCLASS);
    if (superclass_it == It.NIL)  return false;
    
    return true;
  } //---- IsClass 
  
  
  static protected It isNIL(Object key, Object value)
  {
    if (key == null || value == null)     
      return It.NIL;
    
    String key_str   = CastToString(key);
    String value_str = CastToString(value);
    
    if (key_str == It.K_NIL && value_str == It.K_NIL)
      return It.NIL;
      
    return null;
  } //---- isNIL()
  
  
  private static String KeyValue(Object key, Object value)
  { 
    //System.out.println("It.KeyValue key:" + key + " value:" + value);
    String kv = "[" + K_NIL + "," + K_NIL + "]";
    try
    {
      kv = "[" + key.toString() + "," + value.toString() + "]";
    }
    catch (Exception e)
    {}
    
    return kv;
  } //---- KeyValue
 
  
  //---------- connect ----------
  public void addFacet(String relation_name, It target)
  {
    //It relation = New(It.K_NAME, relation_name);
    It relation = NewValue(relation_name);
    addFacet(relation, target);
  }
  public void addFacet(It relation, It facet)
  {  
    //System.out.println("> connect relation=" + relation.getValue() + " target=" + target.getValue());
    if (_facets.containsKey(relation))
    {
      It previous_target = _facets.get(relation);
      _facets.remove(relation);
      _kv2it.remove(KeyValue(relation._key, relation.getValue()));
      _kv2it.remove(KeyValue(previous_target._key, previous_target.getValue()));
    }
    _facets.put(relation, facet);
    _kv2it.put(KeyValue(relation.getValue(), this.getValue()), relation);
    
    //System.out.println("It.connect target: " + target);
    //System.out.println("It.connect this.getValue(): " + this.getValue());
    
    Object facet_value = facet;
    try
    {
      It target_it = (It)facet;
      facet_value = facet.getValue();
    }
    catch (Exception e) 
    {
      //System.out.println("It.connect exception: " + e.getMessage());
      facet_value = facet;
    }
    //System.out.println("It.connect target_value: " + target_value);
    
    _kv2it.put(KeyValue(facet_value, this.getValue()), facet);
  } 
  //---------- addFacet
  
  
  //---------- evaluate ----------
  public It evaluate()
  {   
    return evaluate(It.NIL);
  }
  public It evaluate(It arg)
  {   
    ArrayList<It> it_list = new ArrayList<It>();
    it_list.add(arg);
    return evaluate(it_list);
  }
  public It evaluate(ArrayList<It> arg_list)
  { 
    if (IsClass(this))
      return New(this);
    return this;
  }
  //---------- evaluate
  
  
  //---------- NewValue ----------
  static public It NewValue(String value)
  {
    return NewValue(value, It.NIL);
  }
  static public It NewValue(String value, Object next)
  {
    return new It(It.K_VALUE, value, next);
  }
  //---------- NewValue
  
  
  //---------- ToList ----------
  static public ArrayList<It> ToList(Object arg)
  {
    return ToList(new Object[]{arg});
  }
  static public ArrayList<It> ToList(Object[] args)
  {
     ArrayList<It> arg_list= new ArrayList<It>();
     for (int i=0; i<args.length; i++)
     {
       It item = null;
       try
       {
         item = (It) args[i];
       }
       catch (Exception e) {}
       
       if (item == null)
         arg_list.add(It.New(It.K_VALUE, args[i]));
       else
         arg_list.add(item);
     }
     return arg_list;
  }
  //---------- ToList

  
  //---------- getFacet ----------
  public It getFacet(Object relation)
  { 
    String key_value = KeyValue(relation, this.getValue());
    It     it        = _kv2it.get(key_value);
    return getFacet(it);
  }
  public It getFacet(It it)
  {   
    if (_facets.containsKey(it))
    {
      It relation = _facets.get(it);
      return relation;
    }
    return It.NIL;
  }
  //---------- getFacet
  
  
  public It getFunction(String function_name, String class_name)
  {
    It  function_it = ENVIRONMENT.getFacet(function_name);
    if (function_it == It.NIL)
      function_it = It.New(It.K_FUNCTION, class_name, class_name);

    return function_it;
  } //---- getFunction
  
  
  public HashMap<It, It> getFacets()
  {   
    return _facets;
  } //---- getFacets()
  
  @Override
  public String toString() 
  { return getValue().toString(); }

  @Override
  public Object getKey()   { return _key; }
  
  
  //---------- Value accessors ----------
  @Override
  public Object getValue() { return _value; }

  @Override
  public Object setValue(Object new_value) 
  {
    Object previous_value = _value;
    _value = new_value;
    return previous_value;
  }
  //---------- Value accessors
  
  
  //---------- Next accessors ----------
  public Object getNext()              
  { return _next; }
  
  public Object setNext(Object new_next)              
  {
    Object previous_next = _next;
    _next = new_next;
    return previous_next;
  }
  //---------- Next accessors
  
  
  public String getUUID()              
  { return _uuid.toString(); }
  
  static private Integer CastToInteger(Object o) 
  {
    Integer o_int = 0;
    try
    {
      String o_str = (String) o;
      o_int = Integer.parseInt(o_str);
    }
    catch (Exception e) 
    {}
    return o_int;
  } // CastToInteger
  
  static private String CastToString(Object o) 
  {
    String o_str = "";
    try
    {
      It o_it = (It) o;
      if (o_it != null)
        o_str = o_it.getKey().toString();
    }
    catch (Exception e) 
    {
      o_str = o.toString().toUpperCase();
    }
    return o_str;
  } // CastToString
  
  
  static private String CamelCaseToUnderscore(String input_str)
  {
    String[] words    = input_str.split("(?<=[a-z])(?=[A-Z])");
    String output_str = "";
    for (int i=0; i < words.length; i++)
    {
      output_str += words[i];
      if (i+1 < words.length)
        output_str += "_";
    }
    return output_str.toLowerCase();
  } //---- CamelCaseToUnderscore
} //---------- It
