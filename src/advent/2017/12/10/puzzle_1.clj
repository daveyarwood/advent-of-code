(ns advent.2017.12.10.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-lengths
  [s]
  (as-> s ?
    (str/trim ?)
    (str/split ? #",\s*")
    (map #(Integer/parseInt %) ?)))

(defn knot-hash
  [lengths [circle i skip-size]]
  (loop [circle          circle
         i               i
         skip-size       skip-size
         [length & more] lengths]
    (if length
      (let [section-indices (map #(rem % (count circle))
                                 (range i (+ i length)))
            section         (map circle section-indices)]
        (recur (reduce (fn [circle [i n]] (assoc circle i n))
                       circle
                       (map vector section-indices (reverse section)))
               (rem (+ i length skip-size) (count circle))
               (inc skip-size)
               more))
      [circle i skip-size])))

(defn multiply-first-two
  [circle]
  (* (first circle) (second circle)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-10-01-input"))]
    (-> rdr
        slurp
        parse-lengths
        (knot-hash [(vec (range 256)) 0 0])
        first
        multiply-first-two
        prn)))
