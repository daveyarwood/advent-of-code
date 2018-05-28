(ns advent.2015.12.09.puzzle-1
  (:require [clojure.math.combinatorics :as combo]
            [clojure.java.io            :as io]))

(defn parse-distance
  [s]
  (let [[loc1 loc2 distance] (->> s
                                  (re-matches #"(.*) to (.*) = (\d+)")
                                  rest)]
    [loc1 loc2 (Integer/parseInt distance)]))

(defn route-distance
  [distances route]
  (reduce (fn [total [loc1 loc2]]
            (+ total (->> distances
                          (filter (fn [[l1 l2 distance]]
                                    (= #{l1 l2} #{loc1 loc2})))
                          first
                          (#(get % 2)))))
          0
          (partition 2 1 route)))

(defn route-distances
  [distances]
  (->> distances
       (mapcat butlast)
       (into #{})
       combo/permutations
       (map (partial route-distance distances))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-09-01-input"))]
    (->> rdr line-seq (map parse-distance) route-distances (apply min) prn)))

