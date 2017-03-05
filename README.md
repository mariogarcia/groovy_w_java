Interoperation Groovy/Java
==========================

Project showing the inter-operation between Groovy and Java. Basically
it shows how any code written in Groovy can be used a if it were
written in Java.

Running the application
=======================

The application exposes temperature measurements by countries. At the
moment it only exposes the content of csv files. In order to expose
values from these csv files, in `application.yml` change where your
csv files are:

Then add a csv file with the name of the available countries and use
the following format (spain.csv):

```
madrid,01-02-2017,22,15
barcelona,01-02-2017,22,15
valencia,01-02-2017,22,15
```

Finally run the app. From the root folder:

```shell
./gradlew :rest:run
```

And check the values at: http://localhost:4567/measurements/spain

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
