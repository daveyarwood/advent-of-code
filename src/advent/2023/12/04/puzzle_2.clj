(ns advent.2023.12.04.puzzle-2
  (:require [advent.2023.12.04.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.set                :as set]))

(defn play-the-game
  "Plays the game and returns the number of cards (originals + copies) you have
   at the end."
  [cards]
  (loop [copies {}
         i      0]
    (let [{:keys [winning-numbers your-numbers]}
          (nth cards i)

          card-quantity
          (inc (get copies i 0))

          number-of-subsequent-cards-to-copy
          (count (set/intersection winning-numbers your-numbers))

          card-indexes-to-copy
          (range (inc i) (+ (inc i) number-of-subsequent-cards-to-copy))]
      (if (= (dec (count cards)) i)
        (+ (count cards)
           (reduce + (vals copies)))
        (recur
          (merge-with
            +
            copies
            (into {}
                  (map #(vector % card-quantity)
                       card-indexes-to-copy)))
          (inc i))))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2023-12-04-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-card)
         play-the-game
         prn)))
