(ns advent.2015.12.06.puzzle-2
  (:require [advent.2015.12.06.puzzle-1 :as p1]
            [clojure.java.io :as io]))

(def initial-value 0)

(def legend
  {"turn on"  inc
   "turn off" #(max 0 (dec %))
   "toggle"   #(+ % 2)})

(defn count-lights
  [grid]
  (->> grid
       (map (fn [row] (reduce + row)))
       (reduce +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-06-01-input"))]
    (-> rdr
        line-seq
        (p1/follow-instructions initial-value legend)
        count-lights
        prn)))
