(ns advent.2015.12.02.puzzle-2
  (:require [advent.2015.12.02.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn ribbon-cost
  [s]
  (let [[l w h]        (p1/parse-dimensions s)
        [s1 s2 _]      (sort [l w h])
        present-ribbon (+ (* 2 s1) (* 2 s2))
        bow-ribbon     (* l w h)]
    (+ present-ribbon bow-ribbon)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-02-01-input"))]
    (->> rdr line-seq (map ribbon-cost) (reduce +) prn)))
