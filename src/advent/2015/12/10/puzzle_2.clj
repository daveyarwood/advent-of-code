(ns advent.2015.12.10.puzzle-2
  (:require [advent.2015.12.10.puzzle-1 :as p1]))

(defn -main
  []
  (->> p1/input (iterate p1/look-and-say) (drop 50) first count prn))
