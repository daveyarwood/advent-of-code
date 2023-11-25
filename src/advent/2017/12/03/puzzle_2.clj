(ns advent.2017.12.03.puzzle-2
  (:require [advent.2017.12.03.puzzle-1 :as p1]
            [advent.grid                :as grid]))

(defn square-values*
  [i previous-values]
  (let [coordinate      (nth p1/coordinates i)
        neighbor-values (->> (grid/neighbors-with-diagonal
                               previous-values
                               coordinate)
                             (map previous-values)
                             (remove nil?))
        value           (if (empty? neighbor-values)
                          1
                          (apply + neighbor-values))
        values          (assoc previous-values coordinate value)]
    (cons value (lazy-seq (square-values* (inc i) values)))))

(def square-values
  (square-values* 0 {}))

(defn -main
  []
  (->> square-values
       (drop-while #(<= % p1/input))
       first
       prn))
