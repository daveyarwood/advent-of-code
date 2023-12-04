(ns advent.2023.12.02.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(defn- parse-cube-set
  [string]
  (->> (str/split string #",\s*")
       (map #(let [[_ amount color] (re-matches #"(\d+) (.*)" %)]
               [(keyword color) (parse-long amount)]))
       (into {})))

(defn- parse-cube-sets
  [string]
  (->> (str/split string #";\s*")
       (map parse-cube-set)))

(defn parse-game
  [string]
  (let [[_ game-number cube-sets] (re-matches #"Game (\d+): (.*)" string)]
    {:id        (parse-long game-number)
     :cube-sets (parse-cube-sets cube-sets)}))

(comment
  ;;=> {:id 1,
  ;;    :cube-sets
  ;;    ({:green 1, :blue 2}
  ;;     {:red 13, :blue 2, :green 3}
  ;;     {:green 4, :red 14})}
  (parse-game
    "Game 1: 1 green, 2 blue; 13 red, 2 blue, 3 green; 4 green, 14 red"))

(defn game-possible?
  [{:keys [cube-sets]} inventory]
  (every?
    (fn [cube-set]
      (every? (fn [[color amount]]
                (>= (get inventory color 0) amount))
              cube-set))
    cube-sets))

(comment
  ;;=> (true true false false true)
  (map
    #(game-possible?
       (parse-game %)
       {:red 12, :green 13, :blue 14})
    ["Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
     "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue"
     "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red"
     "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red"
     "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2023-12-02-01-input"))]
    (->> rdr
         line-seq
         (reduce (fn [sum line]
                   (let [{:keys [id] :as game} (parse-game line)]
                     (if (game-possible? game {:red 12, :green 13, :blue 14})
                       (+ sum id)
                       sum)))
                 0)
         prn)))
