(ns advent.2016.12.01.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn parse-instruction
  [s]
  (->> s
       (re-matches #"([LR])(\d+)")
       rest
       ((fn [[turn advance]]
          [(if (= "L" turn) :left :right)
           (Integer/parseInt advance)]))))

(defn parse-instructions
  [input]
  (as-> input ?
    (str/trim ?)
    (str/split ? #", ")
    (map parse-instruction ?)))

(def directions
  [[0 1]    ; north
   [1 0]    ; east
   [0 -1]   ; south
   [-1 0]]) ; west

(defn turn
  [orientation direction]
  (as-> (cycle directions) ?
    (drop-while (partial not= orientation) ?)
    (nth ? (case direction :left 3 :right 1 0))))

(defn follow-instruction
  "location: a coordinate pair, i.e. [x y]

   orientation: which direction you're facing, represented as an [x y] delta
   tuple. for example, [0 1] represents north, because the y coordinate will
   increment when you move forward. similarly, [-1 0] represents west because
   the x coordinate will decrement.

   direction: which direction to turn, :left or :right

   distance: how many blocks to move forward after turning left or right"
  [[location orientation] [direction distance]]
  (let [orientation (turn orientation direction)]
    [(mapv (fn [coord delta] (+ coord (* delta distance)))
           location
           orientation)
     orientation]))

(defn blocks-away
  [[x1 y1] [x2 y2]]
  (+ (Math/abs (- x2 x1)) (Math/abs (- y2 y1))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-01-01-input"))]
    (->> rdr
         slurp
         parse-instructions
         (reduce follow-instruction [[0 0] [0 1]])
         first
         (blocks-away [0 0])
         prn)))
