(ns advent.2017.12.06.puzzle-2
  (:require [advent.2017.12.06.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-06-01-input"))]
    (let [cycles       (-> rdr slurp p1/parse-input p1/reallocation-cycles)
          loop-start-i (-> cycles p1/take-while-unique count)]
      (-> cycles
          (nth loop-start-i)
          p1/reallocation-cycles
          p1/take-while-unique
          count
          prn))))
