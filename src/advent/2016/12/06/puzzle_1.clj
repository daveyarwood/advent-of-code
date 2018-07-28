(ns advent.2016.12.06.puzzle-1
  (:require [clojure.java.io :as io]))

(defn columns
  [rows]
  (apply map str rows))

(defn most-common-char
  [s]
  (->> s frequencies (apply max-key val) key))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-06-01-input"))]
    (->> rdr line-seq columns (map most-common-char) (apply str) println)))
