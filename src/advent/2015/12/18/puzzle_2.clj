(ns advent.2015.12.18.puzzle-2
  (:require [advent.2015.12.18.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn mvi
  "map-indexed, but it returns a vector."
  [& args]
  (vec (apply map-indexed args)))

(defn corner?
  [[x y] rows]
  (let [max-x (dec (count (first rows)))
        max-y (dec (count rows))]
    (and (#{0 max-x} x) (#{0 max-y} y))))

(defn step
  [rows]
  (mvi (fn [x row]
         (mvi (fn [y on?]
                (let [neighbors-on (->> (p1/neighbors rows x y)
                                        (filter identity)
                                        count)]
                  (cond
                    (corner? [x y] rows) true
                    on?                  (contains? #{2 3} neighbors-on)
                    :else                (= 3 neighbors-on))))
              row))
       rows))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-18-01-input"))]
    (->> rdr
         line-seq
         (mapv p1/parse-row)
         (iterate step)
         (drop 100)
         first
         p1/count-lights
         prn)))
