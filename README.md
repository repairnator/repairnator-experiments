# Nopol [![Build Status](https://travis-ci.org/SpoonLabs/nopol.svg?branch=master)](https://travis-ci.org/SpoonLabs/nopol) [![Coverage Status](https://coveralls.io/repos/github/SpoonLabs/nopol/badge.svg?branch=master)](https://coveralls.io/github/SpoonLabs/nopol?branch=master)

Nopol is an automatic software repair tool for Java. This code is research code, released under the GPL licence.

If you use this code for academic research, please cite:
[Nopol: Automatic Repair of Conditional Statement Bugs in Java Programs](https://hal.archives-ouvertes.fr/hal-01285008/document) (Jifeng Xuan, Matias Martinez, Favio Demarco, Maxime Clément, Sebastian Lamelas, Thomas Durieux, Daniel Le Berre, Daniel Le Berre, Martin Monperrus). IEEE Transactions on Software Engineering, 2016.
```Bibtex
@article{xuan:hal-01285008,
 title = {Nopol: Automatic Repair of Conditional Statement Bugs in Java Programs},
 author = {Xuan, Jifeng and Martinez, Matias and Demarco, Favio and Clément, Maxime and Lamelas, Sebastian and Durieux, Thomas and Le Berre, Daniel and Monperrus, Martin},
 journal = {IEEE Transactions on Software Engineering},
 year = {2016},
}
```

Others papers about Nopol:
* ["Automatic Repair of Buggy If Conditions and Missing Preconditions with SMT"](http://hal.inria.fr/hal-00977798/PDF/NOPOL-Automatic-Repair-of-Buggy-If-Conditions-and-Missing-Preconditions-with-SMT.pdf) (Favio DeMarco, Jifeng Xuan, Daniel Le Berre, Martin Monperrus), In Proceedings of the 6th International Workshop on Constraints in Software Testing, Verification, and Analysis (CSTVA 2014) [(Bibtex)](http://www.monperrus.net/martin/bibtexbrowser.php?key=DeMarco2014&bib=monperrus.bib)
* [DynaMoth: Dynamic Code Synthesis for Automatic Program Repair](https://hal.archives-ouvertes.fr/hal-01279233/document) (Thomas Durieux, Martin Monperrus), In Proceedings of the 11th International Workshop in Automation of Software Test, 2016, describes the dynamic synthesis part of Nopol [(Bibtex)](http://www.monperrus.net/martin/bibtexbrowser.php?key=durieux%3Ahal-01279233&bib=monperrus.bib)  
* [Automatic Repair of Infinite Loops](https://arxiv.org/pdf/1504.05078.pdf) (Sebastian Lamelas-Marcote and Martin Monperrus), Technical report hal-01144026, University of Lille, 2015, describes the Infinitel part. [(Bibtex)](http://www.monperrus.net/martin/bibtexbrowser.php?key=Lamelas2015&bib=monperrus.bib) 

## Getting started

Nopol requires Java and an SMT solver installed on the machine (e.g. Z3)

1) [CoCoSpoon](https://github.com/SpoonLabs/CoCoSpoon):

```
git clone https://github.com/SpoonLabs/CoCoSpoon.git
cd CoCoSpoon
mvn clean install
cd ..
```

2) Compile NoPol:

```
git clone https://github.com/SpoonLabs/nopol.git
cd nopol/nopol
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64

# -DskipTests is required, to run the tests one needs to compile ../test-projects/ (see below)
mvn package -DskipTests
```

3) Locate the Nopol jar file produced at step 2)

```
$ ls target/*jar
target/nopol-0.2-SNAPSHOT.jar
target/nopol-0.2-SNAPSHOT-jar-with-dependencies.jar # we use this one
```
In the following, `nopol.jar` refers to the jar file with dependencies (`target/nopol-<VERSION>-SNAPSHOT-jar-with-dependencies.jar`)

4) Compile the test-projects

```
$ cd ../test-projects/
# compiling app (in target/classes) and tests (in target/test-classes), but don't run the tests (they obviously fail, because the goal is to repair them)
$ mvn test -DskipTests 
```
4b) Optional: run the tests of Nopol to check your installation

```
$ cd ../nopol/
$ mvn test
```

5) Execute Nopol (parameters explained below)

(Long commands are broken in several lines, separated by a backslash, which means an escaped linebreak in Unix shells.)

```
cd ../test-projects/
java -jar nopol.jar \
-s src/main/java/ \
-c target/classes:target/test-classes:/home/<user>/.m2/repository/junit/junit/4.11/junit-4.11.jar:/home/<user>/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar \
-t symbolic_examples.symbolic_example_1.NopolExampleTest \
-p ../nopol/lib/z3/z3_for_linux
```

If you keep `nopol.jar` instead of the actual jar located at the previous step, you'll get `Error: unable to access jarfile nopol.jar` (see above). You should replace also `<user>` by your own username.

It should output something like:
```
----INFORMATION----
Nb classes : 34
Nb methods : 53
Nb statements: 5
Nb statement executed by the passing tests of the patched line: 0
Nb statement executed by the failing tests of the patched line: 0
Nb unit tests : 9
Nb Statements Analyzed : 3
Nb Statements with Angelic Value Found : 1
Nb inputs in SMT : 8
Nb SMT level: 2
Nb SMT components: [4] [== of arity: 2, != of arity: 2, < of arity: 2, <= of arity: 2]
                  class java.lang.Boolean: 4
Nb variables in SMT : 13
Nb run failing test  : [2, 1]
Nb run passing test : [4, 18]
NoPol Execution time : 3262ms
----PATCH FOUND----
symbolic_examples.symbolic_example_1.NopolExample:12: CONDITIONAL index < 1
```

NoPol (SMT and Dynamoth) returns also a unix code (integer):
* 0 if a patch has been found
* -1 otherwise

Parameter `-c` can be found with `mvn dependency:build-classpath`.

## Minimal Usage

4 parameters are required
```
Usage: java -jar nopol.jar

  (-s|--source) source1:source2:...:sourceN 
        Define the path to the source code of the project. For instance `src/main/java`

  (-c|--classpath) <classpath>
        Define the classpath of the project separated by a path separator (`:` on Linux). 
        Must contain the application binary classes (`target/classes`)
        Must contain the application test classes (`target/test-classes`)
        Must contain the library classes (`lib/junit.jar` for instance)
        
  [(-t|--test) test1:test2:...:testN ]
        Define the tests of the project. For instance `symbolic_examples.symbolic_example_1.NopolExampleTest`

  [(-p|--solver-path) <solverPath>]
        Define the solver binary path (only used with smt synthesis). For instance `../nopol/lib/z3/z3_for_linux`

```


## Advanced Usage

See also notes below.

```
Usage: java -jar nopol.jar

  [(-m|--mode) <repair|ranking>]
        Define the mode of execution. (default: repair)

  [(-e|--type) <pre_then_cond|condition|precondition>]
        The type of statement to analyze (only used with repair mode). (default: pre_then_cond)

  [(-o|--oracle) <angelic|symbolic>]
        Define the oracle (only used with repair mode). (default: angelic)

  [(-y|--synthesis) <smt|dynamoth>]
        Define the patch synthesis. (default: smt)

  [(-l|--solver) <z3|cvc4>]
        Define the solver (only used with smt synthesis). (default: z3)

  [--complianceLevel <complianceLevel>]
        The Java version of the project. (default: 7)

  [--maxTime <maxTime>]
        The maximum time execution in minute for the whole execution of Nopol.(default: 10)

  [--maxTimeType <maxTimeType>]
        The maximum time execution in minute for one type of patch per per suspicious statement (eg. 5 minutes max to find a precondition at line x). (default: 5)

  [(-z|--flocal) < cocospoon|dumb|gzoltar>]
        Define the fault localizer to be used. (default: cocospoon). 
          "cocospoon" means source code instrumentation plus ochiai metric.
          "dumb" means random fault localization. 
          "gzoltar" means binary code instrumentation with external library plus ochiai metric.

```

Notes: For using Dynamoth (`-y dynamoth`), you must add tools.jar in the classpath of Nopol, and use java with `-cp` (and not `-jar`):

    java -cp $JAVA_HOME/lib/tools.jar:../nopol/target/nopol-SNAPSHOT-jar-with-dependencies.jar fr.inria.lille.repair.Main <nopol arguments>
 
## Contact

For questions and feedback , please contact martin.monperrus@univ-lille1.fr

