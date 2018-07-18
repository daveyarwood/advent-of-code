(ns advent.2016.12.03.puzzle-1
  (:require [clojure.java.io :as io]))

(defn parse-triangle
  [line]
  (->> line (re-seq #"\d+") (map #(Integer/parseInt %))))

(defn possible?
  [triangle]
  (let [[a b c] (sort triangle)]
    (> (+ a b) c)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-03-01-input"))]
    (->> rdr line-seq (map parse-triangle) (filter possible?) count prn)))
