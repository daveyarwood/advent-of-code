(ns advent.2015.12.23.puzzle-2
  (:require [advent.2015.12.23.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-23-01-input"))]
    (->> rdr
         line-seq
         (mapv p1/parse-instruction)
         (p1/run-instructions {"a" 1 "b" 0})
         prn)))
