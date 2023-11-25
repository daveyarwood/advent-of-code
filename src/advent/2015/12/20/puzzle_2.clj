(ns advent.2015.12.20.puzzle-2
  (:require [advent.math :as math]))

(defn presents-for-house
  [n]
  (->> n
       math/all-factors
       (drop-while #(-> % (* 50) (< n)))
       (map (partial * 11))
       (apply +)))

(defn -main
  []
  (->> (range)
       (map #(vector % (presents-for-house %)))
       (drop-while (fn [[_ presents]] (< presents 36000000)))
       first
       first
       prn))

