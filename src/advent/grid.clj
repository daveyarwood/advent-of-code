(ns advent.grid
  (:require [clojure.set    :as set]
            [clojure.string :as str]))

(defn str->grid
  "Given a multiline string like \"abc\\ndef\\nghi\", returns a vector o(f
   vectors of characters like [[\\a \\b \\c] [\\d \\e \\f] [\\g \\h \\i]])"
  [string]
  (->> string
       str/split-lines
       (mapv vec)))

(defn value-at-coords
  [grid [x y]]
  (get-in grid [y x]))

(comment
  ;;=> [[\a \b \c]
  ;;    [\d \e \f]
  ;;    [\g \h \i]]
  (str->grid "abc\ndef\nghi")

  ;;=> \f
  (value-at-coords
    (str->grid "abc\ndef\nghi")
    [2 1]))

(defn neighbor-coords-without-diagonal
  [[x y]]
  [[(inc x) y] [(dec x) y]
   [x (inc y)] [x (dec y)]])

(defn neighbor-coords-with-diagonal
  [[x y]]
  [[(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]
   [(dec x) y]                   [(inc x) y]
   [(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]])

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
  (neighbors* neighbor-coords-without-diagonal))

(def neighbors-with-diagonal
  (neighbors* neighbor-coords-with-diagonal))

(defn- neighbors*-v2
  [neighbors-fn]
  (fn [coord grid & [wall-pred]]
    (let [wall-pred (or wall-pred (constantly false))]
      (->> (neighbors-fn coord)
           (filter (fn [neighbor-coord]
                     (let [value (or (value-at-coords grid neighbor-coord)
                                     ::off-grid)]
                       (and (not= ::off-grid value)
                            (not (wall-pred value))))))))))

(def neighbors-without-diagonal-v2
  (neighbors*-v2 neighbor-coords-without-diagonal))

(def neighbors-with-diagonal-v2
  (neighbors*-v2 neighbor-coords-with-diagonal))

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
