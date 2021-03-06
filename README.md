# ilivalidator - checks interlis transfer files

## Features
- checks INTERLIS 1+2 transfer files
- enable/disable checks
- custom error messages

## License
ilivalidator is licensed under the LGPL (Lesser GNU Public License).

## Build Status
- master branch [![build status](https://www.travis-ci.org/claeis/ilivalidator.svg?branch=master)](https://www.travis-ci.org/claeis/ilivalidator)
- stable branch [![build status](https://www.travis-ci.org/claeis/ilivalidator.svg?branch=stable)](https://www.travis-ci.org/claeis/ilivalidator)

## System Requirements
For the current version of ilivalidator, you will need a JRE (Java Runtime Environment) installed on your system, version 1.6 or later.
The JRE (Java Runtime Environment) can be downloaded for free from the Website <http://www.java.com/>.

## Software Download 
<https://github.com/claeis/ilivalidator/releases>

## Installing ilivalidator
To install the ilivalidator, choose a directory and extract the distribution file there. 

## Running ilivalidator
The ilivalidator can be started with

    java -jar ilivalidator.jar [options] file.xtf

## Building from source
To build the `ilivalidator.jar`, use

    gradle build

To build a binary distribution, use

    gradle bindist

## Documentation
[docs/ilivalidator.rst](docs/ilivalidator.rst)

