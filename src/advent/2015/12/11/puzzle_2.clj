(ns advent.2015.12.11.puzzle-2
  (:require [advent.2015.12.11.puzzle-1 :as p1]))

(defn -main
  []
  (-> p1/input p1/next-password p1/next-password prn))
