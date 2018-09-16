(ns advent.2016.12.15.puzzle-1
  (:require [clojure.java.io :as io]))

(comment
  "Punting on this one because of the frustrating tendency to end up with
   StackOverflowErrors when using lazy sequences in Clojure. The code below is
   how I'd like to do it, but the stack is standing in my way.")

(defn parse-disc
  [line]
  (->> line
       (re-matches
         (re-pattern
           (str
             "Disc #(\\d+) has (\\d+) positions; "
             "at time=0, it is at position ""(\\d+)\\.")))
       rest
       (map #(Integer/parseInt %))))

(defn time-series
  [discs]
  (->> [0 discs]
       (iterate (fn [[time discs]]
                  [(inc time)
                   (map (fn [[positions position]]
                          [positions
                           (-> position inc (rem positions))])
                        discs)]))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-15-01-input"))]
    (->> rdr
         line-seq
         (map parse-disc)
         (sort-by first)   ; sort by disc number
         (map #(drop 1 %)) ; discard the disc numbers
         time-series
         ;; this is causing a stackoverflow for some reason
         (drop-while (fn [[time discs]]
                       (not-every? (fn [[_ position]] (zero? position))
                                   discs)))
         first
         prn)))
