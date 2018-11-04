(ns advent.2017.12.02.puzzle-2
  (:require [advent.2017.12.02.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.math.combinatorics :as combo]))

(defn row-result
  [row]
  (->> (combo/combinations row 2)
       (map sort)
       (filter (fn [[low high]] (zero? (rem high low))))
       first
       (apply (fn [low high] (/ high low)))))

(defn checksum
  [rows]
  (apply + (map row-result rows)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-02-01-input"))]
    (->> rdr line-seq (map p1/parse-row) checksum prn)))

