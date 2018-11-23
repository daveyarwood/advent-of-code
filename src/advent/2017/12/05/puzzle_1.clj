(ns advent.2017.12.05.puzzle-1
  (:require [clojure.java.io :as io]))

(comment
  "Thwarted again by StackOverflowError :(")

(defn step
  [[instructions i]]
  (let [offset (nth instructions i)]
    [(concat (take i instructions)
             [(inc offset)]
             (drop (inc i) instructions))
     (+ i offset)]))

(defn steps-from
  [[instructions i]]
  (let [[_ next-i :as next-step] (step [instructions i])]
    (if (>= next-i (count instructions))
      (list next-step)
      (cons next-step (steps-from next-step)))))

(defn steps
  [instructions]
  (steps-from [instructions 0]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-05-01-input"))]
    (->> rdr
         line-seq
         (map #(Integer/parseInt %))
         steps
         count
         prn)))
