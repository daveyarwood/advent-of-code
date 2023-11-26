(ns advent.2017.12.15.puzzle-2
  (:require [advent.2017.12.15.puzzle-1 :as p1]
            [advent.math                :as math]))

;;; Generator logic for puzzle 2

(defn p2-generator
  "Like the generator for puzzle 1, but it only bothers to return numbers from
   the puzzle 1 generator that are divisble by `divisor`."
  [factor starting-value divisor]
  (->> (p1/generator starting-value (fn [value]
                                      (-> value
                                          (* factor)
                                          (rem 2147483647))))
       (filter #(math/divisible-by? % divisor))))

(comment
  (let [gen-a (p2-generator 16807 65 4)
        gen-b (p2-generator 48271 8921 8)]
    (->> (p1/pair-generator gen-a gen-b)
         (take 5000000)
         (reduce (fn [tally [n1 n2]]
                   (if (p1/last-16-bits-match? n1 n2)
                     (inc tally)
                     tally))
                 0))))

(defn -main
  []
  (let [gen-a (p2-generator 16807 703 4)
        gen-b (p2-generator 48271 516 8)]
    (->> (p1/pair-generator gen-a gen-b)
         (take 5000000)
         (reduce (fn [tally [n1 n2]]
                   (if (p1/last-16-bits-match? n1 n2)
                     (inc tally)
                     tally))
                 0)
         prn)))
