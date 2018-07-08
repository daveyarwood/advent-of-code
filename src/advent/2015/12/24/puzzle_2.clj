(ns advent.2015.12.24.puzzle-2
  (:require [advent.2015.12.24.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-24-01-input"))]
    (->> rdr
         line-seq
         (map #(Integer/parseInt %))
         ;; After experimenting with the number of packages in the first group,
         ;; I found that 4 is the smallest number of packages whose total weight
         ;; can add up to the same as the other groups.
         (p1/ideal-configuration 4 4)
         p1/quantum-entanglement
         prn)))

