(ns advent.2016.12.06.puzzle-2
  (:require [advent.2016.12.06.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn least-common-char
  [s]
  (->> s frequencies (apply min-key val) key))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-06-01-input"))]
    (->> rdr line-seq p1/columns (map least-common-char) (apply str) println)))
