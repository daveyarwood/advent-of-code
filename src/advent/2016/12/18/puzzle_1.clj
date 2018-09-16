(ns advent.2016.12.18.puzzle-1
  (:require [clojure.core.match :refer (match)]
            [clojure.java.io    :as    io]))

(defn parse-row
  [line]
  (map #(if (#{\^} %) :trap :safe) line))

(defn next-row
  [row]
  (->> (concat [:safe] row [:safe])
       (partition 3 1)
       (map #(match (vec %)
               [:trap :trap :safe] :trap
               [:safe :trap :trap] :trap
               [:trap :safe :safe] :trap
               [:safe :safe :trap] :trap
               :else               :safe))))

(defn count-safe-tiles
  [rows]
  (->> rows
       (map #(count (filter #{:safe} %)))
       (apply +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-18-01-input"))]
    (->> rdr
         line-seq
         first
         parse-row
         (iterate next-row)
         (take 40)
         count-safe-tiles
         prn)))
