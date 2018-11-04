(ns advent.2017.12.02.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-row
  [line]
  (as-> line ?
    (str/split ? #"\s+")
    (map #(Integer/parseInt %) ?)))

(defn row-difference
  [row]
  (- (apply max row) (apply min row)))

(defn checksum
  [rows]
  (apply + (map row-difference rows)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-02-01-input"))]
    (->> rdr line-seq (map parse-row) checksum prn)))

