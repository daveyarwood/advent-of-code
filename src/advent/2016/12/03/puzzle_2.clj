(ns advent.2016.12.03.puzzle-2
  (:require [advent.2016.12.03.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn regroup-vertically
  [lines]
  (->> lines
       (partition 3)
       (map (fn [[row-1 row-2 row-3]]
              (map list row-1 row-2 row-3)))
       (apply concat)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-03-01-input"))]
    (->> rdr
         line-seq
         ;; these aren't actually the triangles we're working with, but as a
         ;; parser it still works, and then we can change the way we group the
         ;; numbers
         (map p1/parse-triangle)
         ;; group the numbers into 3-tuples going down instead of left to right
         regroup-vertically
         (filter p1/possible?)
         count
         prn)))
