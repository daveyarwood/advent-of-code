(ns advent.2015.12.02.puzzle-1
  (:require [clojure.java.io :as io]))

(defn parse-dimensions
  [s]
  (->> (re-matches #"(\d+)x(\d+)x(\d+)" s)
       rest
       (map #(Integer/parseInt %))))

(defn wrapping-paper-cost
  [s]
  (let [[l w h]      (parse-dimensions s)
        surface-area (+ (* 2 l w) (* 2 w h) (* 2 h l))
        slack        (min (* l w) (* w h) (* h l))]
    (+ surface-area slack)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-02-01-input"))]
    (->> rdr line-seq (map wrapping-paper-cost) (reduce +) prn)))
