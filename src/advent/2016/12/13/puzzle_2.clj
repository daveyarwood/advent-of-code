(ns advent.2016.12.13.puzzle-2
  (:require [advent.2016.12.13.puzzle-1 :as p1]
            [clojure.set                :as set]))

(defn coords-visited-in-up-to-n-steps
  [n start]
  (->> #{start}
       (iterate (fn [visited]
                  (->> visited
                       (mapcat #(cons % (p1/neighbors %)))
                       set)))
       (drop n)
       first
       count))

(def starting-position [1 1])

(defn -main
  []
  (prn (coords-visited-in-up-to-n-steps 50 starting-position)))
