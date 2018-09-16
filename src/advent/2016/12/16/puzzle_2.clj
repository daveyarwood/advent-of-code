(ns advent.2016.12.16.puzzle-2
  (:require [advent.2016.12.16.puzzle-1 :as p1]))

(def disk-length 35651584)

(defn -main
  []
  (->> p1/initial-state
       (p1/fill-disk-length disk-length)
       p1/checksum
       println))

