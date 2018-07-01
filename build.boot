(set-env!
  :source-paths   #{"src"}
  :resource-paths #{"resources"}
  :dependencies   '[[org.clojure/clojure            "1.9.0"]
                    [org.clojure/data.json          "0.2.6"]
                    [org.clojure/math.combinatorics "0.1.4"]
                    [digest                         "1.4.8"]
                    [instaparse                     "1.4.9"]])

(deftask run
  "Runs the main function in a particular puzzle namespace."
  [y year   YEAR   int "The year."
   d day    DAY    int "The day."
   p puzzle PUZZLE int "The puzzle number for the given day (i.e. 1 or 2)."]
  ;; TODO: left-pad the day number with 0 if less than 10
  (assert year   "Year required.")
  (assert day    "Day required.")
  (assert puzzle "Puzzle number required.")
  (let [day       (if (< day 10) (str \0 day) (str day))
        puzzle-ns (symbol (format "advent.%s.12.%s.puzzle-%s" year day puzzle))
        main-fn   (symbol (str puzzle-ns "/-main"))]
    (require [puzzle-ns])
    ((resolve main-fn))))
