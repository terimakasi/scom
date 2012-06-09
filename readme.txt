--------------------------------------------------------------
              SCOM: Single Class Object Model  
              ------------------------------
              ----- Programmer's Guide -----
              ------------------------------
        www.terimakasi.com    10 june 2012 - 01:32
            https://github.com/terimakasi/scom
--------------------------------------------------------------

Table of Contents
-----------------
  1. Introduction
  2. Getting Started
  3. Interactive Shell
  4. Guidelines
  5. License
  6. Samples
  7. To Do
  A. Appendix

1. Introduction
---------------
  SCOM is a exercise in style with a 'constructivist' motivation. The purpose 
  is to design an Model Objet provided as a Single class (!!), called 'It' 
  (core/root class). 

  SCOM paradigm is founded on top of 3 'Design Intents':

  a. Runtime Semantic Network
  ---------------------------
     SCOM provides an easy and straightforward way to creatr 'semantic networks' 
     at runtime (much like XML DOM)
     The atoms of SCOM semantic networks are RDF-like triples 
     (Subject --Predicate--> Object). 
     In SCOM, all 3 parts of the triple are 'It' instances
     - 'Subject':   is an It instance 
     - 'Predicate': is an unidirectional relation, it is called a 'facet'
     - 'Object':    is called a 'facet value' 

  b. Self evaluation instances 
  ----------------------------
     Inspired by functional programming (especially 'Lisp'). Thus Application 
     behavior is implemented:
     - by overriding 'evaluate' method in subclasses of 'It'
     - once a subclass implements a method, it could then be added to an It 
       instance as a 'behavioral facet'

  c. Native Adaptative Object Model
  ---------------------------------
     SCOM supports the AOM paradigm (Adaptative Object Model), where business 
     domain classes are not static like 'regular OOP languages' (e.g: Java, 
     C#/C++) but instead dynamically created at runtime (much like classes 
     described by Semantic Web Ontology language: 'Owl')

2. Getting Started
------------------
  a. Create an It instance by calling 'New()' factory method:
     It item = It.New();

  b. Then add a 'VALUE' facets (a facet is like similar an attributes or 
     property):
     e.g: item.putFacet(VALUE, New("Hello World !")); 

  c. Then you can use 'Print()' helper method to show instance's value and facets:
     e.g: Print(item);

  d. Now test this code by running 'HelloWorld.java' 
     (in scom.tutorials.helloworld sample), the output should be:
     **** SCOM: HelloWorld sample ****
     object1
      name: object1
      value: Hello World !
      class: Object

     Explanation: here we see that we have created an It instance which is 
     also an instance of 'Object' class in SCOM native AOM (Adaptative Object 
     Model), this instance has 3 facets:
                  - value = 'Hello World !'
                  - class = 'Object'
                  - name  = 'object1'

  e. Now we may uncomment the following line (in 'HelloWorld.java'):
     //Print(item, WITH_UNLIMITED_DEPTH);

     This will print facets of item's facets recursively: 

       object1:
         value: Hello World !
         class: Object
           Object:
             class: Class
               Class:
                 superclass: Class
                 count: 3
                 new: Class
                 class: Class
                 name: Class
             count: 1
             value: NIL
             superclass: Object
             new: Object
             name: Object
         name: object1

     Explanation: here we see that 'class' facet which is 'Object' has also
                  its own facets:
                  note: 'class' of 'Object' is 'Class' which is the metaclass 
                        of SCOM native AOM (Adaptative Object Model)
                        'Object' has also a 'superclass' (which is itself)
       Object:
         class: Class
           Class:
             superclass: Class
             count: 3
             new: Class
             class: Class
             name: Class
         count: 1
         value: NIL
         superclass: Object
         new: Object
         name: Object

3. Interactive Shell
--------------------
   SCOM embeds an Interactive Command Shell. This Shell allows to create 
   instances and classes as well as 'custom commands' (instances of native 
   'Command' class) which are written in Javascript.

   To start the SHELL, use the following code:
     SHELL.evaluate();
    
   This will display a '>' prompt on standard console output, then you may 
   input 'help' command, which prints the following:
     print 'variable':        prints value of variable (e.g: print Object)
     eval 'variable':         evaluates 'variable'
     set 'variable' 'value':  sets value of 'variable'
     edit 'variable':         edit value of 'variable' in default editor 
     new:                     creates an 'Object' instance
     new 'class':             creates an instance of 'class' (eg: new Integer) 
     list 'class':            lists all instances of 'class' (eg: list Command)
     ?, h, help:              prints this text
     q, quit or exit:         exits this Shell

   Now let's start by listing all AOM classes:
     > list Class

   This will print:
       Command
       String
       Class
       Integer
       Object

   Then let's create our first custom command:
     > new Command
       command1
         class: Command
         name: command1
         script():

   Let's list the instances of 'Command' class:
     > list Command
       command1

   Now let's edit command1's script:
     > edit command1

   This will open Notepad to allow edition of command1's script, input the 
   following (please notice that strings are delimited by simple quote '):
     print('hello');

   Now exit Notepad, this will print:
     command1
       class: Command
       name: command1
       script(): print("hello");

   Now let's run our custom command:
     > command1
       hello

   You may have the same output with 'eval' command (because all It instances
   are able to self evaluate):
     > eval command1
       hello

   Last, let's see what happens if we input dummy Javascript code:
     > edit command1

   Now enter the following in Notepad:
     dummy javascript code
  
   Let's see what happens when running our modified script:
    > command1
      sun.org.mozilla.javascript.internal.EvaluatorException: 
      il manque ';' avant une instruction (<Unknown source>#1) in 
      <Unknown source> at line number 1

4. Guidelines
---------------
  All samples/apps built on top of SCOM must comply to the following rules:
    - All implementation classes (not taking into account unit/functional 
      tests) must be subclasses of 'It' class
    - subclasses should not add instance methods to the API (else it will 
      break SCOM paradigm), thus are only allowed to override:
      - 'evaluate()' and optionally 'toString()'
      - constructor: in fact it's a requirement, due to the instanciation 
        of 'It' through a static factory method ('New()')  
        note: SCOM is 'null safe', this is implemented by a native singleton 
              ('NIL') which requires a factory method
    - subclasses should not add static/class methods
    - subclasses must override 'NAME' and 'CLASS_NAME' static fields

  Native Object Model: 'ENVIRONMENT'
  ----------------------------------
  'It' core class provides a native Object Model ('ENVIRONMENT') which is a 
  Singleton populated by 'initEnvironment()'
  note: application may extend 'ENVIRONMENT' by overriding 'initEnvironment()' 
        (see: 'ItLisp' in com.samples.lang.lisp) in that case the overriding 
        'initEnvironment()' must start by calling the parent class's 
        initEnvironment():
         
         protected void initEnvironment()
         {
           super.initEnvironment();
           ...
         }

5. License
---------------
  This project is Open Source, under 'MIT license' 
  (http://en.wikipedia.org/wiki/MIT_License)

6. Samples
---------------

7. To Do
--------------------
- Quality:
  - Unit tests & Automatic Tests
  - count It instances and avoid unnecessary duplication of Literals/Constants 
    (e.g: NEW_F)
- Semantic Network:
  - RDF serialization, may be used to specify native Object Model
  - Graph Layout (with D3 Javascript framework)
- Languages:
  - Implementation of Lisp interpreter
  - Encapsulation of Java embedded interpreter

A. Appendix
-----------
  A.1. Naming conventions
  -----------------------
  - All static methods start with an Upper case letter then CamelCase 
    (e.g: 'NewClass()')
    - private/protected methods have a additional '_' suffix (e.g: 'IsNIL_')
  - instance methods start with a Lower case letter then CamelCase 
    (e.g: 'getKey()')
    - private methods have a additional '_' suffix
  - private instance fields start with a '_' prefix (e.g: _key);
  - Subclasses of It should be prefixed with 'It' (e.g: ItLisp)
    - those which are meant to be a 'method facet' of an It instance should 
      be suffixed with 'F' (e.g: 'ItSuccessorF')
  - variables and methods parameters:
    - should use the 'underscore' naming convention (eg: 'class_it')
    - if they are 'It' instances, they should be suffixed with '_it' 
      (eg: 'class_it')
  - variables which are used for typecasting (e.g: Object to String) should
    be suffixed with '_str' (eg: 'o_str')