(ns advent.grid
  (:require [clojure.set :as set]))

(defn- neighbors*
  [neighbors-fn]
  (fn [grid-map coord & [wall-pred]]
    (let [wall-pred (or wall-pred (constantly false))]
      (->> (neighbors-fn coord)
           (filter (fn [neighbor-coord]
                     (let [value (get grid-map neighbor-coord ::off-grid)]
                       (and (not= ::off-grid value)
                            (not (wall-pred value))))))))))

(def neighbors-without-diagonal
  (neighbors* (fn [[x y]]
                [[(inc x) y] [(dec x) y]
                 [x (inc y)] [x (dec y)]])))

(def neighbors-with-diagonal
  (neighbors* (fn [[x y]]
                [[(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]
                 [(dec x) y]                   [(inc x) y]
                 [(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]])))

(defn- coord?
  [x]
  (and (sequential? x)
       (= 2 (count x))
       (number? (first x))
       (number? (second x))))

;; I threw this together while attempting to solve 2016, Day 24, based on my
;; solution to 2016, Day 13 which was a similar path-finding exercise.
;;
;; I ended up punting on 2016, Day 24, so to be honest, I'm not sure if this
;; really works.
(defn minimum-steps
  [grid-map pair wall-pred]
  {:pre [(map? grid-map)
         (every? (fn [[k v]] (coord? k)) grid-map)
         (set? pair)
         (every? coord? pair)]}
  (let [[start end] (seq pair)]
    (loop [steps 0, visited #{}, realities [start]]
      (if (some #{end} realities)
        steps
        (recur (inc steps)
               (set/union visited (set realities))
               (->> realities
                    (remove visited)
                    (mapcat #(neighbors-without-diagonal
                               grid-map
                               %
                               wall-pred))))))))

(defn manhattan-distance
  [[a b] [c d]]
  (+ (Math/abs (- c a))
     (Math/abs (- d b))))
