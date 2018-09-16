(ns advent.2016.12.18.puzzle-2
  (:require [advent.2016.12.18.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-18-01-input"))]
    (->> rdr
         line-seq
         first
         p1/parse-row
         (iterate p1/next-row)
         (take 400000)
         p1/count-safe-tiles
         prn)))
