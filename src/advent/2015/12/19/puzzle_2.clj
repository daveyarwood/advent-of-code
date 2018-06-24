(ns advent.2015.12.19.puzzle-2
  (:require [advent.2015.12.19.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.set                :as set]))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2015-12-19-01-input"))]
    (let [lines          (line-seq rdr)
          start-molecule (last lines)
          goal-molecule  "e"
          replacements   (->> lines
                              (take-while seq)
                              (map (comp reverse p1/parse-replacement)))]
      (loop [molecule start-molecule
             steps    0]
        (let [possibilities (->> replacements
                                 (map #(p1/possible-replacements molecule %))
                                 (apply set/union))]
          (cond
            (= goal-molecule molecule)
            (prn steps)

            (empty? possibilities)
            (recur start-molecule 0)

            :else
            (recur (rand-nth (seq possibilities)) (inc steps))))))))
