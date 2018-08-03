(ns advent.2016.12.08.puzzle-2
  (:require [advent.2016.12.08.puzzle-1 :as p1]
            [clojure.java.io            :as io]
            [clojure.string             :as str]))

(defn print-screen
  [screen]
  (->> screen
       (map (fn [row]
              (->> row
                   (map #(if % \# \.))
                   (apply str))))
       (str/join \newline)
       (println)))

(defn -main
  []
  (with-open [rdr (io/reader (io/resource "2016-12-08-01-input"))]
    (->> rdr
         line-seq
         (map p1/parse-instruction)
         (reduce p1/follow-instruction (p1/blank-screen 50 6))
         print-screen)))
