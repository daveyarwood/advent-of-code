# Advent of Code

Working my way through the [Advent of Code](http://adventofcode.com/) problems,
probably all in Clojure, although I'm not opposed to switching languages if it
happens to make sense for a particular problem.

I'm late to the party, so I'm starting on the 2015 problems in May 2018. I'll
catch up eventually.

## Running puzzle solutions

Assuming you have [Boot][boot-clj] installed, you can use the `run` task defined
in `build.boot` to run the main function for a particular puzzle. For example:

```bash
# Run advent.2015.12.01.puzzle-1/-main
$ boot run -y 2015 -d 1 -p 1
```

[boot-clj]: https://github.com/boot-clj/boot
