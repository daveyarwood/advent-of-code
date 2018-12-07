(ns advent.2017.12.14.puzzle-1
  (:require [advent.2017.12.10.puzzle-2 :as knot]
            [clojure.string             :as str]))

(defn hash->bits
  [hash-str]
  (->> (for [c hash-str]
         (as-> c ?
           (str ?)
           (Integer/parseInt ? 16)
           (Integer/toBinaryString ?)
           (format "%4s" ?)
           (str/replace ? #"\s" "0")))
       (apply str)))

(defn knot-hash-grid
  [input]
  (for [row (range 128)]
    (-> input (str \- row) knot/knot-hash hash->bits)))

(defn count-used-squares
  [grid]
  (->> grid
       (map #(->> % (filter #{\1}) count))
       (reduce +)))

(def input "hxtvlmkl")

(defn -main
  []
  (-> input knot-hash-grid count-used-squares prn))
