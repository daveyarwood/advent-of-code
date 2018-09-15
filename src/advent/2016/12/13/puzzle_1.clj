(ns advent.2016.12.13.puzzle-1
  (:require [clojure.set :as set]))

(def puzzle-input 1350)

(defn wall?
  [[x y]]
  (let [sum           (+ (* x x)
                         (* 3 x)
                         (* 2 x y)
                         y
                         (* y y)
                         puzzle-input)
        binary-string (Integer/toString sum 2)
        active-bits   (count (filter #{\1} binary-string))]
    (odd? active-bits)))

(defn neighbors
  [[x y]]
  (->> [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]]
       (filter (fn [[a b]]
                 (and (not (neg? a))
                      (not (neg? b))
                      (not (wall? [a b])))))))

(defn shortest-path
  [start end]
  (loop [steps 0, visited #{}, realities [start]]
    (if (some #{end} realities)
      steps
      (recur (inc steps)
             (set/union visited (set realities))
             (->> realities
                  (remove visited)
                  (mapcat neighbors))))))

(def starting-position [1 1])
(def target-position [31 39])

(defn -main
  []
  (prn (shortest-path starting-position target-position)))
