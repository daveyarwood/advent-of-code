(ns advent.2017.12.11.puzzle-2
  (:require [advent.2017.12.11.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(defn farthest-distance
  [path]
  (loop [[direction & more] path, coord [0 0], farthest 0]
    (if direction
      (let [new-coord (p1/go-direction coord direction)
            distance  (p1/shortest-distance new-coord [0 0])]
        (recur more new-coord (max distance farthest)))
      farthest)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-11-01-input"))]
    (-> rdr
        slurp
        str/trim
        (str/split #",")
        farthest-distance
        prn)))
