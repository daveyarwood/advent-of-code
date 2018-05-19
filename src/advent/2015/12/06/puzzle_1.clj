(ns advent.2015.12.06.puzzle-1
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def initial-value false)

(def legend
  {"turn on"  (constantly true)
   "turn off" (constantly false)
   "toggle"   not})

(defn initial-grid
  [initial-value]
  (->> initial-value (repeat 1000) (into []) (repeat 1000) (into [])))

(defn parse-instruction
  [instruction]
  (let [[command & the-rest] (-> #"([^\d]+)(\d+),(\d+) through (\d+),(\d+)"
                                 (re-matches instruction)
                                 rest)
        [x1 y1 x2 y2]         (map #(Integer/parseInt %) the-rest)]
    [(str/trim command) x1 y1 x2 y2]))

(defn follow-instruction
  [legend]
  (fn [grid instruction]
    (let [[command x1 y1 x2 y2] (parse-instruction instruction)
          coordinates           (for [x (range x1 (inc x2))
                                      y (range y1 (inc y2))]
                                  [x y])
          act                   (get legend command)]
      (reduce (fn [grid coordinate] (update-in grid coordinate act))
              grid
              coordinates))))

(defn follow-instructions
  [instructions initial-value legend]
  (reduce (follow-instruction legend)
          (initial-grid initial-value)
          instructions))

(defn count-lights
  [grid]
  (->> grid
       (map (fn [row] (->> row (filter identity) count)))
       (reduce +)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-06-01-input"))]
    (-> rdr
        line-seq
        (follow-instructions initial-value legend)
        count-lights
        prn)))
