(ns advent.2015.12.13.puzzle-2
  (:require [advent.2015.12.13.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn inject-myself
  [affinities]
  (as-> affinities ?
    (map (fn [[person affinities]] [person (assoc affinities "Dave" 0)]) ?)
    (into {} ?)
    (assoc ? "Dave" (zipmap (keys affinities) (repeat 0)))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-13-01-input"))]
    (->> rdr
         line-seq
         p1/parse-affinities
         inject-myself
         p1/seating-arrangements
         (apply max-key second)
         second
         prn)))
