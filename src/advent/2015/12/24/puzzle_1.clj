(ns advent.2015.12.24.puzzle-1
  (:require [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]))

(defn quantum-entanglement
  [group]
  (apply * group))

(defn ideal-configuration
  [groups group-1-size weights]
  (let [total-weight    (apply + weights)
        group-weight    (/ total-weight groups)
        group-1-options (as-> weights ?
                          (combo/combinations ? group-1-size)
                          (filter #(= group-weight (apply + %)) ?))]
    (->> group-1-options
         (apply min-key quantum-entanglement))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-24-01-input"))]
    (->> rdr
         line-seq
         (map #(Integer/parseInt %))
         ;; After experimenting with the number of packages in the first group,
         ;; I found that 6 is the smallest number of packages whose total weight
         ;; can add up to the same as the other groups.
         (ideal-configuration 3 6)
         quantum-entanglement
         prn)))

