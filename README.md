# BasicMon - Really Simple Monitors for Java

This software is based loosely on the [Java Simon](https://github.com/virgo47/javasimon) project, which is itself
based loosely on the JaMON project. Java Simon has many more features than BasicMon and is almost as fast, or in
some cases faster (mostly with BasicMon statistics enabled). I highly recommend using Java Simon, unless:

  * your project has very basic timing requirements, or
  * the smallest timing overhead is required, or
  * you need to be able to select different timing implementations (i.e.use of atomic classes vs synchronized keyword)
    depending on your specific platform and concurrency requirements.

This project grew from a test case I put together for
[Javasimon Issue #21](https://code.google.com/p/javasimon/issues/detail?id=21) which reported that Java Simon
confusingly included the time it took to obtain a lock to the timer reference, in the timing of the timed block
itself.

In any of these cases, BasicMon should suit you well.

## Build

  * Compiled Simon jars depend on:
    * JDK 1.6 or higher (1.5 may work but has not been tested);
  * Use "mvn" to build the Simon:
  * Commons-math is required at compile time, optional at runtime but required for extra statistics

## Usage

There are two types of basic monitors available: `Counter` and `Timers`.

Counters track a single long value and its average. If extra stats are enabled, its min, max, and variance are
also tracked.

Timers measure time and tracks number of measurements (splits), total time, and average time. If extra stats are
enabled, the timer split minimum, maximum, and variance are also tracked.

Extra stats require commons-math and add significant timing overhead.

### Basic Mon Manager

You obtain BasicMon implementations from the `BasicMonManager`:

```java
BasicTimer timer = BasicMonManager.getTimer("a-stopwatch");
```

Here we obtained a BasicTimer monitor. If the monitor is accessed for the first time it is created. If you accessing
an existing monitor, the existing monitor is returned. Other BasicMonManager methods can be used to obtain timers
or counters with specific settings, or with different implementations depending on your requirements. The primary
implementation choice is to use synchronization-based monitors or atomic-based monitors. See the javadocs for more
details and guidance.

### Timer

Using a timer is simple:

```java
BasicTimerSplit split = timer.start(); // returns split object
// here goes the measured code
long time = split.stop(); // returns the split time in ns
```

After few runs of your measured code you can get additional information:

```java
long totalNanos = timer.getTotal();
long maxSplit = timer.getMax();
long minSplit = timer.getMin();
... etc ...
```

The BasicMonManager, BasicTimer's, and BasicCounter's all have useful toString implementations.

### Selecting Synchronized vs Atomic Timers/Counters

The timer and counter based on java synchronization is the default. This timer is generally a good
choice for most situations. The alternative `java.util.concurrent.atomic`-based timer and counter may
be a better choice for some concurrency scenarios, or on some platforms. The included benchmarks can
be used to check this.

On my platform, atomics are slightly (but not much) better in single-thread operations, or with very
high contention (threads much greater than CPUs), but with multi-threaded operations with a reasonable
number of threads synchronization is best.

Use BasicMonManager.getAtomicTimer or BasicMonManager.getAtomicCounter to get an implementation based on
`java.util.concurrent.atomic`.

## Resources

Project is hosted on Github as "basicmon":
  * Project page: http://github.com/rocketraman/basicmon
  * Issue tracker: http://github.com/rocketraman/basicmon/issues

Project uses the following libraries:
  * Commons math: http://commons.apache.org/math/ (Extra statistics, optional at runtime)

## License

This software is distributed under the terms of the LGPL License:
  * check "lgpl.txt" in the root directory of the project
  * portions of this code are from the Java Simon project, and are included with permission
