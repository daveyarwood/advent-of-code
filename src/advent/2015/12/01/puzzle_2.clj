(ns advent.2015.12.01.puzzle-2
  (:require [advent.2015.12.01.puzzle-1 :as p1]
            [advent.util                :as util]
            [clojure.java.io            :as io]))

(defn first-basement-position
  [instructions]
  (->> instructions
       (reductions p1/follow-instruction 0)
       (map vector (drop 1 (range)))
       (drop-while (fn [[position floor]] (not (neg? floor))))
       first
       first))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-01-01-input"))]
    ;; NB: first-basement-position returns the first position that is IN the
    ;; basement. The problem asks for the first instruction that RESULTS in the
    ;; next position being in the basement, so we subtract one.
    (-> rdr util/char-seq first-basement-position dec prn)))
