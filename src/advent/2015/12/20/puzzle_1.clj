(ns advent.2015.12.20.puzzle-1
  (:require [advent.util.math :as math]))

(defn presents-for-house
  [n]
  (->> n math/all-factors (map (partial * 10)) (apply +)))

(defn -main
  []
  (->> (range)
       (map #(vector % (presents-for-house %)))
       (drop-while (fn [[_ presents]] (< presents 36000000)))
       first
       first
       prn))

