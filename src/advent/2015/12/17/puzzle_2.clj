(ns advent.2015.12.17.puzzle-2
  (:require [advent.2015.12.17.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-17-01-input"))]
    (let [combos  (->> rdr
                       line-seq
                       (map #(Integer/parseInt %))
                       (p1/combinations-summing 150))
          minimum (->> combos (apply min-key count) count)]
      (->> combos (filter #(= minimum (count %))) count prn))))
