(ns advent.util.math
  (:require [clojure.math.combinatorics :as combo]))

(defn divisible-by?
  [x y]
  (zero? (rem x y)))

(defn prime-factors
  [n]
  (if (<= n 1)
    ()
    (let [factor (as-> n ?
                   (Math/sqrt ?)
                   (Math/ceil ?)
                   (inc ?)
                   (range 3 ? 2)
                   (cons 2 ?)
                   (filter #(divisible-by? n %) ?)
                   (first ?)
                   (or ? n))]
      (concat [factor] (prime-factors (/ n factor))))))

(defn all-factors
  [n]
  (if (zero? n)
    '(0)
    (->> n prime-factors combo/subsets (map #(apply * %)) sort)))

