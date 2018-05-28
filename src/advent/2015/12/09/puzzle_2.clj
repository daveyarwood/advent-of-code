(ns advent.2015.12.09.puzzle-2
  (:require [advent.2015.12.09.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-09-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-distance)
         p1/route-distances
         (apply max)
         prn)))

