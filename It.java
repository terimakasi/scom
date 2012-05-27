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
package scom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.UUID;
import java.lang.Class;
import java.lang.Integer;
import java.lang.reflect.Constructor;

public class It implements java.util.Map.Entry<Object, Object> 
{
  public static final String CLASS_NAME = It.class.getCanonicalName();
  
  public static final String               K_NIL                    = "NIL";
  public static       It                   NIL                      = new It(K_NIL, K_NIL, K_NIL);
  
  public static final String               K_NAME                   = "name";
  public static       It                   NAME;
  
  public static final String               K_FUNCTION               = "function";
  public static final String               K_VALUE                  = "value";
  public static final String               K_CLASS                  = "class";
  public static final String               K_SUPERCLASS             = "superclass";
  public static final String               K_COUNT                  = "count";
  public static final String               K_MODEL_LAYER            = "model_layer";
  
  public static final String               V_CLASS_MODEL            = "class_model";
  public static final It                   CLASS_MODEL              = new It(V_CLASS_MODEL, V_CLASS_MODEL, K_NIL);
  
  public static final String               V_USER_MODEL             = "user_model";
  public static final It                   USER_MODEL               = new It(V_USER_MODEL, V_USER_MODEL, K_NIL);
  
  public static final String               K_NEW_F                  = "new";
  public static       It                   NEW_F;
  
  public static final String               K_METACLASS              = "Class";
  public static       It                   METACLASS                = null;
  public static       int                  _init_count              = 0;
  
  public static final String               K_OBJECT                 = "Object";
  public static       It                   OBJECT;                  // Caution: don't initialize with null
  
  public static final String               K_DOUBLE                 = "Double";
  public static       It                   DOUBLE;                  // Caution: don't initialize with null
  
  public static final String               K_INTEGER                = "Integer";
  public static       It                   INTEGER;                 // Caution: don't initialize with null
  
  public static final String               K_STRING                 = "String";
  public static       It                   STRING;                  // Caution: don't initialize with null
  
  public static final String               K_TRUE                   = "true";
  public static       It                   TRUE                     = new It(K_TRUE, K_TRUE, K_TRUE);
  
  public static final String               K_ENVIRONMENT            = "Environment";
  public static       It                   ENVIRONMENT              = GetEnvironment();
  
  public static final int                  WITHOUT_DEPTH            = -1;
  public static final int                  WITH_UNLIMITED_DEPTH     = 0;
    
  protected           HashMap<It, It>      _facet_map               = new HashMap<It, It>();
  protected           It[]                 _metadata                = new It[]{ NIL };
    
  protected           HashMap<String, It>  _kv2it                   = new HashMap<String, It>();
  protected final     Object               _key;
  protected           Object               _value                   = NIL;
  protected           Object               _next                    = NIL;
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
   
    initEnvironment_();
  } // Private Constructor
  
  static protected It GetEnvironment()
  {
    if (ENVIRONMENT==null)
      ENVIRONMENT = new It(K_ENVIRONMENT, K_ENVIRONMENT, K_NIL);
    
    return ENVIRONMENT;
  } //---- GetEnvironment
  
  protected void initEnvironment_()
  {
    if (_init_count > 1)
      return;
    
    _init_count++;
    GetEnvironment();
    
    NAME  = new It(K_NAME, K_NAME, K_NIL);
      
    NEW_F = new It(K_NEW_F, K_NEW_F, K_NIL);
    
    // http://en.wikipedia.org/wiki/Metaclass  
    METACLASS = NewNativeClass_(K_METACLASS, NIL, NIL);       // NB: don't add 'COUNT' value yet, INTEGER is required  
    
    OBJECT = NewNativeClass_(K_OBJECT, METACLASS, NIL);       // NB: don't add 'COUNT' value yet, INTEGER is required 
      OBJECT.putFacet(K_VALUE,          NIL);
    
    INTEGER = NewNativeClass_(K_INTEGER, METACLASS, OBJECT);  // NB: don't add 'COUNT' value yet, INTEGER is required 
      INTEGER.putFacet(K_VALUE,         New(0)); 
      
    STRING = NewNativeClass_(K_STRING,  METACLASS, OBJECT);    // NB: don't add 'COUNT' value yet, INTEGER is required 
      STRING.putFacet(K_VALUE,          New("")); 
    
    METACLASS.putFacet(K_COUNT,         New(3)); // NB: requires INTEGER so don't move this code up (OBJECT creation)
    OBJECT.putFacet(K_COUNT,            New(0));
    INTEGER.putFacet(K_COUNT,           New(0)); 
    STRING.putFacet(K_COUNT,            New(0));
    
    ENVIRONMENT.putFacet(K_METACLASS,   METACLASS)
               .putFacet(K_OBJECT,      OBJECT) 
               .putFacet(K_INTEGER,     INTEGER)
               .putFacet(K_STRING,      STRING); 
  } //---- InitEnvironment_
  
  
  //---------- New ----------
  // 'pseudo constructor' required to handle 'NIL' instance of It (inspired from LISP) **** 
  // this one instanciates an 'It class' (an It instance which has the required 'It class' facets)
  static public It New(It class_it) 
  {
    //System.out.println("New class(" + class_it + ")");
    if (! IsClass(class_it))  return NIL;
      
    //---- Instanciate class ----
    String instance_base_name = GetInstanceBaseName_(class_it);
      //---- increment count of class intances ----
      It      count_it       = class_it.getFacet(K_COUNT);
      Integer instance_count = CastToInteger_(count_it.getValue()) + 1;
      count_it.setValue(instance_count.toString());
      //---- increment count of class intances
    
    String instance_name = instance_base_name + instance_count;
    It instance_it = new It(instance_name, instance_name, NIL);
      instance_it.putFacet(K_CLASS, class_it);
      instance_it.putFacet(K_NAME,  NewValue(instance_name));
    //---- Instanciate class
    
      
    //----- Add facets to instance from class template -----
    HashMap<It, It> relations   = class_it.getFacets();
    Set<It>         facets      = relations.keySet();
    Iterator<It>    iterator    = facets.iterator();
    while (iterator.hasNext())
    {
      It facet = iterator.next();
      
      //System.out.print("  facet(" + class_it + "): '" + facet + "'");
      if (facet.getFacet(K_MODEL_LAYER) != CLASS_MODEL)
      {
         //System.out.println("  facet(" + class_it + "): '" + facet + "' is user model");
         instance_it.putFacet(facet, relations.get(facet));
      }
    }
    //----- Add facets from class
      
    return instance_it;
  } //---- New (instanciate 'It class')
  
  static public It New(Object key, Object value) 
  {
    return New(key, value, NIL);
  }
  static public It New(Object key, Object value, Object option) 
  {
    It item = IsNIL_(key,value);
    if (item == null)
      item = new It(key, value, option);
    return item;
  }
  static public It New(Object key, Object value, String class_name) 
  {
    return New(key, value, NIL, class_name);
  }
  static public It New(Object key, Object value, Object next, String class_name) 
  {
    It is_nil = IsNIL_(key,value);

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
        return NIL; 
      }
    }
    return NIL;
  }
  //---------- New
  
  
  //---------- NewClass ----------
  static public It NewClass(String class_name)
  {  return NewClass(class_name, METACLASS);  }
  static public It NewClass(String class_name, It metaclass_it)
  {  return NewClass(class_name, metaclass_it, OBJECT);  }
  static public It NewClass(String class_name, It metaclass_it, It super_it)
  {  
    //System.out.println("It.NewClass class_name:" + class_name + "  class:" + metaclass_it + "  super:" + super_it);
    if (! IsClass(metaclass_it))  return It.NIL;
    
    It new_class = new It(class_name, class_name, K_NIL);
      new_class.putFacet      (K_NAME,       new_class);
      new_class.putClassFacet_(K_CLASS,      metaclass_it);
      new_class.putClassFacet_(K_NEW_F,      new_class);
      new_class.putClassFacet_(K_COUNT,      New(0));
      new_class.putClassFacet_(K_SUPERCLASS, super_it);
      
      ENVIRONMENT.putFacet(class_name, new_class);
          
      //---- increment count of class intances ----
      It      count_it       = metaclass_it.getFacet(K_COUNT);
      Integer instance_count = CastToInteger_(count_it) + 1;
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
  
  
  static protected It IsNIL_(Object key, Object value)
  {
    if (key == null || value == null)     
      return NIL;
    
    String key_str   = CastToString_(key);
    String value_str = CastToString_(value);
    
    if (key_str == K_NIL && value_str == K_NIL)
      return NIL;
      
    return null;
  } //---- IsNIL_()
  
  
  private static String KeyValue_(Object key, Object value)
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
  } //---- KeyValue_
  
  
  //---------- evaluate ----------
  public It evaluate()
  {  return evaluate(NIL);  }
  
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
  static public It New(Integer value)
  { 
     It new_integer = New(INTEGER, NIL);
     new_integer.setValue(value);
     return new_integer;
  } // New (NewValue Integer)
  
  static public It New(Float value)
  {  
    //return New(value.toString(), It.NIL);  
    return New(value, value);  // ToDo: .next is not NIL
  } // New (NewValue Float)
  
  static public It New(Double value)
  {  
    //return New(value.toString(), It.NIL);
    return New(value, value);  // ToDo: .next is not NIL
  } // New (NewValue Double)
  
  static public It New(String value)
  {  
    //return NewValue(value, NIL);
    return New(value, value);  // ToDo: .next is not NIL
  } // New (NewValue String)
  
  // Caution !! Regression if replaced by New ( String)
  static private It NewValue(String value)
  {  return NewValue(value,         NIL);  }
  
  static public It NewValue(String value, Object next)
  {  return new It(K_VALUE,         value, next);  }
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

  
  //---------- putFacet ----------
  private It putClassFacet_(String facet_name, It facet_value)
  {
    It facet = NewValue(facet_name)
               .putFacet(K_MODEL_LAYER, CLASS_MODEL);
    return putFacet(facet, facet_value);
  }
  public It putFacet(String facet_name, It facet_value)
  {
    It facet = NewValue(facet_name);
    return putFacet(facet, facet_value);
  }
  public It putFacet(It facet, It facet_value)
  {  
    if (_facet_map.containsKey(facet))
    {
      It previous_facet_value = _facet_map.get(facet);
      _facet_map.remove(facet);
      _kv2it.remove(KeyValue_(facet._key, facet.getValue()));
      _kv2it.remove(KeyValue_(previous_facet_value._key, previous_facet_value.getValue()));
    }
    _facet_map.put(facet, facet_value);
    _kv2it.put(KeyValue_(facet.getValue(), this.getValue()), facet);
    
    Object o_facet_value = facet_value;
    try
    {
      It facet_value_it = (It)facet_value;
      o_facet_value = facet_value_it.getValue();
    }
    catch (Exception e) 
    {
      o_facet_value = facet_value;
    }
    
    _kv2it.put(KeyValue_(o_facet_value, this.getValue()), facet_value);
    
    return this;
  } 
  //---------- putFacet
  
  
  //---------- getFacet ----------
  public It getFacet(Object facet)
  { 
    String key_value = KeyValue_(facet, this.getValue());
    It     it        = _kv2it.get(key_value);
    return getFacet(it);
  }
  public It getFacet(It it)
  {   
    if (_facet_map.containsKey(it))
    {
      It facet = _facet_map.get(it);
      return facet;
    }
    return NIL;
  }
  //---------- getFacet
  
  
  public It getFunction(String function_name, String class_name)
  {
    It  function_it = ENVIRONMENT.getFacet(function_name);
    if (function_it == NIL)
      function_it = New(K_FUNCTION, class_name, class_name);

    return function_it;
  } //---- getFunction
  
  
  public HashMap<It, It> getFacets()
  {   
    return _facet_map;
  } //---- getFacets()
 
  
  @Override
  public String toString() { return getValue().toString(); }

  @Override
  public Object getKey()   { return _key; }
  
  
  //---------- 'Value' accessors ----------
  @Override
  public Object getValue() { return _value; }

  @Override
  public Object setValue(Object value) 
  {
    Object previous_value = _value;
    _value = value;
    return previous_value;
  }
  //---------- 'Value' accessors
  
  
  //---------- 'Next' accessors ----------
  public Object getNext()              
  { return _next; }
  
  public Object setNext(Object new_next)              
  {
    Object previous_next = _next;
    _next = new_next;
    return previous_next;
  }
  //---------- 'Next' accessors
  
  
  public String getUUID()              
  { return _uuid.toString(); }
  //---- getUUID
  
  
  static private boolean IsIt_(Object o) 
  {
    try
    {
      It o_it = (It) o;
      return true;
    }
    catch (Exception e) 
    {}
    return false;
  } //---- IsIt_
    
    
  static private Integer CastToInteger_(Object o) 
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
  } //---- CastToInteger_
  
  static private String CastToString_(Object o) 
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
  } //--- CastToString_
  
  
  static private String CamelCaseToUnderscore_(String input_str)
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
  } //---- CamelCaseToUnderscore_
  
  static private String GetInstanceBaseName_(It class_it)
  {
    String   instance_base_name = class_it.getValue().toString();
    String[] words              = instance_base_name.split("(?<=[a-z])(?=[A-Z])");
    instance_base_name          = CamelCaseToUnderscore_(instance_base_name); 
    return instance_base_name;
  } //---- GetInstanceBaseName_
    
  static private String NewInstanceName_(It class_it)
  {
    String   instance_base_name = GetInstanceBaseName_(class_it);
    
    It      count_it       = class_it.getFacet(K_COUNT);
    if (count_it != NIL)
    {
      Integer instance_count = CastToInteger_(count_it.getValue()) + 1;
      return instance_base_name + instance_count;
    }
   else
      return instance_base_name + "_" + UUID.randomUUID().toString();
  } //---- NewInstanceName_
  
  static private It NewNativeClass_(String class_name, It class_it, It superclass_it)
  {
    It native_class = It.NIL;
    native_class = new It(class_name, class_name, K_NIL);
    if (class_name.equals(K_METACLASS))
      native_class.putClassFacet_(K_CLASS,      native_class);
    else
      native_class.putClassFacet_(K_CLASS,      class_it); 
    
    native_class.putFacet(NAME,                 native_class);
    native_class.putClassFacet_(K_NEW_F,        native_class); 
    
    if (superclass_it == It.NIL)
      native_class.putClassFacet_(K_SUPERCLASS, native_class);
    else
      native_class.putClassFacet_(K_SUPERCLASS, superclass_it);
    
    return native_class;
  } //---- NewNativeClass_
  
  static public String Repeat(String str, int times)
  {
    StringBuilder ret = new StringBuilder();
    for(int i = 0;i < times;i++) ret.append(str);
    return ret.toString();
  } //---- Repeat
  
  //---------- Print ----------
  static public void Print(String message)
  {  
    System.out.println(message);
  } //---- Print (String)
  
  static public void Print(It item)
  {
    Print(item, WITHOUT_DEPTH);
  }
  static public void Print(It item, int depth)
  { 
    Print(item, depth, ""); 
  }
  static public void Print(It item, int depth, String indent_title)
  { 
    String suffix = "";
    if (depth != WITHOUT_DEPTH)
      suffix = ":";
    
    System.out.println(indent_title + item + suffix);
      
    HashMap<It, It> facet_map  = item.getFacets();
    Set<It>         facets     = facet_map.keySet();
    Iterator<It>    iterator   = facets.iterator();
    while (iterator.hasNext())
    {
      It     facet       = iterator.next();
      It     facet_value = facet_map.get(facet);
      
      if (facet_value != null)
      {
        System.out.println(indent_title + "  " + facet.getValue() + ": " + facet_value.getValue());
      
        if (depth>=0 && IsIt_(facet_value) && facet_value.getFacet(K_CLASS) != NIL)
          if (facet_value != item) // to avoid infinite metaclass recursion: class(Class) = Class
            Print((It)facet_value, depth + 1, indent_title + "    ");
      }
    }
  } //---- Print (It)
  //---------- Print
} //---------- It
