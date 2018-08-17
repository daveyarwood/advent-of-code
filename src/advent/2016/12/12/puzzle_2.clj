(ns advent.2016.12.12.puzzle-2
  (:require [advent.2016.12.12.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-12-01-input"))]
    (->> rdr
         line-seq
         (mapv p1/parse-instruction)
         (p1/run-instructions {"a" 0 "b" 0 "c" 1 "d" 0})
         prn)))
