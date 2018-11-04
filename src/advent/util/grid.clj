(ns advent.util.grid
  (:require [clojure.set :as set]))

;; I threw this together while attempting to solve 2016, Day 24, based on my
;; solution to 2016, Day 13 which was a similar path-finding exercise.
;;
;; I ended up punting on 2016, Day 24, so to be honest, I'm not sure if this
;; really works.

(defn neighbors
  [grid-map [x y] wall-pred]
  (->> [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]]
       (filter (fn [coord]
                 (let [value (get grid-map coord ::off-grid)]
                   (and (not= ::off-grid value)
                        (not (wall-pred value))))))))

(defn- coord?
  [x]
  (and (sequential? x)
       (= 2 (count x))
       (number? (first x))
       (number? (second x))))

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
                    (mapcat #(neighbors grid-map % wall-pred))))))))

