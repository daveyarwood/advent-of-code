(ns advent.2016.12.01.puzzle-2
  (:require [advent.2016.12.01.puzzle-1 :as p1]
            [clojure.java.io            :as io]))

(defn explode-instructions
  "Passing through a location in the process of following a single instruction
   counts as visiting it. For example, if I'm at [0 0] facing north, and I
   follow the instruction [:right 4] (turn right and go 4 blocks), I visit 4
   locations in the process: [1 0], [2 0], [3 0] and [4 0] where I end up. To
   account for this, each instruction with a distance > 1 is translated into a
   series of instructions with a distance of 1."
  [instructions]
  (->> instructions
       (map (fn [[direction distance]]
              (if (> distance 1)
                (cons [direction 1] (repeat (dec distance) [:no-turn 1]))
                [[direction distance]])))
       (apply concat)))

(defn follow-instruction
  "Like p1/follow-instruction, but now we keep track of each location, and we
   stop as soon as we reach a location that we've already visited, returning
   that location as [:hq location]."
  [[location orientation visited] [direction distance]]
  (if (visited location)
    (reduced [:hq location])
    (conj (p1/follow-instruction [location orientation] [direction distance])
          (conj visited location))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-01-01-input"))]
    (->> rdr
         slurp
         p1/parse-instructions
         explode-instructions
         (reduce follow-instruction [[0 0] [0 1] #{}])
         second
         (p1/blocks-away [0 0])
         prn)))
