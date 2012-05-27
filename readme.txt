--------------------------------------------------------------
              SCOM: Single Class Object Model  
              ------------------------------
              ----- Programmer's Guide -----
              ------------------------------
            www.terimakasi.com    27th May 2012
            https://github.com/terimakasi/scom
--------------------------------------------------------------
1. Introduction
---------------
  SCOM is a exercise in style with a 'constructivist' motivation. The purpose is to design an Model Objet provided 
  as a Single class (!!), called 'It' (core/root class). 

  SCOM paradigm is founded on top of 3 'Design Intents':

  a. Runtime Semantic Network
  ---------------------------
     SCOM provides an easy and straightforward way to creatr 'semantic networks' at runtime (much like XML DOM)
     The atoms of SCOM semantic networks are RDF-like triples (Subject --Predicate--> Object). 
     In SCOM, all 3 parts of the triple are 'It' instances
     - 'Subject':   is an It instance 
     - 'Predicate': is an unidirectional relation, it is called a 'facet'
     - 'Object':    is called a 'facet value' 

  b. Self evaluation instances 
  ----------------------------
     Inspired by functional programming (especially 'Lisp'). Thus Application behavior is implemented:
     - by overriding 'evaluate' method in subclasses of 'It'
     - once a subclass implements a method, it could then be added to an It instance as a 'behavioral facet'

  c. Adaptative Object Model
  -----------------------------------------
     SCOM supports the AOM paradigm (Adaptative Object Model), where business domain classes are not static like
     'regular OOP languages' (e.g: Java, C#/C++) but instead dynamically created at runtime (much like
     classes described by Semantic Web ontology language: 'Owl')

2. Guidelines
---------------
  All samples/apps built on top of SCOL must comply to the following rules:
    - All implementation classes (non taking into account unit/functional tests) must be subclasses of 'It' class
    - subclasses should not add instance methods to the API (else it wull break SCOM paradigm), thus are only 
      allowed to override:
      - 'evaluate()' and optionally 'toString()'
      - constructor: in fact it's a requirement, due to the instanciation of 'It' through a static factory method ('New()')  
        note: SCOM is 'null safe', this is implemented by a native singleton ('NIL') which requires a factory method
    - subclasses should not add static/class methods
    - subclasses must override 'NAME' and 'CLASS_NAME' static fields

  Native Object Model: 'ENVIRONMENT'
  ----------------------------------
  'It' core class provides a native Object Model ('ENVIRONMENT') which is a Singleton populated by 'initEnvironment()'
  note: application may extend 'ENVIRONMENT' by overriding 'init_environment()' (see: 'ItLisp' in com.samples.lang.lisp)

2. License
---------------
  This project is Open Source, under 'MIT license' (http://en.wikipedia.org/wiki/MIT_License)

3. Samples
---------------

4. To Do
--------------------
- Quality:
  - Unit tests & Automatic Tests
  - count It instances and avoid unnecessary duplication of Literals/Constants (e.g: NEW_F)
- Semantic Network:
  - RDF serialization, may be used to specify native Object Model
  - Graph Layout (with D3 Javascript framework)
- Languages:
  - Implementation of Lisp interpreter
  - Encapsulation of Java embedded interpreter

Appendix
--------
  A.1. Naming conventions
  -----------------------
  - All static methods start with an Upper case letter then CamelCase (e.g: 'NewClass()')
    - private/protected methods have a additional '_' suffix (e.g: 'IsNIL_')
  - instance methods start with a Lower case letter then CamelCase (e.g: 'getKey()')
    - private/protected methods have a additional '_' suffix (e.g: 'initEnvironment_')
  - Subclasses of It should be prefixed with 'It' (e.g: ItLisp)
    - those which are meant to be a 'method facet' of an It instance should be suffixed with 'F' (e.g: 'ItSuccessorF')
  - variables and methods parameters should use the 'underscore' naming convention (eg: 'class_it')
  - variables which are 'It' instances should be suffixed with '_it' (eg: 'class_it')
  - variables which are used for typecasting (e.g: Object to String) should be suffixed with '_str' (eg: 'o_str')