(ns advent.2017.12.14.puzzle-2
  (:require [advent.2017.12.14.puzzle-1 :as p1]
            [advent.grid                :as grid]
            [clojure.string             :as str]))

(comment
  "I couldn't quite get this one right, so I punted after a while.")

(defn regions
  [grid]
  (let [grid-map* (for [x (range (count (first grid)))
                        y (range (count grid))]
                    [[x y] (-> grid (nth y) (nth x) (= \1))])
        grid-map  (into {} grid-map*)
        counter   (atom 0)]
    (reduce (fn [regions [[x y] on?]]
              (if on?
                (let [neighbors (grid/neighbors-without-diagonal
                                  grid-map
                                 [x y])
                      region    (or (->> neighbors
                                         (map regions)
                                         (filter identity)
                                         first)
                                    (swap! counter inc))]
                  (assoc regions [x y] region))
                regions))
            {}
            grid-map*)))

(defn print-regions
  [regions window-size]
  (->> (range window-size)
       (map (fn [y]
              (->> (range window-size)
                   (map (fn [x]
                          (get regions [x y] \.)))
                   (apply str))))
       (str/join \newline)
       println))

(defn -main
  []
  (->> p1/input p1/knot-hash-grid regions vals (apply max)))
