(ns advent.2015.12.17.puzzle-1
  (:require [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]))

(defn combinations-summing
  [total available-sizes]
  (->> available-sizes
       ;; treat each number as a unique value, even if it's a duplicate
       (map #(vector (gensym) %))
       combo/subsets
       ;; extract the numbers out of the [gensym number] tuples
       (map #(map second %))
       (filter #(= total (apply + %)))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-17-01-input"))]
    (->> rdr
         line-seq
         (map #(Integer/parseInt %))
         (combinations-summing 150)
         count
         prn)))
