Jupiter Interface
=================

[![Build Status](https://api.travis-ci.org/maichler/sbt-jupiter-interface.png?branch=master)](https://travis-ci.org/maichler/sbt-jupiter-interface)
[![License](https://img.shields.io/hexpm/l/plug.svg)](https://raw.githubusercontent.com/maichler/sbt-jupiter-interface/master/LICENSE)

An implementation of [SBT's test interface](https://github.com/sbt/test-interface) for [JUnit Jupiter](http://junit.org/junit5). This allows you to run JUnit 5 tests from [SBT](http://www.scala-sbt.org/).

The code is split into a runtime library `jupiter-interface` and an SBT plugin `sbt-jupiter-interface`. The runtime library is written in pure Java and does all the heavy lifting like collecting and running tests. The SBT plugin makes the runtime library available to the SBT build by adding it to `sbt.Keys.testFrameworks` and as project `test dependency`. It also overwrites `sbt.Keys.detectTests` with a custom task that uses JUnits discovery mechanism to collect available tests. This step is necessary since SBT is currently not capable of detecting package private test classes.

## Usage

Add the following lines to `./project/plugins.sbt`. See the section [Using Plugins](http://www.scala-sbt.org/release/docs/Using-Plugins.html) in the sbt wiki for more information.

    addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.1.0")

## Releases

 SBT Plugin      | JUnit Platform | JUnit Engine
:----------------|:---------------|:-------------
 0.1.0           | 1.0.0-M4       | 5.0.0-M4
 
 
## Framework Options

The following options are supported when running JUnit Jupiter tests:

 Option                           | Description
:---------------------------------|:---------------------------------
 `-v`                             | Log "test run started" / "test started" / "test run finished" events on log level "info" instead of "debug".
 `-n`                             | Do not use ANSI colors in the output even if sbt reports that they are supported.
 `-s`                             | Try to decode Scala names in stack traces and test names. Fall back silently to non-decoded names if no matching Scala library is on the class path.
 `-a`                             | Show stack traces and exception class name for AssertionErrors (thrown by all assert* methods in JUnit).`
 `-c`                             | Do not print the exception class name prefix for any messages. With this option, only the result of getMessage() plus a stack trace is shown.
 `+v`                             | Turn off `-v`. Takes precedence over `-v`.
 `+q`                             | Turn off `-q`. Takes precedence over `-q`.
 `+n`                             | Turn off `-n`. Takes precedence over `-n`.
 `+s`                             | Turn off `-s`. Takes precedence over `-s`.
 `+a`                             | Turn off `-a`. Takes precedence over `-a`.
 `+c`                             | Turn off `-c`. Takes precedence over `-c`.
 `--display=<MODE>`               | Select an output display mode for when tests are executed. Use either `flat` or `tree`.
 `--tests=<REGEXPS>`              | Run only the tests whose names match one of the specified regular expressions (in a comma-separated list). Non-matched tests are ignored. Only individual test case names are matched, not test classes. Example: For test `MyClassTest.testBasic()` only "testBasic" is matched. Use sbt's `test-only` command instead to match test classes.
 `-Dkey=value`                    | Temporarily set a system property for the duration of the test run. The property is restored to its previous value after the test has ended. Note that system properties are global to the entire JVM and they can be modified in a non-transactional way, so you should run tests serially and not perform any other tasks in parallel which depend on the modified property.
 `--run-listener=<CLASS_NAME>`    | A (user defined) class which extends either `org.junit.platform.launcher.TestExecutionListener` or `net.aichler.jupiter.api.JupiterTestListener`. An instance of this class is created and added to the JUnit Launcher, so that it will receive test execution events. For more information, see [TestExecutionListener](http://junit.org/junit5/docs/current/api/org/junit/platform/launcher/TestExecutionListener.html). *Note: this uses the test-classloader, so the class needs to be defined in `src/test` or `src/main` or included as a test or compile dependency*
 `--include-tags=<NAMES>`         | A comma separated list of tag names that should be included. Only tests with one or more of these tags will be run.
 `--exclude-tags=<NAMES>`         | A comma separated list of tag names that should be excluded. No tests that match one or more of these tags will be run.
 `--trace-dispatch-events`        | Write dispatch events to file `target/jupiterDispatchEvents.log` (used internally to test the event dispatcher).
 `--with-types`                   | When using the standard `flat` display mode, this flag causes the internal JUnit types of test identifiers to be added to the test name.

Any parameter not starting with `-` or `+` is treated as a glob pattern for matching tests. Unlike the patterns given directly to sbt's `test-only` command, the patterns given to jupiter-interface will match against the full test names (as displayed by jupiter-interface) of all atomic test cases, so you can match on test methods and parts of suites.

You can set default options in your build.sbt file:

    testOptions += Tests.Argument(jupiterTestFramework, "-q", "-v")

Or use them with the test-quick and test-only commands:

    test-only -- +q +v *Sequence*h2mem*
    

## Credits

This plugin is heavily inspired by [JUnit Interface](https://github.com/sbt/junit-interface) and the console launcher from [JUnit Team](https://github.com/junit-team/junit5). Parts of the output capturing is based on code from [Apache Geronimo GShell](http://geronimo.apache.org/gshell/index.html).