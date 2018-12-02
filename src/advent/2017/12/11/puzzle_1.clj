(ns advent.2017.12.11.puzzle-1
  (:require [clojure.java.io :as io]
            [clojure.string  :as str]))

(comment
  "hex grid coordinate system reference:

   https://stackoverflow.com/a/5085274/2338327

   http://althenia.net/svn/stackoverflow/hexgrid.png?usemime=1&rev=3

   The problem presents the grid tilted differently from the one above, so in
   order to use this grid system, I'm translating the directions from:

   nw - ne
   w  - e
   sw - se

   to:

   nw - n - ne
   sw - s - se")

(defn go-direction
  [[x y] direction]
  (case direction
    "nw"  [(dec x) (inc y)]
    "n"   [x (inc y)]
    "ne"  [(inc x) y]
    "sw"  [(dec x) y]
    "s"   [x (dec y)]
    "se"  [(inc x) (dec y)]))

(defn follow-path
  [directions start-coord]
  (reduce go-direction start-coord directions))

(defn shortest-distance
  [[x1 y1] [x2 y2]]
  (let [dx (- x2 x1), dy (- y2 y1)]
    (if (= (Integer/signum dx) (Integer/signum dy))
      (Math/abs (+ dx dy))
      (max (Math/abs dx) (Math/abs dy)))))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2017-12-11-01-input"))]
    (-> rdr
        slurp
        str/trim
        (str/split #",")
        (follow-path [0 0])
        (shortest-distance [0 0])
        prn)))
