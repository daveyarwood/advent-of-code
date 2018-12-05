(ns advent.2017.12.13.puzzle-2
  (:require [advent.2017.12.13.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(comment
  "This takes ridiculously long to return.")

(defn caught?
  [ticks]
  (let [number-of-layers (count (first ticks))]
    (->> ticks
         (take number-of-layers)
         (map-indexed vector)
         (some (fn [[i layers]]
                 (-> layers (nth i) :scanner-position (= 0)))))))

(defn necessary-delay
  [ticks]
  (->> (range)
       (map #(caught? (drop % ticks)))
       (map-indexed vector)
       (filter (fn [[i caught?]] (not caught?)))
       first
       first))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-13-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-layer)
         p1/initialize-firewall
         (iterate p1/tick)
         necessary-delay
         prn)))
