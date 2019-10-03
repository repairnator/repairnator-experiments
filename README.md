# librepair-experiments

This repository aims to contain code of failing projects.

* `builds` folder contains all the Travis build identifiers, and the JSON file contains the result of the Travis API eg <https://api.travis-ci.org/v3/build/348887356> 
* branch `master` contains the documentation of this repo
* each branch corresponds to a failing build in Java
  * each branch has a file `repairnator.json` containing metadata
  * each branch has log files `*.log` of the Maven process
  * each branch has 2 or 3 commits, where:
    * the first commi is the failing one
    * the second one contains metadata and log information
    * the  last one, optional, contains a patch (human or generated? TBD)
