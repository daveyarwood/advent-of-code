(ns advent.2023.12.02.puzzle-2
  (:require [advent.2023.12.02.puzzle-1 :as p1]
            [clojure.java.io :as io]))

(defn- cubes-required
  [{:keys [cube-sets]}]
  (into {}
        (for [color [:red :green :blue]]
          [color
           (->> cube-sets
                (apply
                  max-key
                  (fn [cube-set]
                    (get cube-set color 0)))
                color)])))

(defn- power
  [{:keys [red green blue]}]
  (* red green blue))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2023-12-02-01-input"))]
    (->> rdr
         line-seq
         (reduce (fn [sum line]
                   (let [game (p1/parse-game line)]
                     (+ sum (power (cubes-required game)))))
                 0)
         prn)))
