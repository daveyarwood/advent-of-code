(ns advent.2023.12.06.puzzle-2
  (:require [advent.2023.12.06.puzzle-1 :as p1]))

(def ^:const example-race
  {:time 71530, :distance 940200})

(def ^:const race
  {:time 59796575, :distance 597123410321328})

(comment
  (count (p1/viable-options example-race))
  (count (p1/viable-options race)))

(defn -main
  []
  (-> race p1/viable-options count prn))
