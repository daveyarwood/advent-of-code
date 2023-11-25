(ns advent.2016.12.24.puzzle-1
  (:require [advent.grid     :as grid]
            [clojure.java.io :as io]))

(comment
  "I got stuck on this one too. To be honest, all these path-finding problems
   are getting a little old. I'm just not knowledgable about performant
   path-finding algorithms.")

(defn parse-location
  [c]
  (case c
    \# :wall
    \. :open
    (Character/digit c 10)))

(defn grid-map
  [rows]
  (->> rows
       (map-indexed (fn [y row]
                      (->> row
                           (map-indexed (fn [x value]
                                          [[x y] value])))))
       (apply concat)
       (into {})))

(defn parse-grid
  [lines]
  (->> lines
       (map #(map parse-location %))
       grid-map))

(defn wire-locations
  [grid-map]
  (into {}
    (for [[k v] grid-map
          :when (number? v)]
      [v k])))

(defn wire-pairs
  [wire-locations]
  (let [wires (keys wire-locations)]
    (set (for [wire wires
               other-wire (filter (complement #{wire}) wires)]
           (set [wire other-wire])))))

(def distance-between-wire-pair
  (-> (fn [grid-map pair]
        (grid/minimum-steps grid-map pair #{:wall})
       memoize)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-24-01-input"))]
    (let [grid           (-> rdr line-seq parse-grid)
          wire-locations (wire-locations grid)
          wire-pairs     (->> wire-locations
                              wire-pairs
                              (map (fn [pair]
                                     [pair
                                      (let [[a b] (seq pair)]
                                        (distance-between-wire-pair
                                          grid
                                          #{(get wire-locations a)
                                            (get wire-locations b)}))]))
                              (into {}))]
      (prn wire-pairs))))

