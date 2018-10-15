(ns advent.2016.12.22.puzzle-2
  (:require [clojure.java.io            :as io]
            [advent.2016.12.22.puzzle-1 :as p1]))

(comment
  "MAN. This one is diabolically hard to solve in code, although fairly easy to
   solve by hand. I ended up reading through the Reddit thread and using a
   Codepen tool someone implemented (https://codepen.io/anon/pen/BQEZzK) to help
   me solve it by hand.

   Below is my attempt to solve this in code. I have no idea if it works or not
   because if it does work, it's prohibitively slow and I didn't wait long
   enough.")

(defn grid
  [nodes]
  (reduce (fn [grid {:keys [position] :as node}]
            (assoc grid position (dissoc node :position)))
          {}
          nodes))

(defn initialize
  [grid start end]
  (assoc-in grid [end :has-target-data?] true))

(defn solved?
  [grid start end]
  (get-in grid [start :has-target-data?]))

(defn neighbors
  [[x y] grid]
  (for [coords [[(inc x) y] [(dec x) y]
                [x (inc y)] [x (dec y)]]
        :let [neighbor (get grid coords)]
        :when neighbor]
    [coords neighbor]))

(defn possible-moves-from
  [[loc-a node-a] grid]
  (for [[loc-b node-b] (neighbors loc-a grid)
        :when (p1/viable? [node-a node-b])]
    (let [transfer-amount (:used node-a)
          updated-node-a  (-> node-a
                              (dissoc :has-target-data?)
                              (assoc :used 0)
                              (update :available + transfer-amount))
          updated-node-b  (-> node-b
                              (merge (when (:has-target-data? node-a)
                                       {:has-target-data? true}))
                              (update :used + transfer-amount)
                              (update :available - transfer-amount))]
      (-> grid
          (assoc loc-a updated-node-a
                 loc-b updated-node-b)))))

(defn possible-moves
  [grid]
  (->> grid
       (mapcat #(possible-moves-from % grid))))

(defn fastest-solution
  [start end grid]
  (loop [timelines #{(initialize grid start end)}]
    (if-let [solution (->> timelines
                           (filter #(solved? % start end))
                           first)]
      solution
      (->> timelines
           (mapcat possible-moves)
           set
           recur))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-22-01-input"))]
    (->> rdr
         line-seq
         (drop 2)
         (map p1/parse-node)
         grid
         (fastest-solution [0 0] [36 0])
         prn)))
