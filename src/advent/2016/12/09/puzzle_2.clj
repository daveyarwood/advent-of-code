(ns advent.2016.12.09.puzzle-2
  (:require [advent.2016.12.09.puzzle-1 :as    p1]
            [advent.util.io             :refer (char-seq)]
            [clojure.java.io            :as    io]))

(defn decompressed-length
  "using the algorithm explained here:
   https://www.reddit.com/r/adventofcode/comments/5hbygy/2016_day_9_solutions/dazentu"
  [s]
  (loop [length  0
         i       0
         weights (repeat (count s) 1)
         buffer  nil]
    (cond
      ;; When we've reached the end, return the accumulated length.
      (= (count s) i)
      length

      ;; '(' is the beginning of a marker. When we encounter one, we read it and
      ;; update the following weights according to its values.
      ;;
      ;; This has a multiplier effect when markers are nested within the reach
      ;; of previous markers
      (= \( (nth s i))
      (recur length (inc i) weights "(")

      (= \) (nth s i))
      (let [marker             (str buffer \))
            [size repetitions] (->> marker
                                    (re-matches #"\((\d+)x(\d+)\)")
                                    rest
                                    (map #(Integer/parseInt %)))
            updated-weights    (concat
                                 (take (inc i) weights)
                                 (->> weights
                                      (drop (inc i))
                                      (take size)
                                      (map (partial * repetitions)))
                                 (drop (+ i 1 size) weights))]
        (recur length (inc i) updated-weights nil))

      ;; when `buffer` is non-nil, we are reading a marker
      buffer
      (recur length (inc i) weights (str buffer (nth s i)))

      ;; otherwise, we are reading an "ordinary" character and adding its weight
      ;; to the total length
      :else
      (recur (+ length (nth weights i)) (inc i) weights nil))))


(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-09-01-input"))]
    (->> rdr
         char-seq
         (remove #{\newline})
         (apply str)
         decompressed-length
         prn)))
