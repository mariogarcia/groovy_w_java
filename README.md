Interoperation Groovy/Java
==========================

Project showing the inter-operation between Groovy and Java. Basically
it shows how any code written in Groovy can be used a if it were
written in Java.

Pros
====

TODO

Cons
====

TODO

Pitfalls
========

The main pitfalls when trying to mix both languages are those related
with meta-programming, such as

- `Trying to use AST transformations over a Java class`: Transformations
  can only be applied to Groovy code. However the Groovy code
  resulting from the transformation is perfectly visible to the Java
  code.
- `Trying to use Traits over Java code`: Traits can only be used by
  Groovy code.
