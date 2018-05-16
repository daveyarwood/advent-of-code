(ns advent.2015.12.05.puzzle-1
  (:require [clojure.java.io :as io]))

(defn nice?
  [s]
  (let [pairs (partition 2 1 s)]
    (and (->> s (filter #{\a \e \i \o \u}) count (<= 3))
         (some (fn [[a b]] (= a b)) pairs)
         (not-any? #{[\a \b] [\c \d] [\p \q] [\x \y]} pairs))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-05-01-input"))]
    (->> rdr line-seq (filter nice?) count prn)))

