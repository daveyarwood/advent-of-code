(ns advent.2015.12.18.puzzle-1
  (:require [clojure.java.io :as io]))

(defn mvi
  "map-indexed, but it returns a vector."
  [& args]
  (vec (apply map-indexed args)))

(defn parse-row
  [line]
  (mapv #(= \# %) line))

(defn neighbors
  "Given a 2D matrix (a list of rows, each of which is a list of elements) and
   an x and y coordinate, returns a list of elements adjacent to the element at
   that coordinate."
  [rows x y]
  (let [max-x           (dec (count (first rows)))
        max-y           (dec (count rows))
        neighbor-coords (for [xx [(dec x) x (inc x)]
                              yy [(dec y) y (inc y)]
                              :let [coord [xx yy]]
                              :when (and (not= [x y] coord)
                                         (not (neg? xx))
                                         (not (neg? yy))
                                         (<= xx max-x)
                                         (<= yy max-y))]
                          coord)]
    (map #(let [[x y] %] (get-in rows [x y])) neighbor-coords)))

(defn step
  [rows]
  (mvi (fn [x row]
         (mvi (fn [y on?]
                (let [neighbors-on (->> (neighbors rows x y)
                                        (filter identity)
                                        count)]
                  (if on?
                    (contains? #{2 3} neighbors-on)
                    (= 3 neighbors-on))))
              row))
       rows))

(defn count-lights
  [rows]
  (->> rows
       (map #(count (filter identity %)))
       (apply +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-18-01-input"))]
    (->> rdr
         line-seq
         (mapv parse-row)
         (iterate step)
         (drop 100)
         first
         count-lights
         prn)))
