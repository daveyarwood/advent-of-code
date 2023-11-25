(ns advent.2017.12.03.puzzle-1
  (:require [advent.grid :as grid]))

(comment
  "This naïve solution works for small inputs, but takes impossibly long for the
   puzzle input.")

(def input 265149)

(def spiral-pattern
  (->> (range)
       (map inc)
       (partition 2)
       (map (fn [[a b]]
              (concat (repeat a :east)
                      (repeat a :north)
                      (repeat b :west)
                      (repeat b :south))))
       (apply concat)
       lazy-seq))

(defn go
  [[x y] direction]
  (case direction
    :east  [(inc x) y]
    :north [x (inc y)]
    :west  [(dec x) y]
    :south [x (dec y)]))

(defn coordinates*
  [i coordinate]
  (let [direction       (nth spiral-pattern i)
        next-coordinate (go coordinate direction)]
    (cons coordinate
          (lazy-seq (coordinates* (inc i) next-coordinate)))))

(def coordinates
  (coordinates* 0 [0 0]))

(defn steps-for-square
  [n]
  (grid/manhattan-distance [0 0] (nth coordinates (dec n))))

(defn -main
  []
  (prn (steps-for-square input)))

